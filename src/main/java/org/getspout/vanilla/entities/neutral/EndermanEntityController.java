package org.getspout.vanilla.entities.neutral;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.MinecraftEntityController;
import org.getspout.vanilla.mobs.MobID;

public class EndermanEntityController extends MinecraftEntityController {

	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.Enderman.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}
	
}
