/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import java.util.Set;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

import org.spout.api.Spout;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.generator.WorldGenerator;
import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.ChunkSnapshot;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Quaternion;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.protocol.Session.State;
import org.spout.api.protocol.event.ProtocolEventListener;
import org.spout.api.util.map.TIntPairHashSet;
import org.spout.api.util.map.TIntPairObjectHashMap;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
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
import org.spout.vanilla.util.VanillaMessageHandlerUtils;
import org.spout.vanilla.world.generator.VanillaBiome;
import org.spout.vanilla.world.generator.flat.FlatGenerator;
import org.spout.vanilla.world.generator.nether.NetherGenerator;
import org.spout.vanilla.world.generator.normal.NormalGenerator;

public class VanillaNetworkSynchronizer extends NetworkSynchronizer implements ProtocolEventListener {
	@SuppressWarnings("unused")
	private final static int POSITION_UPDATE_TICKS = 20;
	private final static double STANCE = 1.6D;
	private final static int TIMEOUT = 15000;

	public VanillaNetworkSynchronizer(Player player, Entity entity) {
		super(player, entity);
		registerProtocolEvents(this);
	}

	private TIntPairObjectHashMap<TIntHashSet> activeChunks = new TIntPairObjectHashMap<TIntHashSet>();
	private TIntPairHashSet biomesSentChunks = new TIntPairHashSet();
	//TODO: track entities as they come into range and untrack entities as they move out of range
	private TIntHashSet activeEntities = new TIntHashSet();
	private final TIntObjectHashMap<Message> queuedInventoryUpdates = new TIntObjectHashMap<Message>();

	@Override
	protected void freeChunk(Point p) {
		int x = (int) p.getX() >> Chunk.CHUNK_SIZE_BITS;
		int y = (int) p.getY() >> Chunk.CHUNK_SIZE_BITS;// + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.CHUNK_SIZE_BITS;

		if (y < 0 || y > p.getWorld().getHeight() >> 4) {
			return;
		}

		TIntHashSet column = activeChunks.get(x, z);
		if (column != null) {
			column.remove(y);
			if (column.isEmpty()) {
				biomesSentChunks.remove(x, z);
				activeChunks.remove(x, z);
				LoadChunkMessage unLoadChunk = new LoadChunkMessage(x, z, false);
				owner.getSession().send(unLoadChunk);
			}
		}
	}

