package org.getspout.vanilla.entity.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.HostileEntity;
import org.getspout.vanilla.entity.living.Land;
import org.getspout.vanilla.entity.living.Living;
import org.getspout.vanilla.mobs.MobID;

public class Slime extends Living implements HostileEntity, Land {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.Slime.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
