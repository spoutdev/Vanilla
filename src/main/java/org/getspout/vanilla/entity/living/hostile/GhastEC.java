package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEC;
import org.getspout.vanilla.entity.living.FlyingEC;
import org.getspout.vanilla.mobs.MobID;

public class GhastEC extends FlyingEC implements HostileEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.Ghast.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
