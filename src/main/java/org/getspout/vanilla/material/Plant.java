package org.getspout.vanilla.material;

import org.getspout.api.material.BlockMaterial;

public interface Plant extends BlockMaterial {

	public boolean hasGrowthStages();

	public int getNumGrowthStages();

	public int getMinimumLightToGrow();

}
