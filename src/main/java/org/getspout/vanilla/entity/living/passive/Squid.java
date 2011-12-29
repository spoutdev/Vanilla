package org.getspout.vanilla.entity.living.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.PassiveEntity;
import org.getspout.vanilla.entity.living.Water;
import org.getspout.vanilla.mobs.MobID;

public class Squid extends Water implements PassiveEntity {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.Squid.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
