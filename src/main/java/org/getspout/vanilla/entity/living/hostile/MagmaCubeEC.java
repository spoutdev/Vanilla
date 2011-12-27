package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEC;
import org.getspout.vanilla.entity.living.LandEC;
import org.getspout.vanilla.mobs.MobID;

public class MagmaCubeEC extends SlimeEC {
	@Override
	public void onAttached() {
		super.onAttached(); //Without the metadata setting...
		parent.setMetadata("MobID", new MetadataStringValue(MobID.MagmaCube.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
