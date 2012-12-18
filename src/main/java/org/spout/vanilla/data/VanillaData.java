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
package org.spout.vanilla.data;

import java.util.Random;

import org.spout.api.inventory.Inventory;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyArray;
import org.spout.api.map.DefaultedKeyFactory;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.math.Vector3;

import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.inventory.player.PlayerArmorInventory;
import org.spout.vanilla.inventory.player.PlayerCraftingInventory;
import org.spout.vanilla.inventory.player.PlayerMainInventory;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.material.block.component.FurnaceBlock;

/**
 * * Common Vanilla-like data mappings that are found in Vanilla.
 */
public class VanillaData {
	private static final Random random = new Random();
	//World-specific
	public static final DefaultedKey<Dimension> DIMENSION = new DefaultedKeyImpl<Dimension>("dimension", Dimension.NORMAL);
	public static final DefaultedKey<Difficulty> DIFFICULTY = new DefaultedKeyImpl<Difficulty>("difficulty", Difficulty.EASY);
	public static final DefaultedKey<Weather> WEATHER = new DefaultedKeyImpl<Weather>("weather", Weather.CLEAR);
	public static final DefaultedKey<WorldType> WORLD_TYPE = new DefaultedKeyImpl<WorldType>("world_type", WorldType.DEFAULT);
	//Component-specific
	public static final DefaultedKey<GameMode> GAMEMODE = new DefaultedKeyImpl<GameMode>("game_mode", GameMode.SURVIVAL);
	public static final DefaultedKey<Float> AIR_SECS = new DefaultedKeyImpl<Float>("air_secs", 15f);
	public static final DefaultedKey<Integer> DEATH_TICKS = new DefaultedKeyImpl<Integer>("death_ticks", 0);
	public static final DefaultedKey<Integer> FIRE_TICKS = new DefaultedKeyImpl<Integer>("fire_ticks", 0);
	public static final DefaultedKey<Long> GROWTH_TICKS = new DefaultedKeyImpl<Long>("growth_ticks", Long.valueOf(0));
	public static final DefaultedKey<Boolean> FLAMMABLE = new DefaultedKeyImpl<Boolean>("flammable", false);
	public static final DefaultedKey<Integer> HEALTH = new DefaultedKeyImpl<Integer>("health", 1);
	public static final DefaultedKey<Integer> MAX_HEALTH = new DefaultedKeyImpl<Integer>("max_health", 1);
	public static final DefaultedKey<Vector3> MAX_SPEED = new DefaultedKeyImpl<Vector3>("max_speed", Vector3.ZERO);
	public static final DefaultedKey<Vector3> MOVEMENT_SPEED = new DefaultedKeyImpl<Vector3>("movement_speed", Vector3.ZERO);
	public static final DefaultedKey<Integer> INTERACT_REACH = new DefaultedKeyImpl<Integer>("interact_reach", 5);
	public static final DefaultedKey<Vector3> VELOCITY = new DefaultedKeyImpl<Vector3>("velocity", Vector3.ZERO);
	public static final DefaultedKey<Inventory> ENTITY_INVENTORY = new DefaultedKeyImpl<Inventory>("inventory", null);
	public static final DefaultedKey<ChestInventory> CHEST_INVENTORY = new DefaultedKeyFactory<ChestInventory>("chest_inventory", ChestInventory.class);
	public static final DefaultedKey<Integer> HUNGER = new DefaultedKeyImpl<Integer>("hunger", 20);
	public static final DefaultedKey<Float> FOOD_SATURATION = new DefaultedKeyImpl<Float>("food_saturation", 5f);
	public static final DefaultedKey<Float> EXHAUSTION = new DefaultedKeyImpl<Float>("exhaustion", 0f);
	public static final DefaultedKey<Boolean> POISONED = new DefaultedKeyImpl<Boolean>("poisoned", false);
	// Furnace
	public static final DefaultedKey<FurnaceInventory> FURNACE_INVENTORY = new DefaultedKeyFactory<FurnaceInventory>("inventory", FurnaceInventory.class);
	public static final DefaultedKey<Float> FURNACE_FUEL = new DefaultedKeyImpl<Float>("fuel", 0f);
	public static final DefaultedKey<Float> MAX_FURNACE_FUEL = new DefaultedKeyImpl<Float>("max_fuel", 0f);
	public static final DefaultedKey<Float> SMELT_TIME = new DefaultedKeyImpl<Float>("smelt_time", -1f);
	public static final DefaultedKey<Float> MAX_SMELT_TIME = new DefaultedKeyImpl<Float>("max_smelt_time", FurnaceBlock.SMELT_TIME);
	// TNT
	public static final DefaultedKey<Float> FUSE = new DefaultedKeyImpl<Float>("fuse", (float) random.nextInt(5) + 1);
	public static final DefaultedKey<Float> EXPLOSION_SIZE = new DefaultedKeyImpl<Float>("explosion_size", 4f);
	public static final DefaultedKey<Boolean> MAKES_FIRE = new DefaultedKeyImpl<Boolean>("makes_fire", false);
	//Entity data
	public static final DefaultedKey<Integer> ATTACHED_COUNT = new DefaultedKeyImpl<Integer>("attached_count", 0);
	public static final DefaultedKey<Boolean> IS_FALLING = new DefaultedKeyImpl<Boolean>("is_falling", false);
	public static final DefaultedKey<Boolean> IS_ON_GROUND = new DefaultedKeyImpl<Boolean>("is_on_Ground", true);
	public static final DefaultedKey<Boolean> IS_JUMPING = new DefaultedKeyImpl<Boolean>("is_jumping", false);
	public static final DefaultedKey<Boolean> HAS_DEATH_ANIMATION = new DefaultedKeyImpl<Boolean>("has_death_animation", true);
	//Human-specific
	public static final DefaultedKey<Boolean> IS_SPRINTING = new DefaultedKeyImpl<Boolean>("is_sprinting", false);
	public static final DefaultedKey<Boolean> IS_SNEAKING = new DefaultedKeyImpl<Boolean>("is_sneaking", false);
	public static final DefaultedKey<Boolean> IS_FLYING = new DefaultedKeyImpl<Boolean>("is_flying", false);
	// Inventory
	public static final DefaultedKey<PlayerMainInventory> MAIN_INVENTORY = new DefaultedKeyFactory<PlayerMainInventory>("main", PlayerMainInventory.class);
	public static final DefaultedKey<PlayerCraftingInventory> CRAFTING_INVENTORY = new DefaultedKeyFactory<PlayerCraftingInventory>("crafting", PlayerCraftingInventory.class);
	public static final DefaultedKey<PlayerArmorInventory> ARMOR_INVENTORY = new DefaultedKeyFactory<PlayerArmorInventory>("armor", PlayerArmorInventory.class);
	public static final DefaultedKey<PlayerQuickbar> QUICKBAR_INVENTORY = new DefaultedKeyFactory<PlayerQuickbar>("quickbar", PlayerQuickbar.class);
	public static final DefaultedKey<ChestInventory> ENDER_CHEST_INVENTORY = new DefaultedKeyFactory<ChestInventory>("ender_chest_inventory", ChestInventory.class);
	//Creature-specific
	public static final DefaultedKey<Integer> LINE_OF_SIGHT = new DefaultedKeyImpl<Integer>("line_of_sight", 1);
	//Item-specific
	public static final DefaultedKey<Integer> UNCOLLECTABLE_TICKS = new DefaultedKeyImpl<Integer>("uncollectable_ticks", 10);
	//Head-specific
	public static final DefaultedKey<Integer> HEAD_HEIGHT = new DefaultedKeyImpl<Integer>("head_height", 1);
	public static final DefaultedKey<Integer> HEAD_YAW = new DefaultedKeyImpl<Integer>("head_yaw", 0);
	//XPOrb-specific
	public static final DefaultedKey<Short> EXPERIENCE_AMOUNT = new DefaultedKeyImpl<Short>("experience_amount", (short) 0);
	public static final DefaultedKey<Long> TIME_DISPERSED = new DefaultedKeyImpl<Long>("time_dispersed", Long.valueOf(0));
	//Slime-specific
	public static final DefaultedKey<Byte> SLIME_SIZE = new SlimeSize("slime_size");
	//Sheep-specific
	public static final DefaultedKey<Short> WOOL_COLOR = new DefaultedKeyImpl<Short>("wool_color", (short) 0);
	public static final DefaultedKey<Boolean> SHEARED = new DefaultedKeyImpl<Boolean>("sheared", false);
	//Pig-specific
	public static final DefaultedKey<Boolean> SADDLED = new DefaultedKeyImpl<Boolean>("saddled", false);
	//Wolf/Ocelot-specific
	public static final DefaultedKey<Boolean> SITTING = new DefaultedKeyImpl<Boolean>("is_sitting", false);
	public static final DefaultedKey<Boolean> TAMED = new DefaultedKeyImpl<Boolean>("is_tamed", false);
	public static final DefaultedKey<String> OWNER = new DefaultedKeyImpl<String>("owner", null);
	//Ocelot-specific
	public static final DefaultedKey<Byte> OCELOT_SKIN = new DefaultedKeyImpl<Byte>("skin", (byte) 0);
	//Redstone-specific
	public static final DefaultedKey<Boolean> IS_POWERED = new DefaultedKeyImpl<Boolean>("is_powered", false);
	//Sign-specific
	public static final DefaultedKey<String[]> SIGN_TEXT = new DefaultedKeyArray<String>("sign_text", 4, String.class);
	//Zombie Specific
	public static final DefaultedKey<Boolean> WAS_VILLAGER = new DefaultedKeyImpl<Boolean>("was_villager", false);
}
