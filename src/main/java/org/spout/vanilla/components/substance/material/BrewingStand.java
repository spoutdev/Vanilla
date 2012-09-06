package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.controlled.BrewingStandBlock;

public class BrewingStand extends BlockComponent {
	@Override
	public BrewingStandBlock getMaterial() {
		return (BrewingStandBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof BrewingStandBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a BrewingStandBlock.");
		}
		super.setMaterial(material);
	}
}
