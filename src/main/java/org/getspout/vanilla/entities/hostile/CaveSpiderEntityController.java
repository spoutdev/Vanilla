package org.getspout.vanilla.entities.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.HostileEntityController;
import org.getspout.vanilla.mobs.MobID;

public class CaveSpiderEntityController extends HostileEntityController {

	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.CaveSpider.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}
	
}
