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
package org.spout.vanilla.world.generator;

import org.spout.vanilla.world.generator.nether.biome.NetherrackBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.DesertBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.ForestBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.JungleBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.MountainsBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.MushroomBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.OceanBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.PlainBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.RiverBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.SmallMountainsBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.TaigaBiome;
import org.spout.vanilla.world.generator.normal.biome.basic.TundraBiome;
import org.spout.vanilla.world.generator.normal.biome.hills.DesertHillsBiome;
import org.spout.vanilla.world.generator.normal.biome.hills.ForestHillsBiome;
import org.spout.vanilla.world.generator.normal.biome.hills.JungleHillsBiome;
import org.spout.vanilla.world.generator.normal.biome.hills.TaigaHillsBiome;
import org.spout.vanilla.world.generator.normal.biome.hills.TundraHillsBiome;
import org.spout.vanilla.world.generator.normal.biome.shore.BeachBiome;
import org.spout.vanilla.world.generator.normal.biome.shore.SwampBiome;
import org.spout.vanilla.world.generator.normal.biome.special.FrozenOceanBiome;
import org.spout.vanilla.world.generator.normal.biome.special.FrozenRiverBiome;
import org.spout.vanilla.world.generator.normal.biome.special.MushroomShoreBiome;
import org.spout.vanilla.world.generator.theend.biome.EndStoneBiome;

public class VanillaBiomes {
	//TODO These numbers need to be checked...
	public static final VanillaBiome OCEAN = new OceanBiome(0);
	public static final VanillaBiome PLAINS = new PlainBiome(1);
	public static final VanillaBiome DESERT = new DesertBiome(2);
	public static final VanillaBiome DESERT_HILLS = new DesertHillsBiome(17);
	public static final VanillaBiome MOUNTAINS = new MountainsBiome(3);
	public static final VanillaBiome FOREST = new ForestBiome(4);
	public static final VanillaBiome FOREST_HILLS = new ForestHillsBiome(18);
	public static final VanillaBiome TAIGA = new TaigaBiome(5);
	public static final VanillaBiome TAIGA_HILLS = new TaigaHillsBiome(19);
	public static final VanillaBiome SWAMP = new SwampBiome(6);
	public static final VanillaBiome RIVER = new RiverBiome(7);
	public static final VanillaBiome FROZEN_RIVER = new FrozenRiverBiome(11);
	public static final VanillaBiome NETHERRACK = new NetherrackBiome(8);
	public static final VanillaBiome TUNDRA = new TundraBiome(12);
	public static final VanillaBiome TUNDRA_HILLS = new TundraHillsBiome(13);
	public static final VanillaBiome MUSHROOM = new MushroomBiome(14);
	public static final VanillaBiome MUSHROOM_SHORE = new MushroomShoreBiome(15);
	public static final VanillaBiome BEACH = new BeachBiome(16);
	public static final VanillaBiome SMALL_MOUNTAINS = new SmallMountainsBiome(20);
	public static final VanillaBiome JUNGLE = new JungleBiome(21);
	public static final VanillaBiome JUNGLE_HILLS = new JungleHillsBiome(22);
	public static final VanillaBiome FROZEN_OCEAN = new FrozenOceanBiome(10);
	public static final VanillaBiome ENDSTONE = new EndStoneBiome(22);
}
