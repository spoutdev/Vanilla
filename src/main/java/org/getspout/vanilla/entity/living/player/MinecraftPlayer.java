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
import org.getspout.api.geo.cuboid.Chunk;
import org.getspout.api.geo.discrete.Point;
import org.getspout.api.geo.discrete.Transform;
import org.getspout.api.player.Player;
import org.getspout.api.util.cuboid.CuboidShortBuffer;
import org.getspout.api.util.map.TIntPairObjectHashMap;
import org.getspout.vanilla.VanillaPlugin;
import org.getspout.vanilla.protocol.msg.CompressedChunkMessage;
import org.getspout.vanilla.protocol.msg.LoadChunkMessage;
import org.getspout.vanilla.protocol.msg.PositionRotationMessage;

public abstract class MinecraftPlayer extends PlayerController {
	
	private static double STANCE = 1.6D;
	
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
	protected void freeChunk(Chunk c) {
		int x = c.getX();
		int y = c.getY();
		int z = c.getZ();
		
		if (y < 0 || y > 15) {
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
	protected void sendChunk(Chunk c) {
		int x = c.getX();
		int y = c.getY();
		int z = c.getZ();
		
		if (y < 0 || y > 15) {
			return;
		}
		
		TIntHashSet column = activeChunks.get(x, z);
		
		if (column == null) {
			column = new TIntHashSet();
			activeChunks.put(x, z, column);
			LoadChunkMessage loadChunk = new LoadChunkMessage(c.getX(), c.getZ(), true);
			owner.getSession().send(loadChunk);
		}
		
		CuboidShortBuffer buffer = c.getBlockCuboidBufferLive();
		short[] rawBlockIdArray = buffer.getRawArray();
		byte[] fullChunkData = new byte[5 * 16 * 16 * 128 / 2];
		for (int i = 0; i < fullChunkData.length; i++) {
			fullChunkData[i] = (byte)0xFF;
		}
		for (int i = 0; i < rawBlockIdArray.length; i++) {
			// TODO - conversion code
			fullChunkData[i] = (byte)(rawBlockIdArray[i] & 0xFF);
		}
		CompressedChunkMessage CCMsg = new CompressedChunkMessage(x << Chunk.CHUNK_SIZE_BITS, y << Chunk.CHUNK_SIZE_BITS, z << Chunk.CHUNK_SIZE_BITS, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, fullChunkData);
		owner.getSession().send(CCMsg);
		column.add(y);
	}
	
	protected void sendPosition(Transform t) {
		Point p = t.getPosition();
		PositionRotationMessage PRMsg = new PositionRotationMessage(p.getX(), p.getY() + STANCE, p.getZ(), p.getY(), 0, 0, true);
		owner.getSession().send(PRMsg);
	}

	
}