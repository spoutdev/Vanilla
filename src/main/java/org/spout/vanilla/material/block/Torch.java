package org.spout.vanilla.material.block;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.PointAttachable;
import org.spout.vanilla.material.block.attachable.WallAttachable;

public class Torch extends WallAttachable implements PointAttachable {
	public Torch(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		if (super.canAttachTo(material, face)) {
			if (face != BlockFace.TOP) {
				if (material == VanillaMaterials.GLASS) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
