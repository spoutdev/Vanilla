package org.spout.vanilla.block;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.GenericBlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.VanillaBlocks;

public abstract class AbstractAttachable extends GenericBlockMaterial implements Attachable {

	protected AbstractAttachable(String name, int id) {
		super(name, id);
	}
	
	protected AbstractAttachable(String name, int id, int data) {
		super(name, id, data);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}
	
	@Override
	public void onUpdate(World world, int x, int y, int z) {
		if(getBlockAttachedTo(world, x, y, z).getBlockMaterial().equals(VanillaBlocks.AIR)) {
			world.setBlockMaterial(x, y, z, VanillaBlocks.AIR, world);
		}
	}
	
	@Override
	public Block getBlockAttachedTo(World world, int x, int y, int z) {
		BlockFace base = getFaceAttachedTo(world.getBlockData(x, y, z));
		Vector3 offset = base.getOffset();
		return world.getBlock((int) (x + offset.getX()), (int) (y + offset.getY()), (int) (z + offset.getZ()));
	}
	
}
