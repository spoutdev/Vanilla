package org.spout.vanilla.components.gamemode;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Entity;

public class SurvivalComponent extends EntityComponent {
	@Override
	public void onAttached() {
		Entity holder = getHolder();
		holder.remove(AdventureComponent.class);
		holder.remove(CreativeComponent.class);
	}
}
