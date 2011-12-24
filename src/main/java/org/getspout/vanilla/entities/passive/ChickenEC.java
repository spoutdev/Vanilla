package org.getspout.vanilla.entities.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.LandEC;
import org.getspout.vanilla.entities.PassiveEC;
import org.getspout.vanilla.mobs.MobID;

public class ChickenEC extends LandEC implements PassiveEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.Chicken.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
