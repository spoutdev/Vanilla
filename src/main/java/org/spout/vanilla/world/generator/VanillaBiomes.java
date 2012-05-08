/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.world.generator;

import org.spout.vanilla.world.generator.flat.biome.FlatGrassBiome;
import org.spout.vanilla.world.generator.nether.biome.NetherrackBiome;
import org.spout.vanilla.world.generator.normal.biome.BeachBiome;
import org.spout.vanilla.world.generator.normal.biome.DesertBiome;
import org.spout.vanilla.world.generator.normal.biome.ForestBiome;
import org.spout.vanilla.world.generator.normal.biome.JungleBiome;
import org.spout.vanilla.world.generator.normal.biome.MountainsBiome;
import org.spout.vanilla.world.generator.normal.biome.MushroomBiome;
import org.spout.vanilla.world.generator.normal.biome.OceanBiome;
import org.spout.vanilla.world.generator.normal.biome.PlainBiome;
import org.spout.vanilla.world.generator.normal.biome.RiverBiome;
import org.spout.vanilla.world.generator.normal.biome.SmallMountainsBiome;
import org.spout.vanilla.world.generator.normal.biome.SwampBiome;
import org.spout.vanilla.world.generator.normal.biome.TaigaBiome;
import org.spout.vanilla.world.generator.normal.biome.TundraBiome;
import org.spout.vanilla.world.generator.theend.biome.EndStoneBiome;

public class VanillaBiomes {
	//TODO These numbers need to be checked...
	public static final VanillaBiome OCEAN = new OceanBiome(0);
	public static final VanillaBiome PLAIN = new PlainBiome(1);
	public static final VanillaBiome DESERT = new DesertBiome(2);
	public static final VanillaBiome MOUNTAINS = new MountainsBiome(3);
	public static final VanillaBiome FOREST = new ForestBiome(4);
	public static final VanillaBiome TAIGA = new TaigaBiome(5);
	public static final VanillaBiome SWAMP = new SwampBiome(6);
	public static final VanillaBiome RIVER = new RiverBiome(7);
	public static final VanillaBiome NETHERRACK = new NetherrackBiome(8);
	public static final VanillaBiome TUNDRA = new TundraBiome(12);
	public static final VanillaBiome MUSHROOM = new MushroomBiome(14);
	public static final VanillaBiome BEACH = new BeachBiome(16);
	public static final VanillaBiome SMALL_MOUNTAINS = new SmallMountainsBiome(20);
	public static final VanillaBiome JUNGLE = new JungleBiome(21);
	public static final VanillaBiome ENDSTONE = new EndStoneBiome(22);
	public static final VanillaBiome FLATGRASS = new FlatGrassBiome(23);
}
