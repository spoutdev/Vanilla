package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.controlled.ChestBlock;

public class Chest extends BlockComponent {
	@Override
	public ChestBlock getMaterial() {
		return (ChestBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof ChestBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a ChestBlock.");
		}
		super.setMaterial(material);
	}
}
