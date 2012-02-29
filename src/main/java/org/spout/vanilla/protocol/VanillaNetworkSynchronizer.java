/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol;

import gnu.trove.set.hash.TIntHashSet;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.ChunkSnapshot;
import org.spout.api.geo.discrete.Point;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.util.map.TIntPairObjectHashMap;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;
import org.spout.vanilla.protocol.msg.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.IdentificationMessage;
import org.spout.vanilla.protocol.msg.LoadChunkMessage;
import org.spout.vanilla.protocol.msg.PingMessage;
import org.spout.vanilla.protocol.msg.PositionRotationMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;

public class VanillaNetworkSynchronizer extends NetworkSynchronizer {
	@SuppressWarnings("unused")
	private final static int POSITION_UPDATE_TICKS = 20;
	private final static double STANCE = 1.6D;
	private final static int TIMEOUT = 15000;

	public VanillaNetworkSynchronizer(Player player) {
		this(player, null);
	}

	public VanillaNetworkSynchronizer(Player player, Entity entity) {
		super(player, entity);
	}

	private TIntPairObjectHashMap<TIntHashSet> activeChunks = new TIntPairObjectHashMap<TIntHashSet>();

	//TODO: track entities as they come into range and untrack entities as they move out of range
	private TIntHashSet activeEntities = new TIntHashSet();

	@Override
	protected void freeChunk(Point p) {
		int x = (int) p.getX() >> Chunk.CHUNK_SIZE_BITS;
		int y = (int) p.getY() >> Chunk.CHUNK_SIZE_BITS;// + SEALEVEL_CHUNK;
		int z = (int) p.getZ() >> Chunk.CHUNK_SIZE_BITS;

		if (y < 0 || y > 7) {
			return;
		}

		TIntHashSet column = activeChunks.get(x, z);
		if (column != null) {
			column.remove(y);
			if (column.isEmpty()) {
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

		if (y < 0 || y > 7) {
			return;
		}

		TIntHashSet column = activeChunks.get(x, z);
		if (column == null) {
			column = new TIntHashSet();
			activeChunks.put(x, z, column);
			LoadChunkMessage loadChunk = new LoadChunkMessage(x, z, true);
			owner.getSession().send(loadChunk);
		}
		column.add(y);
	}

	@Override
	public void sendChunk(Chunk c) {
		int x = c.getX();
		int y = c.getY();// + SEALEVEL_CHUNK;
		int z = c.getZ();

		//System.out.println("Sending chunk (" + x + ", " + y + ", " + z + ") " + c);

		if (y < 0 || y > 7) {
			return;
		}

		ChunkSnapshot snapshot = c.getSnapshot(false);
		short[] rawBlockIdArray = snapshot.getBlockIds();
		short[] rawBlockData = snapshot.getBlockData();
		byte[] fullChunkData = new byte[5 * 16 * 16 * 16 / 2];
		final int maxIdIndex = 16 * 16 * 16;
		final int maxDataIndex = maxIdIndex + 16 * 16 * 16 / 2;
		for (int i = 0; i < fullChunkData.length; i++) {
			if (i < maxIdIndex) {
				fullChunkData[i] = (byte) 0x00;
			} else if (i < maxDataIndex) {
				fullChunkData[i] = (byte) 0x00;
			} else {
				fullChunkData[i] = (byte) 0xFF;
			}
		}
		for (int i = 0; i < rawBlockIdArray.length; i++) {
			// TODO - conversion code
			fullChunkData[i] = (byte) (rawBlockIdArray[i] & 0xFF);
		}
		for (int i = 0; i < rawBlockData.length; i += 2) {
			fullChunkData[i + maxIdIndex] = (byte) (rawBlockData[i + 1] & 0xF << 4 | rawBlockData[i] & 0xF);
		}
		CompressedChunkMessage CCMsg = new CompressedChunkMessage(x * Chunk.CHUNK_SIZE, y * Chunk.CHUNK_SIZE, z * Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, fullChunkData);
		owner.getSession().send(CCMsg);
	}

	@Override
	protected void sendPosition(Point p, float yaw, float pitch) {
		//TODO: Implement Spout Protocol
		PositionRotationMessage PRMsg = new PositionRotationMessage(p.getX(), p.getY() + STANCE, p.getZ(), p.getY(), yaw, pitch, true);
		owner.getSession().send(PRMsg);
	}

	boolean first = true;

	@Override
	protected void worldChanged(World world) {
		if (first) {
			first = false;
			int entityId = owner.getEntity().getId();
			IdentificationMessage idMsg = new IdentificationMessage(entityId, owner.getName(), world.getSeed(), owner.getEntity().is(SurvivalPlayer.class) ? 0 : 1, 0, 0, 128, 20, "DEFAULT");
			owner.getSession().send(idMsg);
			for (int slot = 0; slot < 5; slot++) {
				EntityEquipmentMessage EEMsg = new EntityEquipmentMessage(entityId, slot, -1, 0);
				owner.getSession().send(EEMsg);
			}
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
			PingMessage PingMsg = new PingMessage((int) currentTime);
			lastKeepAlive = currentTime;
			owner.getSession().send(PingMsg);
		}
		super.preSnapshot();
	}

	@Override
	public void updateBlock(Chunk chunk, int x, int y, int z, short id, short data) {
		// TODO - proper translation
		if ((id & 0xFF) > 255) {
			id = 1;
		}
		if ((data & 0xF) > 15) {
			data = 0;
		}
		x = (chunk.getX() << Chunk.CHUNK_SIZE_BITS) + x;
		y = (chunk.getY() << Chunk.CHUNK_SIZE_BITS) + y;
		z = (chunk.getZ() << Chunk.CHUNK_SIZE_BITS) + z;
		if (y >= 0 && y < 128) {
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
			EntityProtocol ep = c.getEntityProtocol(VanillaPlugin.vanillaProtocolId);
			if (ep != null) {
				Message spawn = ep.getSpawnMessage(e);
				if (spawn != null) {
					activeEntities.add(e.getId());
					session.send(spawn);
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
			EntityProtocol ep = c.getEntityProtocol(VanillaPlugin.vanillaProtocolId);
			if (ep != null) {
				Message death = ep.getDestroyMessage(e);
				if (death != null) {
					session.send(death);
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
			EntityProtocol ep = c.getEntityProtocol(VanillaPlugin.vanillaProtocolId);
			if (ep != null) {
				Message sync = ep.getUpdateMessage(e);
				if (sync != null) {
					session.send(sync);
				}
			}
		}
		super.syncEntity(e);
	}
}
