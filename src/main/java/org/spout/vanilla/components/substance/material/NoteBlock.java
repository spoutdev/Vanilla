package org.spout.vanilla.components.substance.material;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.block.controlled.NoteBlockBlock;

public class NoteBlock extends BlockComponent {
	@Override
	public NoteBlockBlock getMaterial() {
		return (NoteBlockBlock) super.getMaterial();
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof NoteBlockBlock)) {
			throw new IllegalArgumentException("Material passed in must be an instance of a NoteBlockBlock.");
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
