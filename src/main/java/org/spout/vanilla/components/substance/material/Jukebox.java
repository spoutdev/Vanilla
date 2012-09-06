package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.controlled.JukeboxBlock;

public class Jukebox extends BlockComponent {
	@Override
	public JukeboxBlock getMaterial() {
		return (JukeboxBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof JukeboxBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a JukeboxBlock.");
		}
		super.setMaterial(material);
	}
}
