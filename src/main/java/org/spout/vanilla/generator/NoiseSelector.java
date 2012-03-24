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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.generator.biome.BiomeSelector;
import org.spout.api.generator.biome.BiomeType;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.generator.nether.NetherGenerator;
import org.spout.vanilla.generator.normal.NormalGenerator;
import org.spout.vanilla.generator.theend.TheEndGenerator;

/**
 * NoiseSelector that uses parameters to setup a conherent noise function which will
 * be used for biome selection. This is the first step of WGEN and doesn't partake in actual
 * biome construction.
 */
public class NoiseSelector extends BiomeSelector {
	private Perlin temp ;
	private Perlin humid;
	private BiomeGenerator generator;
	/**
	 * Constructor that will setup the noise generator to construct a world.
	 * @param vorFreq	  Number of cycles per unit length that a the base Voronoi function will output
	 * @param displacement
	 * @param roughness
	 * @param turFreq
	 * @param power
	 */
	
	public NoiseSelector(BiomeGenerator generator) {
		this.generator = generator;
		
		if(generator instanceof NormalGenerator) { //Überworld
			temp = new Perlin();
			humid = new Perlin();
			
			temp.setNoiseQuality(NoiseQuality.BEST);
			temp.setOctaveCount(3);
			temp.setFrequency(0.001);
			temp.setPersistence(0.012);
			temp.setLacunarity(0.5);
	
			humid.setNoiseQuality(NoiseQuality.BEST);
			humid.setOctaveCount(3);
			humid.setFrequency(0.001);
			humid.setPersistence(0.012);
			humid.setLacunarity(0.5);
		}
	}

	@Override
	public int pickBiome(int x, int y, int z, long seed) {
		int biomeIndex;
		VanillaBiomeType biome = null;
		if(generator instanceof NormalGenerator) {
			temp.setSeed((int) seed);
			humid.setSeed((int) seed);
			double t = (temp.GetValue(x, y, z) * 50.0) + 50.0;
			double h = (humid.GetValue(x, y, z) * 50.0) + 50.0;
			biome = VanillaBiomes.DESERT;
			for(VanillaBiomeType b : (VanillaBiomeType[]) generator.getBiomes().toArray()) {
				if(b.isValidPlacement(t, h)) {
					biome = b;
					break;
				}
			}
		} else if(generator instanceof NetherGenerator) {
			biome = VanillaBiomes.NETHERRACK;
		} else if(generator instanceof TheEndGenerator) {
			biome = VanillaBiomes.ENDSTONE;
		}
		biomeIndex = this.generator.indexOf(biome);
		return biomeIndex;
	}
}
