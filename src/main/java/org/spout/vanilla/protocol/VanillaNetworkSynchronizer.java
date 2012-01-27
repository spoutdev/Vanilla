package org.spout.vanilla.protocol;

import gnu.trove.set.hash.TIntHashSet;

import org.spout.api.Spout;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.api.util.map.TIntPairObjectHashMap;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
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

public class VanillaNetworkSynchronizer extends NetworkSynchronizer {
	
	private final static int POSITION_UPDATE_TICKS = 20;
	private final static double STANCE = 1.6D;
	private final static int TIMEOUT = 15000;
	private int positionUpdate = POSITION_UPDATE_TICKS;
	
	//public final static float SEALEVEL = 64;
	//public final static int SEALEVEL_CHUNK = 4;
	
	public VanillaNetworkSynchronizer(Player player) {
		this(player, null);
	}
	
	public VanillaNetworkSynchronizer(Player player, Entity entity) {
		super(player, entity);
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
	public void sendChunk(Chunk c) {
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
		if (world != null) {
			Point spawn = world.getSpawnPoint().getPosition();
			SpawnPositionMessage SPMsg = new SpawnPositionMessage((int)spawn.getX(), (int)(spawn.getY()), (int)spawn.getZ());
			owner.getSession().send(SPMsg);
		}
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
		super.preSnapshot();
	}
	
	@Override
	public void updateBlock(Chunk chunk, Block block) {
		BlockMaterial material = chunk.getBlockMaterial(block.getX(), block.getY(), block.getZ());
		short id = material.getId();
		short data = material.getData();
		// TODO - proper translation
		if ((id & 0xFF) > 255) {
			id = 1;
		}
		if ((data & 0xF) > 15) {
			data = 0;
		}
		int x = (chunk.getX() << Chunk.CHUNK_SIZE_BITS) + block.getX();
		int y = (chunk.getY() << Chunk.CHUNK_SIZE_BITS) + block.getY();
		int z = (chunk.getZ() << Chunk.CHUNK_SIZE_BITS) + block.getZ();
		if (y >= 0 && y < 128) {
			BlockChangeMessage BCM = new BlockChangeMessage(x, y, z, id & 0xFF, data & 0xF);
			session.send(BCM);
		}
	}

	public Message createUpdateMessage() {
		if (entity == null) {
			return null;
		}
		
		Controller controller = entity.getController();
		
		if (controller == null) {
			return null;
		}
		
		boolean teleport = controller.hasTeleported();
		boolean moved = controller.hasMoved();
		boolean rotated = controller.hasRotated();
		
		if (entity.getLiveTransform() == null) {
			return null;
		}
		
		if (--positionUpdate == 0) {
			positionUpdate = POSITION_UPDATE_TICKS;
			teleport = moved = true;
		}

		// TODO - is this rotation correct?
		int pitch = (int)(entity.getLiveTransform().getRotation().getAxisAngles().getZ() * 256.0F / 360.0F);
		int yaw = (int)(entity.getLiveTransform().getRotation().getAxisAngles().getY() * 256.0F / 360.0F);
		
		int id = this.entity.getId();
		Vector3 pos = entity.getLiveTransform().getPosition();
		Vector3 old = entity.getTransform().getPosition();
		int x = (int)(pos.getX() * 32.0);
		int y = (int)(pos.getY() * 32.0);
		int z = (int)(pos.getZ() * 32.0);
		
		int ox = (int)(old.getX() * 32.0);
		int oy = (int)(old.getY() * 32.0);
		int oz = (int)(old.getZ() * 32.0);
		
		int dx = (x - ox);
		int dy = (y - oy);
		int dz = (z - oz);
		
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
	
	@Override
	public void spawnEntity(Entity e){
		
	}
	
	@Override
	public void destroyEntity(Entity e) {
		
	}

}
