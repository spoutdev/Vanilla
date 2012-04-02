package org.spout.vanilla.material.block;

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.attachable.WallAttachable;

public class SignBase extends WallAttachable {

	public SignBase(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean onPlacement(World world, int x, int y, int z, short data, BlockFace against, Source source) {
		if (against == BlockFace.BOTTOM) {
			return world.setBlockMaterial(x, y, z, VanillaMaterials.SIGN_POST, data, true, source);
		} else if (against != BlockFace.TOP) {
			return world.setBlockMaterial(x, y, z, VanillaMaterials.WALL_SIGN, data, true, source);
		} else {
			return false;
		}
	}

	@Override
	public boolean canPlace(World world, int x, int y, int z, short data, BlockFace against, Source source) {
		if (super.canPlace(world, x, y, z, data, against, source)) {
			return true;
		} else if (world.getBlock(x, y, z).move(against.getOpposite()) instanceof SignBase) {
			return true;
		} else {
			return false;
		}
	}

}
