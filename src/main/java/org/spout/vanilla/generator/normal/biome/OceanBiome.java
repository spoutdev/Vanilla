package org.spout.vanilla.generator.normal.biome;

import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.biome.BiomeType;

/**
 * Biome consisting of the wide wide ocean.
 */
public class OceanBiome extends BiomeType {
	/**
	 * Called during the Biome's construction.
	 * Registers all decorators to be called during the populate stage of world generation
	 */
	@Override
	public void registerDecorators() {
	}

	@Override
	public void generateTerrain(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ) {
	}
}