	@Override
	protected void initChunk(Point p) {
		int x = (int) p.getX() >> Chunk.CHUNK_SIZE_BITS;
		int y = (int) p.getY() >> Chunk.CHUNK_SIZE_BITS;// + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.CHUNK_SIZE_BITS;

		if (y < 0 || y > p.getWorld().getHeight() >> 4) {
			return;
		}

		byte[] solidChunkData = new byte[16 * 16 * 16 * 5 / 2];
		byte[] airChunkData = new byte[16 * 16 * 16 * 5 / 2];

		int i = 0;
		for (int c = 0; c < 4096; c++) {
			solidChunkData[i] = 1;
			airChunkData[i++] = 0;
		}

		for (int c = 0; c < 2048; c++) {
			solidChunkData[i] = 0x00;
			airChunkData[i++] = 0x00;
		}

		for (int c = 0; c < 2048; c++) {
			solidChunkData[i] = 0x00;
			airChunkData[i++] = 0x00;
		}

		for (int c = 0; c < 2048; c++) {
			solidChunkData[i] = 0x00;
			airChunkData[i++] = (byte) 0xFF;
		}

		TIntHashSet column = activeChunks.get(x, z);
		if (column == null) {
			column = new TIntHashSet();
			activeChunks.put(x, z, column);
			LoadChunkMessage loadChunk = new LoadChunkMessage(x, z, true);
			owner.getSession().send(loadChunk);

			final boolean sendBiomes = !biomesSentChunks.contains(x, z);
			byte[] biomeData = sendBiomes ? new byte[Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE] : null;
			if (sendBiomes) {
				biomesSentChunks.add(x, z);
				WorldGenerator gen = p.getWorld().getGenerator();
				if (gen instanceof BiomeGenerator) {
					final long seed = p.getWorld().getSeed();
					for (int dx = x; dx < x + Chunk.CHUNK_SIZE; ++dx) {
						for (int dz = z; dz < z + Chunk.CHUNK_SIZE; ++dz) {
							Biome biome = ((BiomeGenerator) gen).getBiome(x, z, seed);
							if (biome instanceof VanillaBiome) {
								biomeData[(dz & (Chunk.CHUNK_SIZE - 1)) << 4 | (dx & (Chunk.CHUNK_SIZE - 1))] = (byte) ((VanillaBiome) biome).getBiomeId();
							}
						}
					}
				}
			}

			byte[][] packetChunkData = new byte[16][];

			for (i = 0; i < 16; i++) {
				if (i < 4) {
					packetChunkData[i] = solidChunkData;
				} else {
					packetChunkData[i] = airChunkData;
				}
			}

			CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, sendBiomes, new boolean[16], 0, packetChunkData, biomeData);
			owner.getSession().send(CCMsg);
		}
		column.add(y);
	}

	private final static byte[] dark = new byte[2048];
	private final static byte[] light = new byte[2048];

	static {
		for (int i = 0; i < light.length; i++) {
			light[i] = -1;
		}
	}

	@Override
	public void sendChunk(Chunk c) {
		int x = c.getX();
		int y = c.getY();// + SEALEVEL_CHUNK;
		int z = c.getZ();

		if (y < 0 || y >= c.getWorld().getHeight() >> 4) {
			return;
		}

		ChunkSnapshot snapshot = c.getSnapshot(false);
		short[] rawBlockIdArray = snapshot.getBlockIds();
		short[] rawBlockData = snapshot.getBlockData();
		// TODO do lighting right
		byte[] rawBlockLight = dark; // snapshot.getBlockLight();
		byte[] rawSkyLight = c.getY() < 4 ? dark : light; // snapshot.getSkyLight();
		byte[] fullChunkData = new byte[16 * 16 * 16 * 5 / 2];

		boolean hasData = false;
		int arrIndex = 0;
		for (int i = 0; i < rawBlockIdArray.length; i++) {
			// TODO - conversion code
			if ((rawBlockIdArray[i] & 0xFF) != 0) {
				hasData = true;
			}
			fullChunkData[arrIndex++] = (byte) (rawBlockIdArray[i] & 0xFF);
		}
		if (!hasData) {
			return;
		}

		for (int i = 0; i < rawBlockData.length; i += 2) {
			fullChunkData[arrIndex++] = (byte) ((byte) rawBlockData[i] << 4 | (byte) rawBlockData[i + 1] & 0xF);
		}

		System.arraycopy(rawBlockLight, 0, fullChunkData, arrIndex, rawBlockLight.length);
		arrIndex += rawBlockLight.length;

		System.arraycopy(rawSkyLight, 0, fullChunkData, arrIndex, rawSkyLight.length);
		arrIndex += rawSkyLight.length;

		final boolean sendBiomes = !biomesSentChunks.contains(x, z);
		byte[] biomeData = sendBiomes ? new byte[Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE] : null;
		if (sendBiomes) {
			biomesSentChunks.add(x, z);
			WorldGenerator gen = c.getWorld().getGenerator();
			if (gen instanceof BiomeGenerator) {
				final long seed = c.getWorld().getSeed();
				for (int dx = x; dx < x + Chunk.CHUNK_SIZE; ++dx) {
					for (int dz = z; dz < z + Chunk.CHUNK_SIZE; ++dz) {
						Biome biome = ((BiomeGenerator) gen).getBiome(x, z, seed);
						if (biome instanceof VanillaBiome) {
							biomeData[(dz & (Chunk.CHUNK_SIZE - 1)) << 4 | (dx & (Chunk.CHUNK_SIZE - 1))] = (byte) ((VanillaBiome) biome).getBiomeId();
						}
					}
				}
			}
		}

		byte[][] packetChunkData = new byte[16][];
		packetChunkData[y] = fullChunkData;
		CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, sendBiomes, new boolean[16], 0, packetChunkData, biomeData);
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
		if (worldGen instanceof NormalGenerator || worldGen instanceof FlatGenerator) {
			dimensionBit = 0;
		} else if (worldGen instanceof NetherGenerator) {
			dimensionBit = -1;
		} else {
			dimensionBit = 1;
		}
		if (first) {
			first = false;
			int entityId = owner.getEntity().getId();
			VanillaPlayer vc = (VanillaPlayer) owner.getEntity().getController();
			LoginRequestMessage idMsg = new LoginRequestMessage(entityId, owner.getName(), vc.isSurvival() ? 0 : 1, dimensionBit, 0, world.getHeight(), session.getGame().getMaxPlayers(), "DEFAULT");
			owner.getSession().send(idMsg, true);
			//Normal messages may be sent
			owner.getSession().setState(State.GAME);
			for (int slot = 0; slot < 5; slot++) {
				ItemStack slotItem = owner.getEntity().getInventory().getItem(5 + slot);
				EntityEquipmentMessage EEMsg;
				if (slotItem == null) {
					EEMsg = new EntityEquipmentMessage(entityId, slot, -1, 0);
				} else {
					EEMsg = new EntityEquipmentMessage(entityId, slot, slotItem.getMaterial().getId(), slotItem.getData());
				}
				owner.getSession().send(EEMsg);
			}
			entity.getInventory().addViewer(this);
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
		// TODO - proper translation
		int id = material.getId();
		if ((material.getId() & 0xFF) > 255) {
			id = 1;
		}
		if ((data & 0xF) > 15) {
			data = 0;
		}
		x = (chunk.getX() << Chunk.CHUNK_SIZE_BITS) + x;
		y = (chunk.getY() << Chunk.CHUNK_SIZE_BITS) + y;
		z = (chunk.getZ() << Chunk.CHUNK_SIZE_BITS) + z;
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
					activeEntities.add(e.getId());
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
		if (!activeEntities.contains(e.getId())) {
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
					activeEntities.remove(e.getId());
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

		// TODO - is this really worth checking?
		if (!activeEntities.contains(e.getId())) {
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
	public void onSlotSet(Inventory inventory, int slot) {
		Controller c = owner.getEntity().getController();
		if (!(c instanceof VanillaPlayer)) {
			return;
		}

		VanillaPlayer controller = (VanillaPlayer) c;
		ItemStack item = inventory.getItem(slot);
		Message message;
		final int networkSlot = VanillaMessageHandlerUtils.getNetworkInventorySlot(inventory, slot);
		int id = 0;
		if (!(inventory instanceof PlayerInventory)) {
			id = controller.getWindowId();
		}

		System.out.println("ID: " + id);

		if (item == null) {
			message = new SetWindowSlotMessage(id, networkSlot);
		} else {
			message = new SetWindowSlotMessage(id, networkSlot, item.getMaterial().getId(), item.getAmount(), item.getData(), item.getAuxData());
		}

		queuedInventoryUpdates.put(slot, message);
	}

	@Override
	public void onSlotSet(Inventory inventory, int slot, ItemStack item) {
		System.out.println("Setting slot: " + slot);
		Controller c = owner.getEntity().getController();
		if (!(c instanceof VanillaPlayer)) {
			return;
		}

		VanillaPlayer controller = (VanillaPlayer) c;
		Message message;
		final int networkSlot = VanillaMessageHandlerUtils.getNetworkInventorySlot(inventory, slot);
		int id = 0;
		if (!(inventory instanceof PlayerInventory)) {
			id = controller.getWindowId();
		}

		System.out.println("ID: " + id);

		if (item == null) {
			message = new SetWindowSlotMessage(id, networkSlot);
		} else {
			message = new SetWindowSlotMessage(id, networkSlot, item.getMaterial().getId(), item.getAmount(), item.getData(), item.getAuxData());
		}

		queuedInventoryUpdates.put(slot, message);
	}

	@Override
	public void updateAll(Inventory inventory, ItemStack[] slots) {
		Controller c = owner.getEntity().getController();
		if (!(c instanceof VanillaPlayer)) {
			return;
		}

		VanillaPlayer controller = (VanillaPlayer) c;
		byte id = 0;
		if (!(inventory instanceof PlayerInventory)) {
			id = (byte) controller.getWindowId();
		}

		ItemStack[] newSlots = new ItemStack[slots.length];
		for (int i = 0; i < slots.length; ++i) {
			newSlots[VanillaMessageHandlerUtils.getNetworkInventorySlot(inventory, i)] = slots[i];
		}

		session.send(new SetWindowSlotsMessage(id, newSlots));
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
	 * @param players  specific players to send a message to.
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
	 * @param player   specific player to relieve message
	 * @param messages specific message to send.
	 */
	public static void sendPacket(Player player, Message... messages) {
		for (Message message : messages) {
			player.getSession().send(message);
		}
	}

	/**
	 * This method sends an effect play message for a block to all nearby players in a 16-block radius
	 * @param block   The block that the effect comes from.
	 * @param effect	The effect to play
	 */
	public static void playBlockEffect(Block block, PlayEffectMessage.Messages effect) {
		playBlockEffect(block, 16, effect, 0);
	}

	/**
	 * This method sends an effect play message for a block to all nearby players
	 * @param block   The block that the effect comes from.
	 * @param range    The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param effect	The effect to play
	 */
	public static void playBlockEffect(Block block, int range, PlayEffectMessage.Messages effect) {
		playBlockEffect(block, range, effect, 0);
	}

	/**
	 * This method sends an effect play message for a block to all nearby players
	 * @param block   The block that the effect comes from.
	 * @param range    The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param effect	The effect to play
	 * @param data	The data to use for the effect
	 */
	public static void playBlockEffect(Block block, int range, PlayEffectMessage.Messages effect, int data) {
		sendPacketsToNearbyPlayers(block.getPosition(), range, new PlayEffectMessage(effect.getId(), block, data));
	}

	/**
	 * This method sends any amount of packets to all nearby players of a position (within a specified range).
	 * @param position   The position that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range    The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearbyPlayers(Point position, int range, Message... messages) {
		Region region = position.getWorld().getRegionFromBlock(position);
		Set<Player> players = region.getNearbyPlayers(position, range);
		for (Player plr : players) {
			plr.getSession().sendAll(messages);
		}
	}

	/**
	 * This method sends any amount of packets to all nearby players of an entity (within a specified range).
	 * @param entity   The entity that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range    The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearbyPlayers(Entity entity, int range, Message... messages) {
		if (entity == null || entity.getRegion() == null) {
			return;
		}
		Set<Player> players = entity.getRegion().getNearbyPlayers(entity, range);
		for (Player plr : players) {
			plr.getSession().sendAll(messages);
		}
	}

	/**
	 * This method sends any amount of packets and sends them to the nearest player from the entity specified.
	 * @param entity   The entity that the packet relates to. It will be used as the central point to send packets in a range from.
	 * @param range    The range (circular) from the entity in-which the nearest player should be searched for.
	 * @param messages The messages that should be sent to the discovered nearest player.
	 */
	public static void sendPacketsToNearestPlayer(Entity entity, int range, Message... messages) {
		if (entity == null || entity.getRegion() == null) {
			return;
		}

		Player plr = entity.getRegion().getNearestPlayer(entity, range);
		//Only send if we have a player nearby.
		if (plr != null) {
			plr.getSession().sendAll(messages);
		}
	}
}
