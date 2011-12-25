package org.getspout.vanilla.entities.living.other;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.PassiveEC;
import org.getspout.vanilla.entities.living.CreatureEC;
import org.getspout.vanilla.mobs.MobID;

public class SnowGolemEC extends CreatureEC implements PassiveEC {

	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.SnowGolem.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}
	
}
