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
package org.spout.vanilla.data;

import java.util.UUID;

import org.spout.api.inventory.ItemStack;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.math.Vector3;

/**
 * * Common Vanilla-like data mappings that are found in Vanilla.
 */
public class VanillaData {
	//World-specific
	public static final DefaultedKey<Dimension> DIMENSION = new DefaultedKeyImpl<Dimension>("dimension", Dimension.NORMAL);
	public static final DefaultedKey<Difficulty> DIFFICULTY = new DefaultedKeyImpl<Difficulty>("difficulty", Difficulty.EASY);
	public static final DefaultedKey<Weather> WEATHER = new DefaultedKeyImpl<Weather>("weather", Weather.CLEAR);
	public static final DefaultedKey<WorldType> WORLD_TYPE = new DefaultedKeyImpl<WorldType>("world_type", WorldType.DEFAULT);
	/**
	 * World link recalls a stored UUID which would be 'linked' to this world. Linking means the following...
	 * - Age
	 * - Seed
	 * - Portals from the world with the stored UUID would go to the UUID recalled (if applicable, cases such as nethers).
	 * If the value is the default (null) then it means this world has no link to it.
	 */
	public static final DefaultedKey<UUID> WORLD_LINK = new DefaultedKeyImpl<UUID>("world_link", null);
	//Player-specific
	public static final DefaultedKey<GameMode> GAMEMODE = new DefaultedKeyImpl<GameMode>("game_mode", GameMode.SURVIVAL);
	//Controller-specific
	public static final DefaultedKey<Integer> AIR_TICKS = new DefaultedKeyImpl<Integer>("air_ticks", 0);
	public static final DefaultedKey<Integer> CONTROLLER_TYPE = new DefaultedKeyImpl<Integer>("controller_id", 0);
	public static final DefaultedKey<Integer> FIRE_TICKS = new DefaultedKeyImpl<Integer>("fire_ticks", 0);
	public static final DefaultedKey<Long> GROWTH_TICKS = new DefaultedKeyImpl<Long>("growth_ticks", Long.valueOf(0));
	public static final DefaultedKey<Boolean> FLAMMABLE = new DefaultedKeyImpl<Boolean>("flammable", false);
	public static final DefaultedKey<Integer> HEALTH = new DefaultedKeyImpl<Integer>("health", 1);
	public static final DefaultedKey<Integer> MAX_HEALTH = new DefaultedKeyImpl<Integer>("max_health", 1);
	public static final DefaultedKey<Vector3> MAX_SPEED = new DefaultedKeyImpl<Vector3>("max_speed", Vector3.ZERO);
	public static final DefaultedKey<Vector3> MOVEMENT_SPEED = new DefaultedKeyImpl<Vector3>("movement_speed", Vector3.ZERO);
	public static final DefaultedKey<Vector3> VELOCITY = new DefaultedKeyImpl<Vector3>("velocity", Vector3.ZERO);
	public static final DefaultedKey<ItemStack[]> ITEMS = new DefaultedKeyImpl<ItemStack[]>("items", new ItemStack[36]);
	//Creature-specific
	public static final DefaultedKey<Integer> LINE_OF_SIGHT = new DefaultedKeyImpl<Integer>("line_of_sight", 1);
	//Item-specific
	public static final DefaultedKey<Integer> UNCOLLECTABLE_TICKS = new DefaultedKeyImpl<Integer>("uncollectable_ticks", 10);
	//XPOrb-specific
	public static final DefaultedKey<Short> EXPERIENCE_AMOUNT = new DefaultedKeyImpl<Short>("experience_amount", (short) 0);
	public static final DefaultedKey<Long> TIME_DISPERSED = new DefaultedKeyImpl<Long>("time_dispersed", Long.valueOf(0));
	//Slime-specific
	public static final DefaultedKey<Byte> SLIME_SIZE = new SlimeSize("slime_size");
}
