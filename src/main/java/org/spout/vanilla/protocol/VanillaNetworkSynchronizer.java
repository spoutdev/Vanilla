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

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;

import org.spout.api.entity.Entity;
import org.spout.api.entity.component.Controller;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.ChunkSnapshot;
import org.spout.api.geo.cuboid.ChunkSnapshot.EntityType;
import org.spout.api.geo.cuboid.ChunkSnapshot.ExtraData;
import org.spout.api.geo.cuboid.ChunkSnapshot.SnapshotType;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Quaternion;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.Session.State;
import org.spout.api.protocol.event.ProtocolEventListener;
import org.spout.api.util.map.concurrent.TSyncIntPairObjectHashMap;
import org.spout.api.util.set.concurrent.TSyncIntHashSet;
import org.spout.api.util.set.concurrent.TSyncIntPairHashSet;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.WorldType;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;
import org.spout.vanilla.protocol.msg.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.LoadChunkMessage;
import org.spout.vanilla.protocol.msg.LoginRequestMessage;
import org.spout.vanilla.protocol.msg.PlayerLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.RespawnMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;
import org.spout.vanilla.protocol.msg.window.WindowSetSlotMessage;
import org.spout.vanilla.protocol.msg.window.WindowSetSlotsMessage;
import org.spout.vanilla.window.Window;
import org.spout.vanilla.world.generator.VanillaBiome;

import static org.spout.vanilla.material.VanillaMaterials.getMinecraftId;

public class VanillaNetworkSynchronizer extends NetworkSynchronizer implements ProtocolEventListener {
	private static final int SOLID_BLOCK_ID = 1; // Initializer block ID
	private static final byte[] SOLID_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final byte[] AIR_CHUNK_DATA = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
	private static final double STANCE = 1.6D;
	@SuppressWarnings("unused")
	private static final int POSITION_UPDATE_TICKS = 20;
	private static final int TIMEOUT = 15000;
	private final TIntObjectHashMap<Message> queuedInventoryUpdates = new TIntObjectHashMap<Message>();
	private boolean first = true;
	private long lastKeepAlive = System.currentTimeMillis();
	private TSyncIntPairObjectHashMap<TSyncIntHashSet> initializedChunks = new TSyncIntPairObjectHashMap<TSyncIntHashSet>();
	private TSyncIntPairHashSet activeChunks = new TSyncIntPairHashSet();

