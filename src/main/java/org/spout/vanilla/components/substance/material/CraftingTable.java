package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.controlled.CraftingTableBlock;

public class CraftingTable extends BlockComponent {
	@Override
	public CraftingTableBlock getMaterial() {
		return (CraftingTableBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof CraftingTableBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a CraftingTableBlock.");
		}
		super.setMaterial(material);
	}
}
