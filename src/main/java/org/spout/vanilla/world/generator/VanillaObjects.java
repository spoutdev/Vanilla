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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.object.tree.BigTreeObject;
import org.spout.vanilla.world.generator.normal.object.largeplant.CactusStackObject;
import org.spout.vanilla.world.generator.normal.object.DungeonObject;
import org.spout.vanilla.world.generator.normal.object.largeplant.HugeMushroomObject;
import org.spout.vanilla.world.generator.normal.object.largeplant.HugeMushroomObject.HugeMushroomType;
import org.spout.vanilla.world.generator.normal.object.tree.HugeTreeObject;
import org.spout.vanilla.world.generator.normal.object.OreObject;
import org.spout.vanilla.world.generator.normal.object.tree.PineTreeObject;
import org.spout.vanilla.world.generator.normal.object.PondObject;
import org.spout.vanilla.world.generator.normal.object.PondObject.PondType;
import org.spout.vanilla.world.generator.normal.object.tree.ShrubObject;
import org.spout.vanilla.world.generator.normal.object.tree.SmallTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.SpruceTreeObject;
import org.spout.vanilla.world.generator.normal.object.largeplant.SugarCaneStackObject;
import org.spout.vanilla.world.generator.normal.object.tree.TreeObject.TreeType;
import org.spout.vanilla.world.generator.normal.object.WellObject;
import org.spout.vanilla.world.generator.theend.object.SpireObject;

/**
 *
 * IMPORTANT: These objects maybe be modified by plugins. There is no guarantee
 * these will be MC like. For unaltered objects, please create a new instance.
 */
public class VanillaObjects {
	public static final BigTreeObject BIG_OAK_TREE = new BigTreeObject(TreeType.OAK);
	public static final CactusStackObject CACTUS_STACK = new CactusStackObject();
	public static final DungeonObject DUNGEON = new DungeonObject();
	public static final HugeMushroomObject HUGE_RED_MUSHROOM = new HugeMushroomObject(HugeMushroomType.RED);
	public static final HugeMushroomObject HUGE_BROWN_MUSHROOM = new HugeMushroomObject(HugeMushroomType.BROWN);
	public static final HugeTreeObject HUGE_JUNGLE_TREE = new HugeTreeObject();
	public static final OreObject DIRT_ORE = new OreObject(VanillaMaterials.DIRT, 20, 32, 128);
	public static final OreObject GRAVEL_ORE = new OreObject(VanillaMaterials.GRAVEL, 10, 32, 128);
	public static final OreObject COAL_ORE = new OreObject(VanillaMaterials.COAL_ORE, 20, 16, 128);
	public static final OreObject IRON_ORE = new OreObject(VanillaMaterials.IRON_ORE, 20, 8, 64);
	public static final OreObject GOLD_ORE = new OreObject(VanillaMaterials.GOLD_ORE, 2, 8, 32);
	public static final OreObject REDSTONE_ORE = new OreObject(VanillaMaterials.REDSTONE_ORE, 8, 7, 16);
	public static final OreObject DIAMOND_ORE = new OreObject(VanillaMaterials.DIAMOND_ORE, 1, 7, 16);
	public static final OreObject LAPIS_LAZULI_ORE = new OreObject(VanillaMaterials.LAPIS_LAZULI_ORE, 1, 6, 32);
	public static final PondObject LAVA_POND = new PondObject(PondType.LAVA);
	public static final PondObject WATER_POND = new PondObject(PondType.WATER);
	public static final ShrubObject SHRUB = new ShrubObject();
	public static final SmallTreeObject SMALL_OAK_TREE = new SmallTreeObject(TreeType.OAK);
	public static final SmallTreeObject SMALL_BIRCH_TREE = new SmallTreeObject(TreeType.BIRCH);
	public static final SmallTreeObject SMALL_JUNGLE_TREE = new SmallTreeObject(TreeType.JUNGLE);// alter it
	public static final SmallTreeObject SMALL_SWAMP_TREE = new SmallTreeObject(TreeType.OAK); // alter it
	public static final PineTreeObject PINE_TREE = new PineTreeObject();
	public static final SpruceTreeObject SPRUCE_TREE = new SpruceTreeObject();
	public static final SugarCaneStackObject SUGAR_CANE_STACK = new SugarCaneStackObject();
	public static final WellObject WELL = new WellObject();
	public static final SpireObject SPIRE = new SpireObject();
	// for the '/obj' test command
	private static final Map<String, WorldGeneratorObject> BY_NAME = new HashMap<String, WorldGeneratorObject>();
	
	static {
		SMALL_JUNGLE_TREE.setBaseHeight((byte) 4);
		SMALL_JUNGLE_TREE.setRandomHeight((byte) 10);
		SMALL_JUNGLE_TREE.addLogVines(true);
		SMALL_SWAMP_TREE.addLeavesVines(true);
		SMALL_SWAMP_TREE.setLeavesRadiusIncreaseXZ((byte) 1);
		// for the '/obj' test command
		for (Field objectField : VanillaObjects.class.getDeclaredFields()) {
            objectField.setAccessible(true);
			try {
				Object o = objectField.get(null);
				if (o instanceof WorldGeneratorObject) {
					BY_NAME.put(objectField.getName().toLowerCase(), (WorldGeneratorObject) o);
				}
			} catch (Exception ex) {
			}
        }
	}
	
	public static WorldGeneratorObject byName(String name) {
		return BY_NAME.get(name.toLowerCase());
	}
}
