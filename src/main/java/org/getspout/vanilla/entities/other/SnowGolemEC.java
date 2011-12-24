package org.getspout.vanilla.entities.other;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.MinecraftEC;
import org.getspout.vanilla.mobs.MobID;

public class SnowGolemEC extends MinecraftEC {

	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.SnowGolem.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}
	
}
