package org.getspout.vanilla.entities.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.HostileEC;
import org.getspout.vanilla.entities.living.LandEC;
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
