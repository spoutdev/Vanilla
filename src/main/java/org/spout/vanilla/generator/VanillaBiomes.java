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

import org.spout.api.generator.biome.BiomeType;

import org.spout.vanilla.generator.nether.biome.NetherrackBiome;
import org.spout.vanilla.generator.normal.biome.DesertBiome;
import org.spout.vanilla.generator.normal.biome.JungleBiome;
import org.spout.vanilla.generator.normal.biome.MountainBiome;
import org.spout.vanilla.generator.normal.biome.MushroomBiome;
import org.spout.vanilla.generator.normal.biome.OceanBiome;
import org.spout.vanilla.generator.normal.biome.PlainBiome;
import org.spout.vanilla.generator.normal.biome.SwampBiome;
import org.spout.vanilla.generator.normal.biome.TaigaBiome;
import org.spout.vanilla.generator.normal.biome.TundraBiome;
import org.spout.vanilla.generator.normal.biome.UndefinedBiome;
import org.spout.vanilla.generator.theend.biome.EndStoneBiome;

public class VanillaBiomes {
	public static final VanillaBiomeType DESERT = new DesertBiome();
	public static final VanillaBiomeType JUNGLE = new JungleBiome();
	public static final VanillaBiomeType MOUNTAIN = new MountainBiome();
	public static final VanillaBiomeType MUSHROOM = new MushroomBiome();
	public static final VanillaBiomeType OCEAN = new OceanBiome();
	public static final VanillaBiomeType PLAIN = new PlainBiome();
	public static final VanillaBiomeType SWAMP = new SwampBiome();
	public static final VanillaBiomeType TAIGA = new TaigaBiome();
	public static final VanillaBiomeType TUNDRA = new TundraBiome();
	public static final VanillaBiomeType ENDSTONE = new EndStoneBiome();
	public static final VanillaBiomeType NETHERRACK = new NetherrackBiome();
	public static final VanillaBiomeType UNDEFINED = new UndefinedBiome();
}
