package org.getspout.vanilla.entity.living.other;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entity.BossEntity;
import org.getspout.vanilla.entity.living.Flying;
import org.getspout.vanilla.mobs.MobID;

public class Enderdragon extends Flying implements BossEntity {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata(MobID.KEY, new MetadataStringValue(MobID.EnderDragon.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
