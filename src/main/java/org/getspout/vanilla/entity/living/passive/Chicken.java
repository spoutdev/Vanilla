package org.getspout.vanilla.entity.living.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.AnimalEntity;
import org.getspout.vanilla.entity.PassiveEntity;
import org.getspout.vanilla.mobs.MobID;

public class Chicken extends AnimalEntity implements PassiveEntity {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.Chicken.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
