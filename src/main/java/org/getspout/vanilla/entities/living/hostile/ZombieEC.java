package org.getspout.vanilla.entities.living.hostile;

import org.getspout.api.metadata.MetadataStringValue;
import org.getspout.vanilla.entities.HostileEC;
import org.getspout.vanilla.entities.living.LandEC;
import org.getspout.vanilla.entities.living.MonsterEC;
import org.getspout.vanilla.mobs.MobID;

public class ZombieEC extends MonsterEC implements HostileEC, LandEC {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setMetadata("MobID", new MetadataStringValue(MobID.Zombie.id));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

}
