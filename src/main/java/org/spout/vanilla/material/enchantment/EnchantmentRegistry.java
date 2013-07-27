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
package org.spout.vanilla.material.enchantment;

import java.util.HashMap;
import java.util.Map;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Used to register and access enchantments
 */
public class EnchantmentRegistry {
	private static final TIntObjectMap<Enchantment> BY_ID = new TIntObjectHashMap<Enchantment>();
	private static final Map<String, Enchantment> BY_NAME = new HashMap<String, Enchantment>();

	/**
	 * Registers the given enchantment
	 * @param enchantment Enchantment to register
	 * @return Registered enchantment
	 */
	public static <T extends Enchantment> T register(T enchantment) {
		if (BY_ID.containsKey(enchantment.getId())) {
			throw new IllegalArgumentException("Enchantment ID '" + enchantment.getId() + "' has already been registered!");
		}

		BY_ID.put(enchantment.getId(), enchantment);
		BY_NAME.put(enchantment.getName().toLowerCase(), enchantment);
		return enchantment;
	}

	/**
	 * Gets an enchantment with the given id
	 * @param id id of the enchantment
	 * @return Enchantment with the given id, or null if not found
	 */
	public static Enchantment getById(int id) {
		return BY_ID.get(id);
	}

	/**
	 * Gets an enchantment with the given name (case-insensitive)
	 * @param name Name of the enchantment
	 * @return Enchantment with the given name, or null if not found
	 */
	public static Enchantment getByName(String name) {
		return BY_NAME.get(name.toLowerCase());
	}

	/**
	 * Gets all registered Enchantments
	 * @return Array of enchantments
	 */
	public static Enchantment[] values() {
		return BY_ID.values(new Enchantment[BY_ID.size()]);
	}
}
