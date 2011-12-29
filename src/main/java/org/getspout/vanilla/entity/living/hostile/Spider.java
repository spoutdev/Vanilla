package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEntity;
import org.getspout.vanilla.entity.living.Land;
import org.getspout.vanilla.entity.living.Monster;
import org.getspout.vanilla.mobs.MobID;

public class Spider extends Monster {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.Spider.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
