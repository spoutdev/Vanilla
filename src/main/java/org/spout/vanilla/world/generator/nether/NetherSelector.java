package org.spout.vanilla.world.generator.nether;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeSelector;
import org.spout.vanilla.world.generator.VanillaBiomes;

public class NetherSelector extends BiomeSelector {

	@Override
	public Biome pickBiome(int x, int y, int z, long seed) {
		//My name is commander shepard and this is my favorite biome in the nether
		return VanillaBiomes.NETHERRACK;
	}

}
