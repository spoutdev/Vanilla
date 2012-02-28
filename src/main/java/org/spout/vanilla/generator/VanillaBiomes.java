/*
 * This file is part of Vanilla.
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

import org.spout.vanilla.biome.BiomeType;
import org.spout.vanilla.generator.nether.biome.NetherrackBiome;
import org.spout.vanilla.generator.normal.biome.DesertBiome;
import org.spout.vanilla.generator.normal.biome.MountainBiome;
import org.spout.vanilla.generator.normal.biome.MushroomBiome;
import org.spout.vanilla.generator.normal.biome.OceanBiome;
import org.spout.vanilla.generator.normal.biome.PlainBiome;
import org.spout.vanilla.generator.normal.biome.SwampBiome;
import org.spout.vanilla.generator.normal.biome.TaigaBiome;
import org.spout.vanilla.generator.normal.biome.TundraBiome;
import org.spout.vanilla.generator.theend.biome.EndStoneBiome;



public class VanillaBiomes {
	public static final BiomeType DESERT = new DesertBiome();
	public static final BiomeType JUNGLE = new PlainBiome();
	public static final BiomeType MOUNTAIN = new MountainBiome();
	public static final BiomeType MUSHROOM = new MushroomBiome();
	public static final BiomeType OCEAN = new OceanBiome();
	public static final BiomeType PLAIN = new PlainBiome();
	public static final BiomeType SWAMP = new SwampBiome();
	public static final BiomeType TAIGA = new TaigaBiome();
	public static final BiomeType TUNDRA = new TundraBiome();
	public static final BiomeType ENDSTONE = new EndStoneBiome();
	public static final BiomeType NETHERRACK = new NetherrackBiome();
}
