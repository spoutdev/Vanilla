package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEC;
import org.getspout.vanilla.entity.living.LandEC;
import org.getspout.vanilla.mobs.MobID;

public class CaveSpiderEC extends SpiderEC {
	@Override
	public void onAttached() {
		super.onAttached();//Again, without the metadata.
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.CaveSpider.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
