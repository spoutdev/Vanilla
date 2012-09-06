package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.piston.PistonExtension;

public class Piston extends BlockComponent {
	@Override
	public PistonExtension getMaterial() {
		return (PistonExtension) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof PistonExtension)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a PistonExtension.");
		}
		super.setMaterial(material);
	}
}
