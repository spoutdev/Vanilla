/*
 * This file is part of Vanilla (http://www.getspout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.vanilla.entity.living.player;

import gnu.trove.set.hash.TIntHashSet;

import org.getspout.api.entity.PlayerController;
import org.getspout.api.geo.World;
import org.getspout.api.geo.cuboid.Chunk;
import org.getspout.api.geo.discrete.Point;
import org.getspout.api.geo.discrete.Transform;
import org.getspout.api.player.Player;
import org.getspout.api.util.cuboid.CuboidShortBuffer;
import org.getspout.api.util.map.TIntPairObjectHashMap;
import org.getspout.vanilla.VanillaPlugin;
import org.getspout.vanilla.protocol.msg.CompressedChunkMessage;
import org.getspout.vanilla.protocol.msg.EntityEquipmentMessage;
import org.getspout.vanilla.protocol.msg.IdentificationMessage;
import org.getspout.vanilla.protocol.msg.LoadChunkMessage;
import org.getspout.vanilla.protocol.msg.PingMessage;
import org.getspout.vanilla.protocol.msg.PositionRotationMessage;
import org.getspout.vanilla.protocol.msg.SpawnPositionMessage;

public abstract class MinecraftPlayer extends PlayerController {
	
	private final static double STANCE = 1.6D;
	private final static int TIMEOUT = 30000;
	
	//public final static float SEALEVEL = 64;
	//public final static int SEALEVEL_CHUNK = 4;
	
	public MinecraftPlayer(Player p){
		super(p);
	}
	
	@Override
	public void onAttached() {
		parent.setTransform(VanillaPlugin.spawnWorld.getSpawnPoint());
	}
	
	@Override
	public void onTick(float dt) {
		// TODO need to send timeout packets
	}
	
	private TIntPairObjectHashMap<TIntHashSet> activeChunks = new TIntPairObjectHashMap<TIntHashSet>();
	
	@Override
	protected void freeChunk(Point p) {
		int x = ((int)p.getX() >> Chunk.CHUNK_SIZE_BITS);
		int y = ((int)p.getY() >> Chunk.CHUNK_SIZE_BITS);// + SEALEVEL_CHUNK;
		int z = ((int)p.getZ() >> Chunk.CHUNK_SIZE_BITS);
		
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
		int x = ((int)p.getX() >> Chunk.CHUNK_SIZE_BITS);
		int y = ((int)p.getY() >> Chunk.CHUNK_SIZE_BITS);// + SEALEVEL_CHUNK;
		int z = ((int)p.getZ() >> Chunk.CHUNK_SIZE_BITS);
		
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
	protected void sendChunk(Chunk c) {
		int x = c.getX();
		int y = c.getY();// + SEALEVEL_CHUNK;
		int z = c.getZ();
		
		//System.out.println("Sending chunk (" + x + ", " + y + ", " + z + ") " + c);
		
		if (y < 0 || y > 7) {
			return;
		}
		
		CuboidShortBuffer buffer = c.getBlockCuboidBufferLive();
		short[] rawBlockIdArray = buffer.getRawArray();
		byte[] fullChunkData = new byte[5 * 16 * 16 * 16 / 2];
		for (int i = 0; i < fullChunkData.length; i++) {
			fullChunkData[i] = (byte)0xFF;
		}
		for (int i = 0; i < rawBlockIdArray.length; i++) {
			// TODO - conversion code
			fullChunkData[i] = (byte)(rawBlockIdArray[i] & 0xFF);
		}
		CompressedChunkMessage CCMsg = new CompressedChunkMessage(x * Chunk.CHUNK_SIZE, y * Chunk.CHUNK_SIZE, z * Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, fullChunkData);
		owner.getSession().send(CCMsg);
	}
	
	protected void sendPosition(Transform t) {
		Point p = t.getPosition();
		PositionRotationMessage PRMsg = new PositionRotationMessage(p.getX(), p.getY() + STANCE, p.getZ(), p.getY(), 0, 0, true);
		owner.getSession().send(PRMsg);
	}
	
	boolean first = true;
	protected void worldChanged(World world) {
		if (first) {
			first = false;
			int entityId = owner.getEntity().getId();
			IdentificationMessage idMsg = new IdentificationMessage(entityId, owner.getName(), world.getSeed(), 1, 0, 0, 128, 20);
			owner.getSession().send(idMsg);
			for (int slot = 0; slot < 5; slot++) {
				EntityEquipmentMessage EEMsg = new EntityEquipmentMessage(entityId, slot, -1, 0);
				owner.getSession().send(EEMsg);
			}
		}
		Point spawn = world.getSpawnPoint().getPosition();
		SpawnPositionMessage SPMsg = new SpawnPositionMessage((int)spawn.getX(), (int)(spawn.getY()), (int)spawn.getZ());
		owner.getSession().send(SPMsg);
	}

	long lastKeepAlive = System.currentTimeMillis();
	
	public void snapshotStart() {
		long currentTime = System.currentTimeMillis();
		if (currentTime > lastKeepAlive + TIMEOUT) {
			PingMessage PingMsg = new PingMessage((int)currentTime);
			lastKeepAlive = currentTime;
			owner.getSession().send(PingMsg);
		}
		super.snapshotStart();
	}
	
}