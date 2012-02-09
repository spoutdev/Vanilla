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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */

package org.spout.vanilla.generator;

import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGenerator;
import org.spout.api.geo.World;
import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.biome.BiomeMap;
import org.spout.vanilla.biome.BiomeSelector;
import org.spout.vanilla.biome.BiomeType;

import java.util.ArrayList;

public abstract class BiomeGenerator implements WorldGenerator {
	private BiomeMap biomes = new BiomeMap();

	private ArrayList<Populator> populators = new ArrayList<Populator>();

	public BiomeGenerator() {
		register(new BiomePopulator(biomes));
		registerBiomes();
	}

	/**
	 * Called during biome generatator's construction phase
	 */
	public abstract void registerBiomes();

	protected void setSelector(BiomeSelector selector) {
		biomes.setSelector(selector);
	}

	/**
	 * Register a new Biome Type to be generatated by this generator
	 * @param biome
	 */
	public void register(BiomeType biome) {
		biomes.addBiome(biome);
	}

	/**
	 * Register a new Populator to be used by this Generator
	 * @param populator
	 */
	public void register(Populator populator) {
		populators.add(populator);
	}

	public void generate(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ) {
		biomes.getBiome(chunkX, chunkZ).generateTerrain(blockData, chunkX, chunkY, chunkZ);
	}

	public final Populator[] getPopulators() {
		return populators.toArray(new Populator[populators.size()]);
	}
}
