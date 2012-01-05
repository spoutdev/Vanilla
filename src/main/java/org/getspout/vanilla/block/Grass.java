package org.getspout.vanilla.block;

import org.getspout.vanilla.material.Plant;

public class Grass extends GenericBlockMaterial implements Plant {

	public Grass(String name) {
		super(name, 2);
	}

	public boolean hasGrowthStages() {
		return false;
	}

	public int getNumGrowthStages() {
		return 0;
	}

	public int getMinimumLightToGrow() {
		return 9;
	}

}
