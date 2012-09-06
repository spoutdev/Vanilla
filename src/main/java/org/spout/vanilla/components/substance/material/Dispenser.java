package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.block.controlled.DispenserBlock;

public class Dispenser extends BlockComponent {
	@Override
	public DispenserBlock getMaterial() {
		return (DispenserBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof DispenserBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a DispenserBlock.");
		}
		super.setMaterial(material);
	}

	public boolean isPowered() {
		return getData().get(VanillaData.IS_POWERED);
	}

	public void setPowered(boolean powered) {
		getData().put(VanillaData.IS_POWERED, powered);
	}
}
