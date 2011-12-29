package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEntity;
import org.getspout.vanilla.entity.living.Land;
import org.getspout.vanilla.mobs.MobID;

public class MagmaCube extends Slime {
	@Override
	public void onAttached() {
		super.onAttached(); //Without the metadata setting...
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.MagmaCube.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
