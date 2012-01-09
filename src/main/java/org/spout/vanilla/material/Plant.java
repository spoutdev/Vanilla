package org.spout.vanilla.material;

import org.spout.api.material.BlockMaterial;

public interface Plant extends BlockMaterial {

	public boolean hasGrowthStages();

	public int getNumGrowthStages();

	public int getMinimumLightToGrow();

}
