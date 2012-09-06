package org.spout.vanilla.components.substance.material;

import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.controlled.EnderChestBlock;

public class EnderChest extends Chest {
	@Override
	public EnderChestBlock getMaterial() {
		return (EnderChestBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof EnderChestBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a EnderChestBlock.");
		}
		super.setMaterial(material);
	}
}
