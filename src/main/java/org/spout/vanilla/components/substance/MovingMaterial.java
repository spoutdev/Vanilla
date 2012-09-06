package org.spout.vanilla.components.substance;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.material.MaterialRegistry;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.protocol.entity.object.FallingBlockProtocol;

public class MovingMaterial extends EntityComponent {
	@Override
	public void onAttached() {
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new FallingBlockProtocol());
	}

	public VanillaBlockMaterial getMaterial() {
		return (VanillaBlockMaterial) MaterialRegistry.get(getData().get(VanillaData.MATERIAL_NAME));
	}

	public void setMaterial(VanillaBlockMaterial material) {
		getData().put(VanillaData.MATERIAL_NAME, material.getName());
	}
}
