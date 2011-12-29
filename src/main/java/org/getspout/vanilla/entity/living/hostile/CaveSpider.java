package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEntity;
import org.getspout.vanilla.entity.living.Land;
import org.getspout.vanilla.mobs.MobID;

public class CaveSpider extends Spider {
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
