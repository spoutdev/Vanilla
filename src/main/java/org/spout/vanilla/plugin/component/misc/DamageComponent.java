package org.spout.vanilla.plugin.component.misc;

import java.util.HashMap;

import org.spout.api.component.type.EntityComponent;
import org.spout.vanilla.plugin.data.Damage;
import org.spout.vanilla.plugin.data.Difficulty;

public class DamageComponent extends EntityComponent {

	private HashMap<Difficulty, Damage> damageList = new HashMap<Difficulty, Damage>();

	public void onAttached() {
		for (Difficulty difficulty :Difficulty.values() ) {
			damageList.put(difficulty, new Damage());
		}
	}
	
	public Damage getDamageLevel(Difficulty difficulty) {
		return damageList.get(difficulty);
	}
}
