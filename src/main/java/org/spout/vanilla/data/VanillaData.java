/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import java.util.HashMap;

import org.spout.api.inventory.ItemStack;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyArray;
import org.spout.api.map.DefaultedKeyFactory;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.ChatStyle;
import org.spout.vanilla.data.effect.EntityEffectType;
import org.spout.vanilla.inventory.block.BrewingStandInventory;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.block.DispenserInventory;
import org.spout.vanilla.inventory.block.DropperInventory;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.inventory.block.HopperInventory;
import org.spout.vanilla.inventory.entity.EntityArmorInventory;
import org.spout.vanilla.inventory.entity.EntityQuickbarInventory;
import org.spout.vanilla.inventory.entity.HorseInventory;
import org.spout.vanilla.inventory.entity.VillagerInventory;
import org.spout.vanilla.inventory.player.DropInventory;
import org.spout.vanilla.inventory.player.PlayerArmorInventory;
import org.spout.vanilla.inventory.player.PlayerCraftingInventory;
import org.spout.vanilla.inventory.player.PlayerMainInventory;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.material.block.component.FurnaceBlock;
import org.spout.vanilla.protocol.entity.creature.CreatureType;

/**
 * * Common Vanilla-like data mappings that are found in Vanilla.
 */
public class VanillaData {
	//World-specific
	public static final DefaultedKey<Dimension> DIMENSION = new DefaultedKeyImpl<Dimension>("dimension", Dimension.NORMAL);
	public static final DefaultedKey<Difficulty> DIFFICULTY = new DefaultedKeyImpl<Difficulty>("difficulty", Difficulty.EASY);
	public static final DefaultedKey<WorldType> WORLD_TYPE = new DefaultedKeyImpl<WorldType>("world_type", WorldType.DEFAULT);
	//Component-specific
	public static final DefaultedKey<GameMode> GAMEMODE = new DefaultedKeyImpl<GameMode>("game_mode", GameMode.SURVIVAL);
	public static final DefaultedKey<Float> AIR_SECS = new DefaultedKeyImpl<Float>("air_secs", 15f);
	public static final DefaultedKey<Integer> DEATH_TICKS = new DefaultedKeyImpl<Integer>("death_ticks", 0);
	public static final DefaultedKey<Long> GROWTH_TICKS = new DefaultedKeyImpl<Long>("growth_ticks", Long.valueOf(0));
	public static final DefaultedKey<Float> HEALTH = new DefaultedKeyImpl<Float>("health", 1.0f);
	public static final DefaultedKey<Float> MAX_HEALTH = new DefaultedKeyImpl<Float>("max_health", 1.0f);
	public static final DefaultedKey<Vector3> MAX_SPEED = new DefaultedKeyImpl<Vector3>("max_speed", Vector3.ZERO);
	public static final DefaultedKey<Vector3> MOVEMENT_SPEED = new DefaultedKeyImpl<Vector3>("movement_speed", Vector3.ZERO);
	public static final DefaultedKey<Integer> INTERACT_REACH = new DefaultedKeyImpl<Integer>("interact_reach", 5);
	public static final DefaultedKey<Vector3> VELOCITY = new DefaultedKeyImpl<Vector3>("velocity", Vector3.ZERO);
	public static final DefaultedKey<Integer> HUNGER = new DefaultedKeyImpl<Integer>("hunger", 20);
	public static final DefaultedKey<Float> FOOD_SATURATION = new DefaultedKeyImpl<Float>("food_saturation", 5f);
	public static final DefaultedKey<Float> EXHAUSTION = new DefaultedKeyImpl<Float>("exhaustion", 0f);
	public static final DefaultedKey<Boolean> POISONED = new DefaultedKeyImpl<Boolean>("poisoned", false);
	public static final DefaultedKey<Float> AGE = new DefaultedKeyImpl<Float>("age", 0f);
	public static final DefaultedKey<Boolean> IN_LOVE = new DefaultedKeyImpl<Boolean>("in_love", false);
	// Brewing stand
	public static final DefaultedKey<Float> BREW_TIME = new DefaultedKeyImpl<Float>("brew_time", -1f);
	// Furnace
	public static final DefaultedKey<Float> FURNACE_FUEL = new DefaultedKeyImpl<Float>("fuel", 0f);
	public static final DefaultedKey<Float> MAX_FURNACE_FUEL = new DefaultedKeyImpl<Float>("max_fuel", 0f);
	public static final DefaultedKey<Float> SMELT_TIME = new DefaultedKeyImpl<Float>("smelt_time", -1f);
	public static final DefaultedKey<FurnaceInventory> FURNACE_INVENTORY = new DefaultedKeyFactory<FurnaceInventory>("inventory", FurnaceInventory.class);
	public static final DefaultedKey<Float> MAX_SMELT_TIME = new DefaultedKeyImpl<Float>("max_smelt_time", FurnaceBlock.SMELT_TIME);
	// Skull block
	public static final DefaultedKey<Float> SKULL_ROTATION = new DefaultedKeyImpl<Float>("skull_rot", 0.0f);
	// Note block
	public static final DefaultedKey<Integer> NOTE = new DefaultedKeyImpl<Integer>("note", 0);
	// Jukebox
	public static final DefaultedKey<ItemStack> JUKEBOX_ITEM = new DefaultedKeyImpl<ItemStack>("playedItem", null);
	// TNT
	public static final DefaultedKey<Float> FUSE = new DefaultedKeyImpl<Float>("fuse", 5F);
	public static final DefaultedKey<Float> EXPLOSION_SIZE = new DefaultedKeyImpl<Float>("explosion_size", 4f);
	public static final DefaultedKey<Boolean> MAKES_FIRE = new DefaultedKeyImpl<Boolean>("makes_fire", false);
	// Beacon
	public static final DefaultedKey<EntityEffectType> PRIMARY_EFFECT = new DefaultedKeyImpl<EntityEffectType>("primary_effect", EntityEffectType.NONE);
	public static final DefaultedKey<EntityEffectType> SECONDARY_EFFECT = new DefaultedKeyImpl<EntityEffectType>("secondary_effect", EntityEffectType.NONE);
	public static final DefaultedKey<Float> UPDATE_DELAY = new DefaultedKeyImpl<Float>("update_delay", 4f);
	public static final DefaultedKey<Float> MAX_UPDATE_DELAY = new DefaultedKeyImpl<Float>("max_update_delay", 4f);
	public static final DefaultedKey<Float> EFFECT_DURATION = new DefaultedKeyImpl<Float>("effect_duration", 9f);
	//Entity data
	public static final DefaultedKey<HashMap> ATTACHED_COUNT = new DefaultedKeyImpl<HashMap>("attached_count", new HashMap<Class<? extends VanillaEntityComponent>, Integer>());
	public static final DefaultedKey<Boolean> IS_FALLING = new DefaultedKeyImpl<Boolean>("is_falling", false);
	public static final DefaultedKey<Boolean> IS_ON_GROUND = new DefaultedKeyImpl<Boolean>("is_on_Ground", true);
	public static final DefaultedKey<Boolean> IS_JUMPING = new DefaultedKeyImpl<Boolean>("is_jumping", false);
	public static final DefaultedKey<Boolean> IS_IN_WATER = new DefaultedKeyImpl<Boolean>("is_in_water", false);
	public static final DefaultedKey<Boolean> HAS_DEATH_ANIMATION = new DefaultedKeyImpl<Boolean>("has_death_animation", true);
	public static final DefaultedKey<Short> EXPERIENCE_AMOUNT = new DefaultedKeyImpl<Short>("experience_amount", (short) 0);
	public static final DefaultedKey<Float> EXPERIENCE_BAR_PROGRESS = new DefaultedKeyImpl<Float>("experience_bar_progress", (float) 0);
	public static final DefaultedKey<Short> EXPERIENCE_LEVEL = new DefaultedKeyImpl<Short>("experience_level", (short) 1);
	public static final DefaultedKey<Short> DROP_EXPERIENCE = new DefaultedKeyImpl<Short>("DropExperience", (short) 0);
	public static final DefaultedKey<Boolean> IS_EATING_BLOCKING = new DefaultedKeyImpl<Boolean>("is_eating_blocking", false);
	public static final DefaultedKey<Boolean> IS_RIDING = new DefaultedKeyImpl<Boolean>("is_riding", false);
	public static final DefaultedKey<Float> FIRE_TICK = new DefaultedKeyImpl<Float>("is_on_fire", 0.0f);
	public static final DefaultedKey<Boolean> FIRE_HURT = new DefaultedKeyImpl<Boolean>("fire_hurt", false);
	public static final DefaultedKey<Boolean> IS_CUSTOM_NAME_VISIBLE = new DefaultedKeyImpl<Boolean>("is_custom_name_visible", false);
	//Human-specific
	public static final DefaultedKey<Boolean> IS_SPRINTING = new DefaultedKeyImpl<Boolean>("is_sprinting", false);
	public static final DefaultedKey<Boolean> IS_SNEAKING = new DefaultedKeyImpl<Boolean>("is_sneaking", false);
	public static final DefaultedKey<Boolean> IS_FLYING = new DefaultedKeyImpl<Boolean>("is_flying", false);
	public static final DefaultedKey<Boolean> CAN_FLY = new DefaultedKeyImpl<Boolean>("can_fly", false);
	public static final DefaultedKey<Boolean> GOD_MODE = new DefaultedKeyImpl<Boolean>("god_mode", false);
	public static final DefaultedKey<Number> FLYING_SPEED = new DefaultedKeyImpl<Number>("flying_speed", 0.1f);
	public static final DefaultedKey<Number> WALKING_SPEED = new DefaultedKeyImpl<Number>("walking_speed", 0.10f);
	public static final DefaultedKey<ViewDistance> VIEW_DISTANCE = new DefaultedKeyImpl<ViewDistance>("view_distance", ViewDistance.NORMAL);
	public static final DefaultedKey<Byte> ARROWS_IN_BODY = new DefaultedKeyImpl<Byte>("arrows_in_body", (byte) 0);
	//Creature-specific
	public static final DefaultedKey<Integer> LINE_OF_SIGHT = new DefaultedKeyImpl<Integer>("line_of_sight", 1);
	//Item-specific
	public static final DefaultedKey<Number> UNCOLLECTABLE_TICKS = new DefaultedKeyImpl<Number>("uncollectable_ticks", (long) 0);
	// Arrow-specific
	public static final DefaultedKey<Boolean> CRITICAL = new DefaultedKeyImpl<Boolean>("critical", false);
	//Head-specific
	public static final DefaultedKey<Float> HEAD_HEIGHT = new DefaultedKeyImpl<Float>("head_height_v2", 1.62f);
	public static final DefaultedKey<Quaternion> HEAD_ROTATION = new DefaultedKeyImpl<Quaternion>("head_rotation", Quaternion.IDENTITY);
	//XPOrb-specific
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
	public static final DefaultedKey<String> OWNER = new DefaultedKeyImpl<String>("owner", "");
	//Ocelot-specific
	public static final DefaultedKey<Byte> OCELOT_SKIN = new DefaultedKeyImpl<Byte>("skin", (byte) 0);
	//Redstone-specific
	public static final DefaultedKey<Boolean> IS_POWERED = new DefaultedKeyImpl<Boolean>("is_powered", false);
	//Sign-specific
	public static final DefaultedKey<String[]> SIGN_TEXT = new DefaultedKeyArray<String>("sign_text", 4, String.class);
	//Zombie Specific
	public static final DefaultedKey<Boolean> WAS_VILLAGER = new DefaultedKeyImpl<Boolean>("was_villager", false);
	//Chicken Specific
	public static final DefaultedKey<Float> TIME_TILL_EGG = new DefaultedKeyImpl<Float>("time_till_egg", 0f);
	// Iron Golem Specific
	public static final DefaultedKey<Boolean> NATURALLY_SPAWNED = new DefaultedKeyImpl<Boolean>("spawned_naturally", true);
	// Creeper
	public static final DefaultedKey<Byte> STATE = new DefaultedKeyImpl<Byte>("state", (byte) -1);
	public static final DefaultedKey<Float> CREEPER_FUSE = new DefaultedKeyImpl<Float>("fuse", 1.5f);
	public static final DefaultedKey<Boolean> CHARGED = new DefaultedKeyImpl<Boolean>("charged", false);
	public static final DefaultedKey<Float> EXPLOSION_RADIUS = new DefaultedKeyImpl<Float>("explosion_radius", 3f);
	// Spider
	public static final DefaultedKey<Boolean> AGGRESSIVE = new DefaultedKeyImpl<Boolean>("aggressive", false);
	// Ghast
	public static final DefaultedKey<Boolean> RED_EYES = new DefaultedKeyImpl<Boolean>("red_eyes", false);
	// Enderman
	public static final DefaultedKey<Byte> HELD_MATERIAL = new DefaultedKeyImpl<Byte>("held_material", (byte) 0);
	public static final DefaultedKey<Byte> HELD_MATERIAL_DATA = new DefaultedKeyImpl<Byte>("held_material_data", (byte) 0);
	// Bat
	public static final DefaultedKey<Boolean> HANGING = new DefaultedKeyImpl<Boolean>("hanging", false);
	// Horse
	public static final DefaultedKey<Integer> TEMPER = new DefaultedKeyImpl<Integer>("temper", 5);
	public static final DefaultedKey<Integer> VARIANT = new DefaultedKeyImpl<Integer>("variant", 0);
	//Sky specific
	public static final DefaultedKey<Long> MAX_TIME = new DefaultedKeyImpl<Long>("max_time", 24000L);
	public static final DefaultedKey<Long> TIME_RATE = new DefaultedKeyImpl<Long>("time_rate", 1L);
	public static final DefaultedKey<Number> WORLD_TIME = new DefaultedKeyImpl<Number>("time_countdown", 0F);
	public static final DefaultedKey<Weather> WORLD_WEATHER = new DefaultedKeyImpl<Weather>("world_weather", Weather.CLEAR);
	public static final DefaultedKey<Weather> WORLD_FORECAST = new DefaultedKeyImpl<Weather>("world_forecast", Weather.CLEAR);
	public static final DefaultedKey<Float> WEATHER_CHANGE_TIME = new DefaultedKeyImpl<Float>("weather_change_time", 120000F);
	public static final DefaultedKey<Float> CURRENT_RAIN_STRENGTH = new DefaultedKeyImpl<Float>("current_rain_strength", 0F);
	public static final DefaultedKey<Float> PREVIOUS_RAIN_STRENGTH = new DefaultedKeyImpl<Float>("previous_rain_strength", 0F);
	public static final DefaultedKey<Integer> STORM_INTENSITY = new DefaultedKeyImpl<Integer>("storm_intensity_v2", 0);
	public static final DefaultedKey<Float> CURRENT_LIGHTNING_STRENGTH = new DefaultedKeyImpl<Float>("current_lightning_strength", 0F);
	public static final DefaultedKey<Float> PREVIOUS_LIGHTNING_STRENGTH = new DefaultedKeyImpl<Float>("previous_lightning_strength", 0F);
	//Painting specific
	public static final DefaultedKey<PaintingType> PAINTING_TYPE = new DefaultedKeyImpl<PaintingType>("painting_type", null);
	public static final DefaultedKey<BlockFace> PAINTING_FACE = new DefaultedKeyImpl<BlockFace>("painting_face", null);
	// Cmd block
	public static final DefaultedKey<String> COMMAND = new DefaultedKeyImpl<String>("command", null);
	public static final DefaultedKey<String> CHAT_CHANNEL = new DefaultedKeyImpl<String>("chat_channel", "CommandBlock");
	// Monster spawner
	public static final DefaultedKey<Integer> RADIUS = new DefaultedKeyImpl<Integer>("radius", 16);
	public static final DefaultedKey<Integer> CREATURE_TYPE = new DefaultedKeyImpl<Integer>("creature", CreatureType.PIG.getId());
	public static final DefaultedKey<Float> SPAWN_DELAY = new DefaultedKeyImpl<Float>("spawn_delay", 0f);
	public static final DefaultedKey<Integer> SPAWN_COUNT = new DefaultedKeyImpl<Integer>("spawn_count", 4);
	public static final DefaultedKey<Float> SPAWN_RANGE = new DefaultedKeyImpl<Float>("spawn_range", 4f);
	public static final DefaultedKey<Integer> MAX_CREATURES = new DefaultedKeyImpl<Integer>("max_creatures", 6);
	public static final DefaultedKey<Integer> CREATURE_SEARCH_RANGE = new DefaultedKeyImpl<Integer>("creature_search_range", 17);
	public static final DefaultedKey<Integer> MIN_SPAWN_DELAY = new DefaultedKeyImpl<Integer>("min_spawn_delay", 10);
	public static final DefaultedKey<Integer> MAX_SPAWN_DELAY = new DefaultedKeyImpl<Integer>("max_spawn_delay", 40);
	// Inventory
	public static final DefaultedKey<EntityArmorInventory> ARMOR_INVENTORY = new DefaultedKeyFactory<EntityArmorInventory>("armor", EntityArmorInventory.class);
	public static final DefaultedKey<EntityQuickbarInventory> ENTITY_HELD_INVENTORY = new DefaultedKeyFactory<EntityQuickbarInventory>("held", EntityQuickbarInventory.class);
	public static final DefaultedKey<PlayerMainInventory> MAIN_INVENTORY = new DefaultedKeyFactory<PlayerMainInventory>("main", PlayerMainInventory.class);
	public static final DefaultedKey<PlayerCraftingInventory> CRAFTING_INVENTORY = new DefaultedKeyFactory<PlayerCraftingInventory>("crafting", PlayerCraftingInventory.class);
	public static final DefaultedKey<PlayerQuickbar> QUICKBAR_INVENTORY = new DefaultedKeyFactory<PlayerQuickbar>("quickbar", PlayerQuickbar.class);
	public static final DefaultedKey<PlayerArmorInventory> PLAYER_ARMOR_INVENTORY = new DefaultedKeyFactory<PlayerArmorInventory>("armor", PlayerArmorInventory.class);
	public static final DefaultedKey<ChestInventory> ENDER_CHEST_INVENTORY = new DefaultedKeyFactory<ChestInventory>("ender_chest_inventory", ChestInventory.class);
	public static final DefaultedKey<DropInventory> DROP_INVENTORY = new DefaultedKeyFactory<DropInventory>("drops", DropInventory.class);
	public static final DefaultedKey<BrewingStandInventory> BREWING_STAND_INVENTORY = new DefaultedKeyFactory<BrewingStandInventory>("brewing_inventory", BrewingStandInventory.class);
	public static final DefaultedKey<DispenserInventory> DISPENSER_INVENTORY = new DefaultedKeyFactory<DispenserInventory>("dispenser_inventory", DispenserInventory.class);
	public static final DefaultedKey<HopperInventory> HOPPER_INVENTORY = new DefaultedKeyFactory<HopperInventory>("hopper_inventory", HopperInventory.class);
	public static final DefaultedKey<DropperInventory> DROPPER_INVENTORY = new DefaultedKeyFactory<DropperInventory>("dropper_inventory", DropperInventory.class);
	public static final DefaultedKey<VillagerInventory> VILLAGER_INVENTORY = new DefaultedKeyFactory<VillagerInventory>("villager_inventory", VillagerInventory.class);
	public static final DefaultedKey<HorseInventory> HORSE_INVENTORY = new DefaultedKeyFactory<HorseInventory>("horse_inventory", HorseInventory.class);
	// Team Data
	public static final DefaultedKey<Boolean> SEE_FRIENDLY_INVISIBLES = new DefaultedKeyImpl<Boolean>("see_friendly_invisibles", false);
	public static final DefaultedKey<Boolean> FRIENDLY_FIRE = new DefaultedKeyImpl<Boolean>("friendly_fire", false);
	public static final DefaultedKey<String> DISPLAY_NAME = new DefaultedKeyImpl<String>("display_name", "");
	public static final DefaultedKey<String> PREFIX = new DefaultedKeyImpl<String>("prefix", "");
	public static final DefaultedKey<String> SUFFIX = new DefaultedKeyImpl<String>("suffix", ChatStyle.RESET.toString());
}
