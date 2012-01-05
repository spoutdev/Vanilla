package org.getspout.vanilla.block;

import org.getspout.vanilla.material.Plant;

public class LongGrass extends GenericBlockMaterial implements Plant {

	public LongGrass(String name, int id, int data) {
		super(name, id, data);
	}

	public int getNumGrowthStages() {
		return 0;
	}

	public int getMinimumLightToGrow() {
		return 0;
	}

	public boolean hasGrowthStages() {
		return false;
	}

}
