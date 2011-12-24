package org.getspout.vanilla.entities.passive;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.MinecraftEntityController;
import org.getspout.vanilla.mobs.MobID;

public class MooshroomEntityController extends MinecraftEntityController {

	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.Mooshroom.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}
	
}
