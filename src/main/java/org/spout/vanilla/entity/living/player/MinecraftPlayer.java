/*
 * This file is part of Vanilla (http://www.spout.org/).
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
package org.spout.vanilla.entity.living.player;

import gnu.trove.set.hash.TIntHashSet;

import org.spout.api.Spout;
import org.spout.api.entity.PlayerController;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;
import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.api.util.map.TIntPairObjectHashMap;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;
import org.spout.vanilla.protocol.msg.EntityEquipmentMessage;
import org.spout.vanilla.protocol.msg.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.IdentificationMessage;
import org.spout.vanilla.protocol.msg.LoadChunkMessage;
import org.spout.vanilla.protocol.msg.PingMessage;
import org.spout.vanilla.protocol.msg.PositionRotationMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionRotationMessage;
import org.spout.vanilla.protocol.msg.SpawnPositionMessage;

public abstract class MinecraftPlayer extends PlayerController {
	
	private final static int POSITION_UPDATE_TICKS = 20;
	private final static double STANCE = 1.6D;
	private final static int TIMEOUT = 15000;
	private int positionUpdate = POSITION_UPDATE_TICKS;
	
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
			IdentificationMessage idMsg = new IdentificationMessage(entityId, owner.getName(), world.getSeed(), 1, 0, 0, 128, 20, "DEFAULT");
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
	
	@Override
	public void preSnapshot() {
		long currentTime = System.currentTimeMillis();
		if (currentTime > lastKeepAlive + TIMEOUT) {
			PingMessage PingMsg = new PingMessage((int)currentTime);
			lastKeepAlive = currentTime;
			owner.getSession().send(PingMsg);
		}
		
		Message update = createUpdateMessage();
		World world = owner.getEntity().getWorld();
		if (update != null) {
			for (Player p : Spout.getGame().getOnlinePlayers()) {
				if (!p.equals(owner) && p.getEntity().getWorld().equals(world)) {
					p.getSession().send(update);
				}
			}
		}
		super.preSnapshot();
	}

	public Message createUpdateMessage() {
		boolean teleport = hasTeleported();
		boolean moved = hasMoved();
		boolean rotated = hasRotated();
		
		if (parent.getLiveTransform() == null) {
			return null;
		}
		
		if (--positionUpdate == 0) {
			positionUpdate = POSITION_UPDATE_TICKS;
			teleport = moved = true;
		}

		// TODO - is this rotation correct?
		int pitch = (int)(parent.getLiveTransform().getRotation().getAxisAngles().getZ() * 256.0F / 360.0F);
		int yaw = (int)(parent.getLiveTransform().getRotation().getAxisAngles().getY() * 256.0F / 360.0F);
		
		int id = this.parent.getId();
		Vector3 pos = Vector3.floor(parent.getLiveTransform().getPosition());
		Vector3 old = Vector3.floor(parent.getTransform().getPosition());
		int x = (int)pos.getX();
		int y = (int)pos.getY();
		int z = (int)pos.getZ();
		
		int dx = (int)(x - old.getX());
		int dy = (int)(y - old.getY());
		int dz = (int)(z - old.getZ());
		
		if (moved && teleport) {
			return new EntityTeleportMessage(id, x, y, z, yaw, pitch);
		}
		if (moved && rotated) {
			return new RelativeEntityPositionRotationMessage(id, dx, dy, dz, yaw, pitch);
		}
		if (moved) {
			return new RelativeEntityPositionMessage(id, dx, dy, dz);
		}
		if (rotated) {
			return new EntityRotationMessage(id, yaw, pitch);
		}

		return null;
	}
}