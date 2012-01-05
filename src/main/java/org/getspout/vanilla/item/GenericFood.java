package org.getspout.vanilla.item;

import org.getspout.vanilla.material.Food;

public class GenericFood extends GenericItemMaterial implements Food {
	private final int hunger;

	public GenericFood(String name, int id, int hunger) {
		super(name, id);
		this.hunger = hunger;
	}

	public int getHungerRestored() {
		return hunger;
	}

}
