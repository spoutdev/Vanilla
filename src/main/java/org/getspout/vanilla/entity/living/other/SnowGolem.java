package org.getspout.vanilla.entity.living.other;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.PassiveEntity;
import org.getspout.vanilla.entity.living.Creature;
import org.getspout.vanilla.mobs.MobID;

public class SnowGolem extends Creature implements PassiveEntity {

	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.SnowGolem.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}
	
}
