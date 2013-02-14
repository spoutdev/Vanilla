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
package org.spout.vanilla.api.enchantment;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to register and access enchantments
 */
public class EnchantmentRegistrar {
	private static final Map<Integer, Enchantment> idLookup = new HashMap<Integer, Enchantment>();
	private static final Map<String, Enchantment> nameLookup = new HashMap<String, Enchantment>();

	/**
	 * Registers the given enchantment
	 * @param enchantment Enchantment to register
	 * @return Registered enchantment
	 */
	public static <T extends Enchantment> T register(T enchantment) {
		if (idLookup.containsKey(enchantment.getId())) {
			throw new IllegalArgumentException("Enchantment ID '" + enchantment.getId() + "' has already been registered!");
		}

		idLookup.put(enchantment.getId(), enchantment);
		nameLookup.put(enchantment.getName().toLowerCase(), enchantment);
		return enchantment;
	}

	/**
	 * Gets an enchantment with the given id
	 * @param id id of the enchantment
	 * @return Enchantment with the given id, or null if not found
	 */
	public static Enchantment getById(int id) {
		return idLookup.get(id);
	}

	/**
	 * Gets an enchantment with the given name (case-insensitive)
	 * @param name Name of the enchantment
	 * @return Enchantment with the given name, or null if not found
	 */
	public static Enchantment getByName(String name) {
		return nameLookup.get(name.toLowerCase());
	}

	/**
	 * Gets all registered Enchantments
	 * @return Array of enchantments
	 */
	public static Enchantment[] values() {
		Enchantment[] values = new Enchantment[idLookup.size()];
		return idLookup.values().toArray(values);
	}
}
