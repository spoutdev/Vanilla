package org.spout.vanilla.components.living;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.vanilla.components.misc.HeadComponent;
import org.spout.vanilla.components.misc.HealthComponent;

public abstract class VanillaEntity extends EntityComponent {

	@Override
	public void onAttached() {
		Entity holder = getHolder();
		holder.put(new HeadComponent());
		holder.put(new HealthComponent());
	}

	public HeadComponent getHead() {
		return getHolder().getOrCreate(HeadComponent.class);
	}

	public HealthComponent getHealth() {
		return getHolder().getOrCreate(HealthComponent.class);
	}
}
