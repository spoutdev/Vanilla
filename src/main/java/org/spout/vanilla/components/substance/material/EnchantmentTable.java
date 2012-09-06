package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.controlled.EnchantmentTableBlock;

public class EnchantmentTable extends BlockComponent {
	@Override
	public EnchantmentTableBlock getMaterial() {
		return (EnchantmentTableBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof EnchantmentTableBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a EnchantmentTableBlock.");
		}
		super.setMaterial(material);
	}
}
