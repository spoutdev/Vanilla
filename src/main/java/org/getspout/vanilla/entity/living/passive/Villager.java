package org.getspout.vanilla.entity.living.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.PassiveEntity;
import org.getspout.vanilla.entity.living.Human;
import org.getspout.vanilla.mobs.MobID;

public class Villager extends Human implements PassiveEntity {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.Villager.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
