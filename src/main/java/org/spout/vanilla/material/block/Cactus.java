package org.spout.vanilla.material.block;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.VanillaMaterials;

public class Cactus extends Solid {

	public Cactus(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(World world, int x, int y, int z) {
		boolean destroy = false;
		BlockMaterial below = world.getBlockMaterial(x, y-1, z);
		if(!below.equals(VanillaMaterials.SAND) && !below.equals(VanillaMaterials.CACTUS)) {
			destroy = true;
		}
		if(!destroy) {
			BlockFace faces[] = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
			for(BlockFace face:faces) {
				int tx = (int) (x + face.getOffset().getX());
				int tz = (int) (z + face.getOffset().getZ());
				BlockMaterial side = world.getBlockMaterial(tx, y, tz);
				if(!side.equals(VanillaMaterials.AIR)) {
					destroy = true;
					break;
				}
			}
		}
		if(destroy) {
			world.setBlockId(x, y, z, (short) 0, world);
			//TODO Drop item!
		}
	}
	
	
	
}
