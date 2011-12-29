package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEntity;
import org.getspout.vanilla.entity.living.Flying;
import org.getspout.vanilla.mobs.MobID;

public class Ghast extends Flying implements HostileEntity {
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
