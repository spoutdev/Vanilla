/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol;

import static org.spout.vanilla.material.VanillaMaterials.getMinecraftId;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.generator.WorldGenerator;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.ChunkSnapshot;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Quaternion;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.protocol.Session.State;
import org.spout.api.protocol.event.ProtocolEventListener;
import org.spout.api.util.map.TIntPairObjectHashMap;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.protocol.msg.BlockActionMessage;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;
import org.spout.vanilla.protocol.msg.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.LoadChunkMessage;
import org.spout.vanilla.protocol.msg.LoginRequestMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.RespawnMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotsMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.window.Window;
import org.spout.vanilla.world.generator.VanillaBiome;
import org.spout.vanilla.world.generator.nether.NetherGenerator;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

public class VanillaNetworkSynchronizer extends NetworkSynchronizer implements ProtocolEventListener {
	@SuppressWarnings("unused")
	private final static int POSITION_UPDATE_TICKS = 20;
	private final static double STANCE = 1.6D;
	private final static int TIMEOUT = 15000;

	public VanillaNetworkSynchronizer(Player player, Entity entity) {
		super(player, player.getSession(), entity);
		registerProtocolEvents(this);
		initChunk(player.getEntity().getPosition());
	}

	private TIntPairObjectHashMap<TIntHashSet> activeChunks = new TIntPairObjectHashMap<TIntHashSet>();
	private final TIntObjectHashMap<Message> queuedInventoryUpdates = new TIntObjectHashMap<Message>();

	@Override
	protected void freeChunk(Point p) {
		int x = (int) p.getX() >> Chunk.BLOCKS.BITS;
		int y = (int) p.getY() >> Chunk.BLOCKS.BITS; // + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.BLOCKS.BITS;

		if (y < 0 || y >= p.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return;
		}

		TIntHashSet column = activeChunks.get(x, z);
		if (column != null) {
			column.remove(y);
			if (column.isEmpty()) {
				activeChunks.remove(x, z);
				LoadChunkMessage unLoadChunk = new LoadChunkMessage(x, z, false);
				owner.getSession().send(unLoadChunk);
			}/* else {
				byte[][] data = new byte[16][];
				data[y] = AIR_CHUNK_DATA;
				CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, false, new boolean[16], 0, data, null);
				session.send(CCMsg);
			}*/
		}
	}

