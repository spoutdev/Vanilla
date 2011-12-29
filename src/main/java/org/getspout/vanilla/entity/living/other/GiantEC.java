package org.getspout.vanilla.entity.living.other;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.living.MonsterEC;
import org.getspout.vanilla.mobs.MobID;

public class GiantEC extends MonsterEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.GiantZombie.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
