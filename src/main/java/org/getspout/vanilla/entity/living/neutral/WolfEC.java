package org.getspout.vanilla.entity.living.neutral;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.AnimalEC;
import org.getspout.vanilla.entity.NeutralEC;
import org.getspout.vanilla.mobs.MobID;

public class WolfEC extends AnimalEC implements NeutralEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.Wolf.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
