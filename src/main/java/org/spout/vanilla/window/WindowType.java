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
package org.spout.vanilla.window;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Available Window Types in the Notchian Minecraft client
 */
public enum WindowType {
	DEFAULT(-1),
	CHEST(0),
	CRAFTINGTABLE(1),
	FURNACE(2),
	DISPENSER(3),
	ENCHANTMENTTABLE(4),
	BREWINGSTAND(5);

	private static final TIntObjectHashMap<WindowType> values = new TIntObjectHashMap<WindowType>();
	static {
		for (WindowType type : values()) {
			values.put(type.getId(), type);
		}
	}

	private final int id;
	private WindowType(int id) {
		this.id = id;
	}

	/**
	 * Gets the type Id of this Window Type
	 * 
	 * @return Type Id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets a certain Window Type by Id
	 * 
	 * @param id of the type
	 * @return Window Type, or null if not found
	 */
	public static WindowType getById(int id) {
		return values.get(id);
	}
}
