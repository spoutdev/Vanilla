package org.getspout.vanilla.block;

import org.getspout.vanilla.material.Plant;

public class Sapling extends GenericBlockMaterial implements Plant {

	public Sapling(String name, int data) {
		super(name, 6, data);
	}

	public boolean hasGrowthStages() {
		return true;
	}

	public int getNumGrowthStages() {
		return 3;
	}

	public int getMinimumLightToGrow() {
		return 8;
	}
}
