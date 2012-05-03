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

import org.spout.vanilla.generator.flat.biome.FlatGrassBiome;
import org.spout.vanilla.generator.nether.biome.NetherrackBiome;
import org.spout.vanilla.generator.normal.biome.BeachBiome;
import org.spout.vanilla.generator.normal.biome.DesertBiome;
import org.spout.vanilla.generator.normal.biome.ForestBiome;
import org.spout.vanilla.generator.normal.biome.JungleBiome;
import org.spout.vanilla.generator.normal.biome.MountainsBiome;
import org.spout.vanilla.generator.normal.biome.MushroomBiome;
import org.spout.vanilla.generator.normal.biome.OceanBiome;
import org.spout.vanilla.generator.normal.biome.PlainBiome;
import org.spout.vanilla.generator.normal.biome.RiverBiome;
import org.spout.vanilla.generator.normal.biome.SmallMountainsBiome;
import org.spout.vanilla.generator.normal.biome.SwampBiome;
import org.spout.vanilla.generator.normal.biome.TaigaBiome;
import org.spout.vanilla.generator.normal.biome.TundraBiome;
import org.spout.vanilla.generator.theend.biome.EndStoneBiome;

public class VanillaBiomes {
	public static final VanillaBiomeType BEACH = new BeachBiome(16);
	public static final VanillaBiomeType DESERT = new DesertBiome(2);
	public static final VanillaBiomeType FOREST = new ForestBiome(4);
	public static final VanillaBiomeType JUNGLE = new JungleBiome(21);
	public static final VanillaBiomeType MOUNTAINS = new MountainsBiome(3);
	public static final VanillaBiomeType MUSHROOM = new MushroomBiome(14);
	public static final VanillaBiomeType OCEAN = new OceanBiome(0);
	public static final VanillaBiomeType PLAIN = new PlainBiome(1);
	public static final VanillaBiomeType RIVER = new RiverBiome(7);
	public static final VanillaBiomeType SMALL_MOUNTAINS = new SmallMountainsBiome(20);
	public static final VanillaBiomeType SWAMP = new SwampBiome(6);
	public static final VanillaBiomeType TAIGA = new TaigaBiome(5);
	public static final VanillaBiomeType TUNDRA = new TundraBiome(12);
	public static final VanillaBiomeType ENDSTONE = new EndStoneBiome();
	public static final VanillaBiomeType NETHERRACK = new NetherrackBiome();
	public static final VanillaBiomeType FLATGRASS = new FlatGrassBiome();
}
