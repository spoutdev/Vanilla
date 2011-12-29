package org.getspout.vanilla.entity.living.other;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.BossEC;
import org.getspout.vanilla.entity.living.FlyingEC;
import org.getspout.vanilla.mobs.MobID;

public class EnderdragonEC extends FlyingEC implements BossEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.EnderDragon.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
