package org.getspout.vanilla.entity.living.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.AnimalEC;
import org.getspout.vanilla.entity.PassiveEC;
import org.getspout.vanilla.mobs.MobID;

public class ChickenEC extends AnimalEC implements PassiveEC {
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
