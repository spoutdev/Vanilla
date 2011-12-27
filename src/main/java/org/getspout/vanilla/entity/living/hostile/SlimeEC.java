package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEC;
import org.getspout.vanilla.entity.living.LandEC;
import org.getspout.vanilla.entity.living.LivingEC;
import org.getspout.vanilla.mobs.MobID;

public class SlimeEC extends LivingEC implements HostileEC, LandEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.Slime.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
