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
package org.spout.vanilla.world;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the difficulty of a world.
 */
public enum Difficulty {
	PEACEFUL(0),
	EASY(1),
	NORMAL(2),
	HARD(3);

	private static final Map<Integer, Difficulty> idMap = new HashMap<Integer, Difficulty>();
	private static final Map<String, Difficulty> nameMap = new HashMap<String, Difficulty>();
	private final int id;

	private Difficulty(int id) {
		this.id = id;
	}

	/**
	 * Returns the ID of this difficulty.
	 * @return ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the Difficulty with the given ID.
	 * @param id ID to check
	 * @return Difficulty with the given ID, or null if no difficulty has the ID
	 */
	public static Difficulty getById(int id) {
		return idMap.get(id);
	}

	/**
	 * Returns the Difficulty with the given name (case-insensitive).
	 * @param name Name to check
	 * @return Difficulty with the given name, or null if no difficulty has the name
	 */
	public static Difficulty getByName(String name) {
		return nameMap.get(name.toLowerCase());
	}

	static {
		for (Difficulty mode : Difficulty.values()) {
			idMap.put(mode.getId(), mode);
			nameMap.put(mode.name().toLowerCase(), mode);
		}
	}
}
