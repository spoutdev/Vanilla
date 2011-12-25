package org.getspout.vanilla.entities.living.other;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.BossEC;
import org.getspout.vanilla.entities.living.FlyingEC;
import org.getspout.vanilla.mobs.MobID;

public class EnderdragonEC extends FlyingEC implements BossEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.EnderDragon.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
