/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.generator;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.biome.BiomeSelector;

/**
 * NoiseSelector that uses parameters to setup a conherent noise function which will
 * be used for biome selection. This is the first step of WGEN and doesn't partake in actual
 * biome construction.
 */
public class NoiseSelector extends BiomeSelector {
	private Perlin tempBase = new Perlin();
	private Perlin moistBase = new Perlin();

	public NoiseSelector() {
		tempBase.setNoiseQuality(NoiseQuality.BEST);
		tempBase.setOctaveCount(2);
		tempBase.setFrequency(0.005);
		tempBase.setPersistence(0.5);
		tempBase.setLacunarity(0.02);
		
		moistBase.setNoiseQuality(NoiseQuality.BEST);
		moistBase.setOctaveCount(2);
		moistBase.setFrequency(0.005);
		moistBase.setPersistence(0.5);
		moistBase.setLacunarity(0.02);		
	}
	
	public NoiseSelector(double vorFreq, double displacement, int roughness, double turFreq, double power) {
		this();
	}

	@Override
	public int pickBiome(int x, int y, int z, long seed) {
		tempBase.setSeed((int) seed*3);
		moistBase.setSeed((int) seed^2); //math is to differentiate the seeds
		
		int biome = 99;
		VanillaBiomeType biomes[] = {VanillaBiomes.DESERT, VanillaBiomes.JUNGLE, VanillaBiomes.MOUNTAIN, VanillaBiomes.MUSHROOM, VanillaBiomes.OCEAN, VanillaBiomes.PLAIN, VanillaBiomes.SWAMP, VanillaBiomes.TAIGA, VanillaBiomes.TUNDRA};;
		for(VanillaBiomeType b : biomes) {
			if(b.isValidPlacement(tempBase.GetValue(x, y, z)*100.0, moistBase.GetValue(x, y, z)*100.0)) {
				biome = b.getBiomeId();
			}
		}
		return biome;
	}
}