	static {
		int i = 0;
		for (int c = 0; c < Chunk.BLOCKS.VOLUME; c++) { // blocks
			SOLID_CHUNK_DATA[i] = SOLID_BLOCK_ID;
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

	public VanillaNetworkSynchronizer(Player player, Entity entity) {
		super(player, player.getSession(), entity, 3);
		registerProtocolEvents(this);
	}

	private Object initChunkLock = new Object();

	@Override
	protected void freeChunk(Point p) {
		int x = (int) p.getX() >> Chunk.BLOCKS.BITS;
		int y = (int) p.getY() >> Chunk.BLOCKS.BITS; // + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.BLOCKS.BITS;

		if (y < 0 || y >= p.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return;
		}

		TIntSet column = initializedChunks.get(x, z);
		if (column != null) {
			column.remove(y);
			if (column.isEmpty()) {
				if (initializedChunks.remove(x, z) != null) {
					activeChunks.remove(x, z);
					LoadChunkMessage unLoadChunk = new LoadChunkMessage(x, z, false);
					owner.getSession().send(false, unLoadChunk);
				}
			}/* else {
				byte[][] data = new byte[16][];
				data[y] = AIR_CHUNK_DATA;
				CompressedChunkMessage CCMsg = new CompressedChunkMessage(x, z, false, new boolean[16], 0, data, null);
				session.send(CCMsg);
			}*/
		}
	}

	@Override
	protected void initChunk(Point p) {

		int x = p.getChunkX();
		int y = p.getChunkY();// + SEALEVEL_CHUNK;
		int z = p.getChunkZ();

		if (y < 0 || y >= p.getWorld().getHeight() >> Chunk.BLOCKS.BITS) {
			return;
		}

		TSyncIntHashSet column = initializedChunks.get(x, z);
		if (column == null) {
			column = new TSyncIntHashSet();
			synchronized (initChunkLock) {
				TSyncIntHashSet oldColumn = initializedChunks.putIfAbsent(x, z, column);
				if (oldColumn == null) {
					LoadChunkMessage LCMsg = new LoadChunkMessage(x, z, true);
					owner.getSession().send(false, LCMsg);
				} else {
					column = oldColumn;
				}
			}
		}
		column.add(y);
	}

	private static BlockMaterial[][] getColumnTopmostMaterials(Point p) {
		BlockMaterial[][] materials = new BlockMaterial[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];

		World w = p.getWorld();

		for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				materials[xx][zz] = w.getTopmostBlock(p.getBlockX() + xx, p.getBlockZ() + zz, true);
			}
		}
		return materials;
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

	private static byte[] getChunkHeightMap(int[][] heights, BlockMaterial[][] materials, int chunkY) {
		byte[] packetChunkData = new byte[Chunk.BLOCKS.HALF_VOLUME * 5];
		int baseY = chunkY << Chunk.BLOCKS.BITS;

		for (int xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				int dataOffset = xx | (zz << Chunk.BLOCKS.BITS);
				int threshold = heights[xx][zz] - baseY;
				if (chunkY == 0 && threshold < 0) {
					threshold = 0;
				}
				int yy;
				// Set blocks below height to the solid block
				for (yy = 0; yy < Chunk.BLOCKS.SIZE && yy <= threshold; yy++) {
					if (yy == threshold) {
						BlockMaterial bm = materials[xx][zz];
						if (bm == null) {
							bm = VanillaMaterials.STONE;
						}
						int converted = getMinecraftId(bm.getId());
						packetChunkData[dataOffset] = (byte) converted;
					} else {
						packetChunkData[dataOffset] = SOLID_BLOCK_ID;
					}
					dataOffset += Chunk.BLOCKS.AREA;
				}
				// Set sky light of blocks above height to 15
				// Use half of start offset and add the block id and data length (2 volumes)
				byte mask = (xx & 0x1) == 0 ? (byte) 0x0F : (byte) 0xF0;
				dataOffset = Chunk.BLOCKS.DOUBLE_VOLUME + (dataOffset >> 1);
				for (; yy < Chunk.BLOCKS.SIZE; yy++) {
					packetChunkData[dataOffset] |= mask;
					dataOffset += Chunk.BLOCKS.HALF_AREA;
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

		initChunk(c.getBase());

		if (activeChunks.add(x, z)) {
			Point p = c.getBase();
			int[][] heights = getColumnHeights(p);
			BlockMaterial[][] materials = getColumnTopmostMaterials(p);

			byte[][] packetChunkData = new byte[16][];

			for (int cube = 0; cube < 16; cube++) {
				packetChunkData[cube] = getChunkHeightMap(heights, materials, cube);
			}

			LoadChunkMessage loadChunk = new LoadChunkMessage(x, z, true);
			owner.getSession().send(false, loadChunk);

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
			owner.getSession().send(false, CCMsg);
		}

		ChunkSnapshot snapshot = c.getSnapshot(SnapshotType.BOTH, EntityType.NO_ENTITIES, ExtraData.NO_EXTRA_DATA);
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
		owner.getSession().send(false, CCMsg);
	}

	@Override
	protected void sendPosition(Point p, Quaternion rot) {
		//TODO: Implement Spout Protocol
		Session session = owner.getSession();
		if (p.distanceSquared(entity.getPosition()) >= 16) {
			EntityTeleportMessage ETMMsg = new EntityTeleportMessage(entity.getId(), (int) p.getX(), (int) p.getY(), (int) p.getZ(), (int) rot.getYaw(), (int) rot.getPitch());
			PlayerLookMessage PLMsg = new PlayerLookMessage(rot.getYaw(), rot.getPitch(), true);
			session.sendAll(false, ETMMsg, PLMsg);
		} else {
			PlayerPositionLookMessage PPLMsg = new PlayerPositionLookMessage(p.getX(), p.getY() + STANCE, p.getZ(), STANCE, rot.getYaw(), rot.getPitch(), true);
			session.send(false, PPLMsg);
		}
	}

	@Override
	protected void worldChanged(World world) {
		//Grab world characteristics.
		GameMode gamemode = world.getDataMap().get(VanillaData.GAMEMODE);
		Difficulty difficulty = world.getDataMap().get(VanillaData.DIFFICULTY);
		Dimension dimension = world.getDataMap().get(VanillaData.DIMENSION);
		WorldType worldType = world.getDataMap().get(VanillaData.WORLD_TYPE);

		//TODO Handle infinite height
		if (first) {
			first = false;
			int entityId = owner.getEntity().getId();
			VanillaPlayer vc = (VanillaPlayer) owner.getEntity().getController();
			LoginRequestMessage idMsg = new LoginRequestMessage(entityId, owner.getName(), gamemode.getId(), dimension.getId(), difficulty.getId(), 256, session.getEngine().getMaxPlayers(), worldType.toString());
			owner.getSession().send(false, true, idMsg);
			owner.getSession().setState(State.GAME);
			for (int slot = 0; slot < 4; slot++) {
				ItemStack slotItem = vc.getInventory().getArmor().getItem(slot);
				EntityEquipmentMessage EEMsg;
				if (slotItem == null) {
					EEMsg = new EntityEquipmentMessage(entityId, slot, -1, 0);
				} else {
					EEMsg = new EntityEquipmentMessage(entityId, slot, getMinecraftId(slotItem.getMaterial().getId()), slotItem.getData());
				}
				owner.getSession().send(false, EEMsg);
			}
		} else {
			owner.getSession().send(false, new RespawnMessage(dimension.getId(), difficulty.getId(), gamemode.getId(), 256, worldType.toString()));
		}

		Point pos = world.getSpawnPoint().getPosition();
		SpawnPositionMessage SPMsg = new SpawnPositionMessage((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
		owner.getSession().send(false, SPMsg);
	}

	@Override
	public void preSnapshot() {
		long currentTime = System.currentTimeMillis();
		if (currentTime > lastKeepAlive + TIMEOUT) {
			KeepAliveMessage PingMsg = new KeepAliveMessage((int) currentTime);
			lastKeepAlive = currentTime;
			owner.getSession().send(false, true, PingMsg);
		}

		for (TIntObjectIterator<Message> i = queuedInventoryUpdates.iterator(); i.hasNext(); ) {
			i.advance();
			session.send(false, i.value());
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
			session.send(false, BCM);
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
					session.sendAll(false, spawn);
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
					session.sendAll(false, death);
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
					session.sendAll(false, sync);
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

		Message message;
		if (item == null) {
			message = new WindowSetSlotMessage(window, slot);
		} else {
			message = new WindowSetSlotMessage(window, slot, getMinecraftId(item.getMaterial()), item.getAmount(), item.getData(), item.getNBTData());
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

		session.send(false, new WindowSetSlotsMessage(window, slots));
		queuedInventoryUpdates.clear();
	}
}
