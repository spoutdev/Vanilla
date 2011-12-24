package org.getspout.vanilla.entities.neutral;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.LandEC;
import org.getspout.vanilla.entities.NeutralEC;
import org.getspout.vanilla.mobs.MobID;
//Pigman might be added in the future
public class ZombiePigmanEC extends LandEC implements NeutralEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.ZombiePigman.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
