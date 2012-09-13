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

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public enum WindowType {
	DEFAULT(-1),
	CHEST(0),
	CRAFTING_TABLE(1),
	FURNACE(2),
	DISPENSER(3),
	ENCHANTMENT_TABLE(4),
	BREWING_STAND(5),
	VILLAGER(6);

	private final int id;
	private static final TIntObjectMap<WindowType> idMap = new TIntObjectHashMap<WindowType>();

	private WindowType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	static {
		for (WindowType type : WindowType.values()) {
			idMap.put(type.getId(), type);
		}
	}

	public static WindowType get(int id) {
		return idMap.get(id);
	}
}
