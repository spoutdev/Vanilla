package org.spout.vanilla.block;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.VanillaBlocks;
import org.spout.vanilla.material.VanillaBlockMaterial;

public class RedstoneTorch extends TorchAttachable implements RedstoneSourceBlock, RedstoneTargetBlock{

	public static final short REDSTONE_POWER = 15;
	private static final Vector3 possibleOutgoing[] = {
		new Vector3( 1, 0, 0),
		new Vector3(-1, 0, 0),
		new Vector3( 0, 0, 1),
		new Vector3( 0, 0,-1),
		new Vector3( 0,-1, 0),
		new Vector3( 0, 2, 0),
	};
	private boolean powered;
	
	public RedstoneTorch(String name, int id, boolean powered) {
		super(name, id);
		this.powered = powered;
	}

	@Override
	public boolean providesAttachPoint(World world, int x, int y, int z, int tx, int ty, int tz) {
		return ty == y;
	}

	@Override
	public short getRedstonePower(World world, int x, int y, int z, int tx, int ty, int tz) {
		if(providesPowerTo(world, x, y, z, tx, ty, tz)) {
			return (short) (powered ? 16 : 0);
		}
		return 0;
	}

	@Override
	public boolean providesPowerTo(World world, int x, int y, int z, int tx, int ty, int tz) {
		if(y == ty) {
			return true;
		}
		if(tx == x && tz == z) {
			return tx - x == -2 || tx - x == 1;
		}
		return false;
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(World world, int x, int y, int z) {
		super.onUpdate(world, x, y, z);
		BlockFace face = getFaceAttachedTo(world.getBlockData(x, y, z));
		Vector3 offset = face.getOffset();
		int tx = (int) (x + offset.getX()), ty = (int) (y + offset.getY()), tz = (int) (z + offset.getZ());
		BlockMaterial mat = world.getBlockMaterial(tx, ty, tz);
		if(mat instanceof VanillaBlockMaterial) {
			short updateId = getId();
			VanillaBlockMaterial va = (VanillaBlockMaterial)mat;
			if(va.getIndirectRedstonePower(world, tx, ty, tz) > 0) {
				//Power off.
				updateId = VanillaBlocks.REDSTONE_TORCH_OFF.getId();
			} else {
				//Seems to be no incoming power, turn on!
				updateId = VanillaBlocks.REDSTONE_TORCH_ON.getId();
			}
			if(updateId != getId()) {
				short data = world.getBlockData(x, y, z);
				world.setBlockIdAndData(x, y, z, updateId, data, false, world);
				
				//Update other redstone inputs
				for(Vector3 offset2:possibleOutgoing) {
					tx = (int) (x + offset.getX());
					ty = (int) (y + offset.getY());
					tz = (int) (z + offset.getZ());
					mat = world.getBlockMaterial(tx, ty, tz);
					if(mat instanceof RedstoneTargetBlock) {
						world.updatePhysics(tx, ty, tz);
						//mat.updatePhysics(world, tx, ty, tz);
					}
				}
			}
		}
	}
}
