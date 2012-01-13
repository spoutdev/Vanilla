package org.spout.vanilla.material;

public interface Plant extends VanillaBlockMaterial {

	public boolean hasGrowthStages();

	public int getNumGrowthStages();

	public int getMinimumLightToGrow();

}