	private static final byte[] SOLID_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final byte[] AIR_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];

	static {
		int i = 0;
		for (int c = 0; c < Chunk.BLOCKS.VOLUME; c++) { // blocks
			SOLID_CHUNK_DATA[i] = 1;
			AIR_CHUNK_DATA[i++] = 0;
		}

		for (int c = 0; c < Chunk.BLOCKS.HALF_VOLUME; c++) { // block data
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = 0x00;
		}

		for (int c = 0; c < Chunk.BLOCKS.HALF_VOLUME; c++) { // block light
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = 0x00;
		}

		for (int c = 0; c < Chunk.BLOCKS.HALF_VOLUME; c++) { // sky light
			SOLID_CHUNK_DATA[i] = 0x00;
			AIR_CHUNK_DATA[i++] = (byte) 0xFF;
		}
	}

	@Override
	protected void initChunk(Point p) {
		int x = (int) p.getX() >> Chunk.BLOCKS.BITS;
		int y = (int) p.getY() >> Chunk.BLOCKS.BITS;// + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.BLOCKS.BITS;

		if (y < 0 || y >= p.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return;
		}

		TIntHashSet column = activeChunks.get(x, z);
		if (column == null) {
			int[][] heights = getColumnHeights(p);

			byte[][] packetChunkData = new byte[16][];
			
			for (int cube = 0; cube < 16; cube++) {
				packetChunkData[cube] = getChunkHeightMap(heights, cube);

			}

			column = new TIntHashSet();
			activeChunks.put(x, z, column);
			LoadChunkMessage loadChunk = new LoadChunkMessage(x, z, true);
			owner.getSession().send(loadChunk);

			Chunk chunk = p.getWorld().getChunkFromBlock(p);
			byte[] biomeData = new byte[Chunk.BLOCKS.AREA];
			for (int dx = x; dx < x + Chunk.BLOCKS.SIZE; ++dx) {
				for (int dz = z; dz < z + Chunk.BLOCKS.SIZE; ++dz) {
					Biome biome = chunk.getBiomeType(dx & Chunk.BLOCKS.MASK, 0, dz & Chunk.BLOCKS.MASK);
					if (biome instanceof VanillaBiome) {
						biomeData[(dz & Chunk.BLOCKS.MASK) << 4 | (dx & Chunk.BLOCKS.MASK)] = (byte) ((VanillaBiome) biome).getBiomeId();
					}
				}
			}

			CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, true, new boolean[16], 0, packetChunkData, biomeData);
			owner.getSession().send(CCMsg);
		}
		column.add(y);
	}
	
	private static int[][] getColumnHeights(Point p) {
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];

		World w = p.getWorld();

		for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				heights[xx][zz] = w.getSurfaceHeight(p.getBlockX() + xx, p.getBlockZ() + zz, true);
			}
		}
		return heights;
	}
	
	private static byte[] getChunkHeightMap(int[][] heights, int chunkY) {
		byte[] packetChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
		int baseY = chunkY << Chunk.BLOCKS.BITS;
		int blockYStep = 1 << (Chunk.BLOCKS.BITS << 1);
		int lightYStep = blockYStep >> 1;
		int skylightBase = Chunk.BLOCKS.VOLUME << 1;

		for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				int dataOffset = xx | (zz << 4);
				int threshold = heights[xx][zz] - baseY;
				if (chunkY == 0 && threshold < 0) {
					threshold = 0;
				}
				int yy;
				for (yy = 0; yy < Chunk.BLOCKS.SIZE && yy <= threshold ; yy++) {
					packetChunkData[dataOffset] = 1;
					dataOffset += blockYStep;
				}
				byte mask = (xx & 0x1) == 0 ? (byte) 0x0F : (byte) 0xF0;
				dataOffset = skylightBase + ((xx | (zz << 4) + (yy << (Chunk.BLOCKS.SIZE << 1))) >> 1);
				for (; yy < Chunk.BLOCKS.SIZE; yy++) {
					packetChunkData[dataOffset] |= mask;
					dataOffset += lightYStep;
				}
			}
		}
		return packetChunkData;
	}

	@Override
	public void sendChunk(Chunk c) {
		int x = c.getX();
		int y = c.getY();// + SEALEVEL_CHUNK;
		int z = c.getZ();

		if (y < 0 || y >= c.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return;
		}

		ChunkSnapshot snapshot = c.getSnapshot(false);
		short[] rawBlockIdArray = snapshot.getBlockIds();
		short[] rawBlockData = snapshot.getBlockData();
		byte[] rawBlockLight = snapshot.getBlockLight();
		byte[] rawSkyLight = snapshot.getSkyLight();
		byte[] fullChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];

		int arrIndex = 0;
		for (int i = 0; i < rawBlockIdArray.length; i++) {
			short convert = getMinecraftId(rawBlockIdArray[i]);
			fullChunkData[arrIndex++] = (byte) (convert & 0xFF);
		}

		for (int i = 0; i < rawBlockData.length; i += 2) {
			fullChunkData[arrIndex++] = (byte) ((rawBlockData[i + 1] << 4) | (rawBlockData[i] & 0xF));
		}

		System.arraycopy(rawBlockLight, 0, fullChunkData, arrIndex, rawBlockLight.length);
		arrIndex += rawBlockLight.length;

		System.arraycopy(rawSkyLight, 0, fullChunkData, arrIndex, rawSkyLight.length);

		arrIndex += rawSkyLight.length;

		byte[][] packetChunkData = new byte[16][];
		packetChunkData[y] = fullChunkData;
		CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, false, new boolean[16], 0, packetChunkData, null);
		owner.getSession().send(CCMsg);
	}

	@Override
	protected void sendPosition(Point p, Quaternion rot) {
		//TODO: Implement Spout Protocol
		PlayerPositionLookMessage PRMsg = new PlayerPositionLookMessage(p.getX(), p.getY() + STANCE, p.getZ(), p.getY(), rot.getYaw(), rot.getPitch(), true);
		owner.getSession().send(PRMsg);
	}

	boolean first = true;

	@Override
	protected void worldChanged(World world) {
		int dimensionBit;
		WorldGenerator worldGen = world.getGenerator();
		if (worldGen instanceof NetherGenerator) {
			dimensionBit = -1;
		} else if (worldGen instanceof TheEndGenerator) {
			dimensionBit = 1;
		} else {
			dimensionBit = 0;
		}
		if (first) {
			first = false;
			int entityId = owner.getEntity().getId();
			VanillaPlayer vc = (VanillaPlayer) owner.getEntity().getController();
			LoginRequestMessage idMsg = new LoginRequestMessage(entityId, owner.getName(), vc.isSurvival() ? 0 : 1, dimensionBit, 0, world.getHeight(), session.getGame().getMaxPlayers(), "DEFAULT");
			owner.getSession().send(idMsg, true);
			//Normal messages may be sent
			owner.getSession().setState(State.GAME);
			for (int slot = 0; slot < 4; slot++) {
				ItemStack slotItem = vc.getInventory().getArmor().getItem(slot);
				EntityEquipmentMessage EEMsg;
				if (slotItem == null) {
					EEMsg = new EntityEquipmentMessage(entityId, slot, -1, 0);
				} else {
					EEMsg = new EntityEquipmentMessage(entityId, slot, getMinecraftId(slotItem.getMaterial().getId()), slotItem.getData());
				}
				owner.getSession().send(EEMsg);
			}
		} else {
			VanillaPlayer vc = (VanillaPlayer) owner.getEntity().getController();
			owner.getSession().send(new RespawnMessage(dimensionBit, (byte) 0, (byte) (vc.isSurvival() ? 0 : 1), world.getHeight(), "DEFAULT"));
		}

		if (world != null) {
			Point spawn = world.getSpawnPoint().getPosition();
			SpawnPositionMessage SPMsg = new SpawnPositionMessage((int) spawn.getX(), (int) spawn.getY(), (int) spawn.getZ());
			owner.getSession().send(SPMsg);
		}
	}

	long lastKeepAlive = System.currentTimeMillis();

	@Override
	public void preSnapshot() {
		long currentTime = System.currentTimeMillis();
		if (currentTime > lastKeepAlive + TIMEOUT) {
			KeepAliveMessage PingMsg = new KeepAliveMessage((int) currentTime);
			lastKeepAlive = currentTime;
			owner.getSession().send(PingMsg, true);
		}

		for (TIntObjectIterator<Message> i = queuedInventoryUpdates.iterator(); i.hasNext(); ) {
			i.advance();
			session.send(i.value());
		}
		super.preSnapshot();
	}

	@Override
	public void updateBlock(Chunk chunk, int x, int y, int z, BlockMaterial material, short data) {
		int id = getMinecraftId(material);
		if ((data & 0xF) > 15) {
			data = 0;
		}
		x += chunk.getBlockX();
		y += chunk.getBlockY();
		z += chunk.getBlockZ();
		if (y >= 0 && y < chunk.getWorld().getHeight()) {
			BlockChangeMessage BCM = new BlockChangeMessage(x, y, z, id & 0xFF, data & 0xF);
			session.send(BCM);
		}
	}

	@Override
	public void spawnEntity(Entity e) {
		if (e == null) {
			return;
		}

		Controller c = e.getController();
		if (c != null) {
			EntityProtocol ep = c.getType().getEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID);
			if (ep != null) {
				Message[] spawn = ep.getSpawnMessage(e);
				if (spawn != null) {
					for (Message msg : spawn) {
						session.send(msg);
					}
				}
			}
		}
		super.spawnEntity(e);
	}

	@Override
	public void destroyEntity(Entity e) {
		if (e == null) {
			return;
		}

		Controller c = e.getController();
		if (c != null) {
			EntityProtocol ep = c.getType().getEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID);
			if (ep != null) {
				Message[] death = ep.getDestroyMessage(e);
				if (death != null) {
					for (Message msg : death) {
						session.send(msg);
					}
				}
			}
		}
		super.destroyEntity(e);
	}

	@Override
	public void syncEntity(Entity e) {
		if (e == null) {
			return;
		}

		Controller c = e.getController();
		if (c != null) {
			EntityProtocol ep = c.getType().getEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID);
			if (ep != null) {
				Message[] sync = ep.getUpdateMessage(e);
				if (sync != null) {
					for (Message msg : sync) {
						session.send(msg);
					}
				}
			}
		}
		super.syncEntity(e);
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		Controller c = owner.getEntity().getController();
		if (!(c instanceof VanillaPlayer)) {
			return;
		}
		VanillaPlayer controller = (VanillaPlayer) c;
		Window window = controller.getActiveWindow();

		slot = window.getSlotIndexMap().getMinecraftSlot(slot);
		if (slot == -1) {
			return;
		}

		Message message;
		if (item == null) {
			message = new SetWindowSlotMessage(window.getInstanceId(), slot);
		} else {
			message = new SetWindowSlotMessage(window.getInstanceId(), slot, getMinecraftId(item.getMaterial().getId()), item.getAmount(), item.getData(), item.getNBTData());
		}
		queuedInventoryUpdates.put(slot, message);
	}

	@Override
	public void updateAll(InventoryBase inventory, ItemStack[] slots) {
		Controller c = owner.getEntity().getController();
		if (!(c instanceof VanillaPlayer)) {
			return;
		}

		Window window = ((VanillaPlayer) c).getActiveWindow();

		session.send(new SetWindowSlotsMessage((byte) window.getInstanceId(), window.getSlotIndexMap().getMinecraftItems(slots)));
		queuedInventoryUpdates.clear();
	}

	/**
	 * This method takes any amount of messages and sends them to every online player on the server.
	 * @param messages
	 */
	public static void broadcastPacket(Message... messages) {
		sendPacket(Spout.getEngine().getOnlinePlayers(), messages);
	}

	/**
	 * This method takes in any amount of messages and sends them to any amount of
	 * players.
	 * @param players specific players to send a message to.
	 * @param messages the message(s) to send
	 */
	public static void sendPacket(Player[] players, Message... messages) {
		for (Player player : players) {
			for (Message message : messages) {
				sendPacket(player, message);
			}
		}
	}

	/**
	 * This method takes in a message and sends it to a specific player
	 * @param player specific player to relieve message
	 * @param messages specific message to send.
	 */
	public static void sendPacket(Player player, Message... messages) {
		for (Message message : messages) {
			player.getSession().send(message);
		}
	}

	/**
	 * This method sends an effect play message for a block to all nearby players in a 16-block radius
	 * @param block The block that the effect comes from.
	 * @param effect The effect to play
	 */
	public static void playBlockEffect(Block block, Entity ignore, PlayEffectMessage.Messages effect) {
		playBlockEffect(block, ignore, 16, effect, 0);
	}

	/**
	 * This method sends an effect play message for a block to all nearby players
	 * @param block The block that the effect comes from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param effect The effect to play
	 */
	public static void playBlockEffect(Block block, Entity ignore, int range, PlayEffectMessage.Messages effect) {
		playBlockEffect(block, ignore, range, effect, 0);
	}

	/**
	 * This method sends an effect play message for a block to all nearby players
	 * @param block The block that the effect comes from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param effect The effect to play
	 * @param data The data to use for the effect
	 */
	public static void playBlockEffect(Block block, Entity ignore, int range, PlayEffectMessage.Messages effect, int data) {
		sendPacketsToNearbyPlayers(block.getPosition(), ignore, range, new PlayEffectMessage(effect.getId(), block, data));
	}

	/**
	 * Sends a block action message to all nearby players in a 48-block radius
	 */
	public static void playBlockAction(Block block, byte arg1, byte arg2) {
		sendPacketsToNearbyPlayers(block.getPosition(), 48, new BlockActionMessage(block, arg1, arg2));
	}

	/**
	 * Sends a block action message to all nearby players
	 */
	public static void playBlockAction(Block block, int range, byte arg1, byte arg2) {
		sendPacketsToNearbyPlayers(block.getPosition(), range, new BlockActionMessage(block, arg1, arg2));
	}

	/**
	 * This method sends any amount of packets to all nearby players of a position (within a specified range).
	 * @param position The position that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearbyPlayers(Point position, Entity ignore, int range, Message... messages) {
		Set<Player> players = position.getWorld().getNearbyPlayers(position, ignore, range);
		for (Player plr : players) {
			plr.getSession().sendAll(messages);
		}
	}

	/**
	 * This method sends any amount of packets to all nearby players of a position (within a specified range).
	 * @param position The position that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearbyPlayers(Point position, int range, Message... messages) {
		Set<Player> players = position.getWorld().getNearbyPlayers(position, range);
		for (Player plr : players) {
			plr.getSession().sendAll(messages);
		}
	}

	/**
	 * This method sends any amount of packets to all nearby players of an entity (within a specified range).
	 * @param entity The entity that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearbyPlayers(Entity entity, int range, Message... messages) {
		if (entity == null || entity.getRegion() == null) {
			return;
		}
		Set<Player> players = entity.getWorld().getNearbyPlayers(entity, range);
		for (Player plr : players) {
			plr.getSession().sendAll(messages);
		}
	}

	/**
	 * This method sends any amount of packets and sends them to the nearest player from the entity specified.
	 * @param entity The entity that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearestPlayer(Entity entity, int range, Message... messages) {
		if (entity == null || entity.getRegion() == null) {
			return;
		}

		Player plr = entity.getWorld().getNearestPlayer(entity, range);
		//Only send if we have a player nearby.
		if (plr != null) {
			plr.getSession().sendAll(messages);
		}
	}
}
