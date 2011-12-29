package org.getspout.vanilla.entity.living.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.PassiveEC;
import org.getspout.vanilla.mobs.MobID;

public class MooshroomEC extends CowEC implements PassiveEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.Mooshroom.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
