package org.spout.vanilla.material.item;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Vector3;
import org.spout.vanilla.material.Block;
import org.spout.vanilla.material.attachable.GroundAttachable;
import org.spout.vanilla.material.block.RedstoneSourceBlock;
import org.spout.vanilla.material.block.RedstoneTargetBlock;
import org.spout.vanilla.material.block.Solid;

public class RedstoneWire extends GroundAttachable implements RedstoneSourceBlock, RedstoneTargetBlock {
	
	private final Vector3[] possibleIncoming = {
		new Vector3( 1, 0, 0),
		new Vector3( 0, 0, 1),
		new Vector3(-1, 0, 0),
		new Vector3( 0, 0,-1),
		new Vector3( 1, 1, 0),
		new Vector3( 0, 1, 1),
		new Vector3(-1, 1, 0),
		new Vector3( 0, 1,-1),
		new Vector3( 0, 1, 0), //Redstone torch from above
	};
	
	private final Vector3[] possibleOutgoing = {
		new Vector3( 1, 0, 0),
		new Vector3( 0, 0, 1),
		new Vector3(-1, 0, 0),
		new Vector3( 0, 0,-1),
		new Vector3( 1, 1, 0),
		new Vector3( 0, 1, 1),
		new Vector3(-1, 1, 0),
		new Vector3( 0, 1,-1),
		new Vector3( 1,-1, 0),
		new Vector3( 0,-1, 1),
		new Vector3(-1,-1, 0),
		new Vector3( 0,-1,-1),
	};
	
	private final Vector3[] possibleOutgoingTorch = {
			new Vector3( 2, 0, 0),
			new Vector3( 0, 0, 2),
			new Vector3(-2, 0, 0),
			new Vector3( 0, 0,-2),
			new Vector3( 1, 1, 0),
			new Vector3( 0, 1, 1),
			new Vector3(-1, 1, 0),
			new Vector3( 0, 1,-1),
	};

	public RedstoneWire(String name, int id, int data) {
		super(name, id, data);
	}

	@Override
	public boolean isOpaque() {
		return true;
	}
	
	@Override
	public boolean hasPhysics() {
		return true;
	}
	
	@Override
	public void onUpdate(World world, int x, int y, int z) {
		super.onUpdate(world, x, y, z);
		System.out.println("Updating "+ x + " " + y + " " + z);
		short maxPower = 0;
		BlockMaterial below = world.getBlockMaterial(x, y - 1, z);
		if(below instanceof Block) {
			maxPower = ((Block)below).getIndirectRedstonePower(world, x, y-1, z); //Check for indirect power from below
		}
		int tx, ty, tz;
		for(Vector3 vec : possibleIncoming) {
			tx = (int) (x + vec.getX());
			ty = (int) (y + vec.getY());
			tz = (int) (z + vec.getZ());
			BlockMaterial block = world.getBlockMaterial(tx, ty, tz);
			short power = 0;
			if(block instanceof RedstoneSourceBlock) {
				RedstoneSourceBlock source = (RedstoneSourceBlock) block;
				power = source.getRedstonePower(world, tx, ty, tz, x, y, z);
			} else if(block instanceof Block) {
				Block vanilla = (Block) block;
				power = vanilla.getDirectRedstonePower(world, tx, ty, tz);
			}
			maxPower = (short) Math.max(maxPower, power);
		}
		setPowerAndUpdate(world, x, y, z, maxPower);
	}
	
	
	
	/**
	 * Sets the wire at x,y,z to the given power and initiates an update process that will recalculate the wire.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param power
	 */
	public void setPowerAndUpdate(World world, int x, int y, int z, short power) {
		short current = world.getBlockData(x, y, z);
		if(current != power) {
			System.out.println("Old: "+current + " new: "+power);
			world.setBlockIdAndData(x, y, z, getId(), power, false, world);
			//Trace signal
			for(int j = 0; j < 3; j++) {
				int ty = y; 
				switch(j) {
				case 0:
					ty--;
					break;
				case 1: //ty = ty;
					break;
				case 2:
					ty++;
					break;
				}
				for(int i = 0; i < 4; i++) {
					int tx = x; 
					int tz = z;
					switch(i) {
					case 0:
						tx--;
						break;
					case 1:
						tx++;
						break;
					case 2:
						tz--;
						break;
					case 3:
						tz++;
						break;
					}
					BlockMaterial block = world.getBlockMaterial(tx, ty, tz);
					if(block instanceof RedstoneWire) {
						if(providesPowerTo(world, x, y, z, tx, ty, tz)) {
							onUpdate(world, tx, ty, tz);
						}
					}
				}
			}
			
			//Update redstone torches.
			for(Vector3 torch:possibleOutgoingTorch) {
				int dx = (int) torch.getX(), dy = (int) torch.getY(), dz = (int) torch.getZ();
				int tx = x + dx, ty = y + dy, tz = z + dz;
				//TODO maybe first check if they are attached to the block in between...
				world.getChunkFromBlock(tx, ty, tz).updatePhysics(tx, ty, tz);
			}
		}
	}
	
	@Override
	public boolean providesPowerTo(World world, int x, int y, int z, int tx, int ty, int tz) {
		return attachesTo(world, x, y, z, tx, ty, tz);
	}

	@Override
	public short getRedstonePower(World world, int x, int y, int z, int tx, int ty, int tz) {
		short power = 0;
		if(providesPowerTo(world, x, y, z, tx, ty, tz)) {
			power = (short) (Math.max(0, world.getBlockData(x, y, z) - 1));
		}
		if(x == tx && y == ty && z == tz) {
			power = world.getBlockData(x, y, z);
		}
		return power;
	}
	
	public boolean attachesTo(World world, int x, int y, int z, int tx, int ty, int tz) {
		BlockMaterial target = world.getBlockMaterial(tx, ty, tz);
		if(target instanceof RedstoneTargetBlock) {
			return ((RedstoneTargetBlock) target).providesAttachPoint(world, tx, ty, tz, x, y, z);
		}
		return false;
	}

	@Override
	public boolean providesAttachPoint(World world, int x, int y, int z, int tx, int ty, int tz) {
		if(y == ty) { //source and target are level
			if(x == tx) {
				return z - 1 == tz || z + 1 == tz;
			}
			if(z == tz) {
				return x - 1 == tx || x + 1 == tx;
			}
		} else {
			BlockMaterial target = world.getBlockMaterial(tx, ty, tz);
			if(target instanceof RedstoneWire) { //only send power down/up for other redstone wires
				if(y < ty) { //This is the below block
					if(world.getBlockMaterial(x, y+1, z) instanceof Solid) { //Current does not walk through solids
						return false;
					}
					if(x == tx) {
						return z - 1 == tz || z + 1 == tz;
					}
					if(z == tz) {
						return x - 1 == tx || x + 1 == tx;
					}
				} else { //This is the upper block
					if(world.getBlockMaterial(tx, ty+1, tz) instanceof Solid) { //Current does not walk through solids
						return false;
					}
					if(x == tx) {
						return z - 1 == tz || z + 1 == tz;
					}
					if(z == tz) {
						return x - 1 == tx || x + 1 == tx;
					}
				}
			} else {
				return false;
			}
		}
		return false;
	}
}
