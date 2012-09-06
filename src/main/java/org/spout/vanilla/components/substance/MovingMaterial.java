package org.spout.vanilla.components.substance;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.protocol.entity.object.FallingBlockProtocol;

public class MovingMaterial extends BlockComponent {
	@Override
	public void onAttached() {
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new FallingBlockProtocol());
	}

	@Override
	public void setMaterial(BlockMaterial material) {
		if (!(material instanceof VanillaBlockMaterial)) {
			throw new IllegalArgumentException("Material passed in must be a VanillaBlockMaterial");
		}
		super.setMaterial(material);
	}
}
