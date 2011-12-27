package org.getspout.vanilla.entity.living.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.PassiveEC;
import org.getspout.vanilla.entity.living.WaterEC;
import org.getspout.vanilla.mobs.MobID;

public class SquidEC extends WaterEC implements PassiveEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.Squid.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
