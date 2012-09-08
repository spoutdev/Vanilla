package org.spout.vanilla.components.substance;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.components.living.VanillaEntity;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.entity.object.XPOrbEntityProtocol;

public class XPOrb extends VanillaEntity {
	@Override
	public void onAttached() {
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new XPOrbEntityProtocol());
	}

	public short getExperience() {
		return getData().get(VanillaData.EXPERIENCE_AMOUNT);
	}

	public void setExperience(short experience) {
		getData().put(VanillaData.EXPERIENCE_AMOUNT, experience);
	}
}
