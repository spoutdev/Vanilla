package org.spout.vanilla.components.substance;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.component.components.EntityComponent;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.protocol.entity.BasicMobEntityProtocol;

/**
 * A component that identifies the entity as an EnderCrystal.
 */
public class EnderCrystal extends EntityComponent {
	@Override
	public void onAttached() {
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new BasicMobEntityProtocol(200));
	}
}
