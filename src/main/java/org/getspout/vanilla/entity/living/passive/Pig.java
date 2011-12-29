package org.getspout.vanilla.entity.living.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.AnimalEntity;
import org.getspout.vanilla.entity.PassiveEntity;
import org.getspout.vanilla.mobs.MobID;

public class Pig extends AnimalEntity implements PassiveEntity {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.Pig.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
