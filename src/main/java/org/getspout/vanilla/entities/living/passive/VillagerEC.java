package org.getspout.vanilla.entities.living.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.PassiveEC;
import org.getspout.vanilla.entities.living.HumanEC;
import org.getspout.vanilla.mobs.MobID;

public class VillagerEC extends HumanEC implements PassiveEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.Villager.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
