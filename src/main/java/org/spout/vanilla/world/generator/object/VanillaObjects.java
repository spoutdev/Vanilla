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
package org.spout.vanilla.world.generator.object;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.spout.api.generator.WorldGeneratorObject;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.nether.object.GlowstonePatchObject;
import org.spout.vanilla.world.generator.nether.object.NetherPortalObject;
import org.spout.vanilla.world.generator.normal.object.BlockPatchObject;
import org.spout.vanilla.world.generator.normal.object.DungeonObject;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.normal.object.OreObject;
import org.spout.vanilla.world.generator.normal.object.OreObject.OreType;
import org.spout.vanilla.world.generator.normal.object.PondObject;
import org.spout.vanilla.world.generator.normal.object.PondObject.PondType;
import org.spout.vanilla.world.generator.normal.object.SnowObject;
import org.spout.vanilla.world.generator.normal.object.WellObject;
import org.spout.vanilla.world.generator.normal.object.largeplant.CactusStackObject;
import org.spout.vanilla.world.generator.normal.object.largeplant.HugeMushroomObject;
import org.spout.vanilla.world.generator.normal.object.largeplant.HugeMushroomObject.HugeMushroomType;
import org.spout.vanilla.world.generator.normal.object.largeplant.SugarCaneStackObject;
import org.spout.vanilla.world.generator.normal.object.tree.BigTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.HugeTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.PineTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.ShrubObject;
import org.spout.vanilla.world.generator.normal.object.tree.SmallTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.SpruceTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.SwampTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.TreeObject.TreeType;
import org.spout.vanilla.world.generator.structure.mineshaft.Mineshaft;
import org.spout.vanilla.world.generator.structure.temple.Temple;
import org.spout.vanilla.world.generator.theend.object.SpireObject;

/**
 * IMPORTANT: These objects should not be used by plugins. Any modifications to
 * these objects will affect the output of the vanilla generators.
 */
public class VanillaObjects {
	public static final BigTreeObject BIG_OAK_TREE = new BigTreeObject();
	public static final CactusStackObject CACTUS_STACK = new CactusStackObject();
	public static final DungeonObject DUNGEON = new DungeonObject();
	public static final HugeMushroomObject HUGE_RED_MUSHROOM = new HugeMushroomObject(HugeMushroomType.RED);
	public static final HugeMushroomObject HUGE_BROWN_MUSHROOM = new HugeMushroomObject(HugeMushroomType.BROWN);
	public static final HugeTreeObject HUGE_JUNGLE_TREE = new HugeTreeObject();
	public static final OreObject DIRT_ORE = new OreObject(OreType.DIRT);
	public static final OreObject GRAVEL_ORE = new OreObject(OreType.GRAVEL);
	public static final OreObject COAL_ORE = new OreObject(OreType.COAL);
	public static final OreObject IRON_ORE = new OreObject(OreType.IRON);
	public static final OreObject GOLD_ORE = new OreObject(OreType.GOLD);
	public static final OreObject REDSTONE_ORE = new OreObject(OreType.REDSTONE);
	public static final OreObject DIAMOND_ORE = new OreObject(OreType.DIAMOND);
	public static final OreObject LAPIS_LAZULI_ORE = new OreObject(OreType.LAPIS_LAZULI);
	public static final PondObject LAVA_POND = new PondObject(PondType.LAVA);
	public static final PondObject WATER_POND = new PondObject(PondType.WATER);
	public static final ShrubObject JUNGLE_SHRUB = new ShrubObject();
	public static final SmallTreeObject SMALL_OAK_TREE = new SmallTreeObject();
	public static final SmallTreeObject SMALL_BIRCH_TREE = new SmallTreeObject();
	public static final SmallTreeObject SMALL_JUNGLE_TREE = new SmallTreeObject();
	public static final SwampTreeObject SWAMP_TREE = new SwampTreeObject();
	public static final PineTreeObject PINE_TREE = new PineTreeObject();
	public static final SpruceTreeObject SPRUCE_TREE = new SpruceTreeObject();
	public static final SugarCaneStackObject SUGAR_CANE_STACK = new SugarCaneStackObject();
	public static final WellObject DESERT_WELL = new WellObject();
	public static final SpireObject THE_END_SPIRE = new SpireObject();
	public static final NetherPortalObject NETHER_PORTAL = new NetherPortalObject();
	public static final BlockPatchObject SAND_PATCH = new BlockPatchObject(VanillaMaterials.SAND);
	public static final BlockPatchObject CLAY_PATCH = new BlockPatchObject(VanillaMaterials.CLAY_BLOCK);
	public static final LootChestObject LOOT_CHEST = new LootChestObject();
	public static final SnowObject FALLING_SNOW = new SnowObject();
	public static final GlowstonePatchObject GLOWSTONE_PATCH = new GlowstonePatchObject();
	public static final Mineshaft MINESHAFT = new Mineshaft();
	public static final Temple TEMPLE = new Temple();
	private static final Map<String, WorldGeneratorObject> BY_NAME = new HashMap<String, WorldGeneratorObject>();

	static {
		SMALL_BIRCH_TREE.setTreeType(TreeType.BIRCH);
		SMALL_JUNGLE_TREE.setTreeType(TreeType.JUNGLE);
		SMALL_JUNGLE_TREE.setBaseHeight((byte) 4);
		SMALL_JUNGLE_TREE.setRandomHeight((byte) 10);
		SMALL_JUNGLE_TREE.addLogVines(true);
		SMALL_JUNGLE_TREE.addCocoaPlants(true);
		CLAY_PATCH.setHeightRadius((byte) 1);
		CLAY_PATCH.getOverridableMaterials().clear();
		CLAY_PATCH.getOverridableMaterials().add(VanillaMaterials.DIRT);
		for (Field objectField : VanillaObjects.class.getDeclaredFields()) {
			objectField.setAccessible(true);
			try {
				final Object object = objectField.get(null);
				if (object instanceof WorldGeneratorObject) {
					BY_NAME.put(objectField.getName().toLowerCase(), (WorldGeneratorObject) object);
				}
			} catch (Exception ex) {
				System.out.println("Could not properly reflect VanillaObjects! Unexpected behaviour may occur, "
						+ "please report to http://issues.spout.org!");
				ex.printStackTrace();
			}
		}
	}

	public static WorldGeneratorObject byName(String name) {
		return BY_NAME.get(name.toLowerCase());
	}

	public static Collection<WorldGeneratorObject> getObjects() {
		return BY_NAME.values();
	}
}
