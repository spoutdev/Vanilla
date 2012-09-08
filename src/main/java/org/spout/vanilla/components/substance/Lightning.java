package org.spout.vanilla.components.substance;

import org.spout.api.component.components.EntityComponent;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.protocol.entity.object.LightningEntityProtocol;

public class Lightning extends EntityComponent {
	@Override
	public void onAttached() {
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new LightningEntityProtocol());
	}
}
