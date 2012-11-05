/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.world.generator.normal.biome.selector;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeSelector;

import org.spout.vanilla.world.generator.biome.selector.BiomeSelectorElement;
import org.spout.vanilla.world.generator.biome.selector.BiomeSelectorLayer;

public class NormalBiomeSelector extends BiomeSelector {
	private static final float DEFAULT_SCALE = 2.5f;
	public static final PrimaryLayer PRIMARY = new PrimaryLayer(DEFAULT_SCALE);
	public static final MushroomLayer MUSHROOM = new MushroomLayer(DEFAULT_SCALE);
	public static final ShoreLayer SHORE = new ShoreLayer();
	public static final LandLayer LAND = new LandLayer(DEFAULT_SCALE);
	public static final SmallMountainsLayer SMALL_MOUNTAINS = new SmallMountainsLayer();
	public static final MountainsLayer MOUNTAINS = new MountainsLayer();
	public static final RiverLayer RIVER = new RiverLayer();
	public static final FrozenRiverLayer FROZEN_RIVER = new FrozenRiverLayer();
	public static final FrozenOceanLayer FROZEN_OCEAN = new FrozenOceanLayer(DEFAULT_SCALE);
	public static final SwampLayer SWAMP = new SwampLayer();
	public static final PlainsLayer PLAINS = new PlainsLayer();
	public static final DesertLayer DESERT = new DesertLayer(DEFAULT_SCALE);
	public static final ForestLayer FOREST = new ForestLayer(DEFAULT_SCALE);
	public static final JungleLayer JUNGLE = new JungleLayer(DEFAULT_SCALE);
	public static final TaigaLayer TAIGA = new TaigaLayer(DEFAULT_SCALE);
	public static final TundraLayer TUNDRA = new TundraLayer(DEFAULT_SCALE);

	static {
		LAND.addLandElements(SWAMP, PLAINS, DESERT, FOREST, JUNGLE, TAIGA, TUNDRA);
	}
	
	@Override
	public Biome pickBiome(int x, int y, int z, long seed) {
		BiomeSelectorElement current = PRIMARY;
		while (!(current instanceof Biome)) {
			current = ((BiomeSelectorLayer) current).pick(x, y, z, seed);
		}
		return (Biome) current;
	}
}
