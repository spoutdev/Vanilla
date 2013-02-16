/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.world.generator.biome;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.spout.vanilla.world.generator.nether.biome.NetherrackBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.JungleHillsBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.MushroomShoreBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.RiverBiome;
import org.spout.vanilla.world.generator.normal.biome.snowy.FrozenRiverBiome;
import org.spout.vanilla.world.generator.normal.biome.snowy.TaigaBiome;
import org.spout.vanilla.world.generator.normal.biome.snowy.TaigaHillsBiome;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.annotated.AnnotatedObjectConfiguration;
import org.spout.api.util.config.serialization.Serialization;

import org.spout.vanilla.util.ColorSerializer;

import org.spout.vanilla.world.generator.normal.biome.grassy.ForestBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.ForestHillsBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.JungleBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.MountainsBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.MushroomBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.OceanBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.PlainBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.SmallMountainsBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.SwampBiome;
import org.spout.vanilla.world.generator.normal.biome.sandy.BeachBiome;
import org.spout.vanilla.world.generator.normal.biome.sandy.DesertBiome;
import org.spout.vanilla.world.generator.normal.biome.sandy.DesertHillsBiome;
import org.spout.vanilla.world.generator.normal.biome.snowy.FrozenOceanBiome;
import org.spout.vanilla.world.generator.normal.biome.snowy.TundraBiome;
import org.spout.vanilla.world.generator.normal.biome.snowy.TundraHillsBiome;
import org.spout.vanilla.world.generator.theend.biome.EndStoneBiome;
import org.spout.vanilla.plugin.world.generator.skylands.biome.SkylandsBiome;

public class VanillaBiomes {
	public static final OceanBiome OCEAN = new OceanBiome(0);
	public static final PlainBiome PLAINS = new PlainBiome(1);
	public static final DesertBiome DESERT = new DesertBiome(2);
	public static final DesertHillsBiome DESERT_HILLS = new DesertHillsBiome(17);
	public static final MountainsBiome MOUNTAINS = new MountainsBiome(3);
	public static final ForestBiome FOREST = new ForestBiome(4);
	public static final ForestHillsBiome FOREST_HILLS = new ForestHillsBiome(18);
	public static final TaigaBiome TAIGA = new TaigaBiome(5);
	public static final TaigaHillsBiome TAIGA_HILLS = new TaigaHillsBiome(19);
	public static final SwampBiome SWAMP = new SwampBiome(6);
	public static final RiverBiome RIVER = new RiverBiome(7);
	public static final FrozenRiverBiome FROZEN_RIVER = new FrozenRiverBiome(11);
	public static final NetherrackBiome NETHERRACK = new NetherrackBiome(8);
	public static final TundraBiome TUNDRA = new TundraBiome(12);
	public static final TundraHillsBiome TUNDRA_HILLS = new TundraHillsBiome(13);
	public static final MushroomBiome MUSHROOM = new MushroomBiome(14);
	public static final MushroomShoreBiome MUSHROOM_SHORE = new MushroomShoreBiome(15);
	public static final BeachBiome BEACH = new BeachBiome(16);
	public static final SmallMountainsBiome SMALL_MOUNTAINS = new SmallMountainsBiome(20);
	public static final JungleBiome JUNGLE = new JungleBiome(21);
	public static final JungleHillsBiome JUNGLE_HILLS = new JungleHillsBiome(22);
	public static final FrozenOceanBiome FROZEN_OCEAN = new FrozenOceanBiome(10);
	public static final EndStoneBiome ENDSTONE = new EndStoneBiome(22);
	public static final SkylandsBiome SKYLANDS = new SkylandsBiome(OCEAN.getBiomeId());
	private static final Map<String, VanillaBiome> BY_NAME = new HashMap<String, VanillaBiome>();

	static {
		for (Field objectField : VanillaBiomes.class.getDeclaredFields()) {
			objectField.setAccessible(true);
			try {
				final Object object = objectField.get(null);
				if (object instanceof VanillaBiome) {
					BY_NAME.put(objectField.getName().toLowerCase(), (VanillaBiome) object);
				}
			} catch (Exception ex) {
				System.out.println("Could not properly reflect VanillaBiomes! Unexpected behaviour may occur, please report to http://issues.spout.org!");
				ex.printStackTrace();
			}
		}
		Serialization.registerSerializer(new ColorSerializer());
	}

	public static void load(Configuration config) {
		final AnnotatedObjectConfiguration biomes =
				new AnnotatedObjectConfiguration(config);
		for (Entry<String, VanillaBiome> entry : BY_NAME.entrySet()) {
			biomes.addObject(entry.getValue(), entry.getKey());
		}
		try {
			biomes.load();
		} catch (ConfigurationException ex) {
			ex.printStackTrace();
		}
	}

	public static void save(Configuration config) {
		final AnnotatedObjectConfiguration biomes =
				new AnnotatedObjectConfiguration(config);
		for (Entry<String, VanillaBiome> entry : BY_NAME.entrySet()) {
			biomes.addObject(entry.getValue(), entry.getKey());
		}
		try {
			biomes.save();
		} catch (ConfigurationException ex) {
			ex.printStackTrace();
		}
	}

	public static VanillaBiome byName(String name) {
		return BY_NAME.get(name.toLowerCase());
	}

	public static Collection<VanillaBiome> getBiomes() {
		return BY_NAME.values();
	}
}
