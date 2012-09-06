package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.controlled.SignPost;

public class Sign extends BlockComponent {
	@Override
	public SignPost getMaterial() {
		return (SignPost) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof SignPost)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a SignPost.");
		}
		super.setMaterial(material);
	}
}
