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
package org.spout.vanilla.inventory.window;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Represents a type of {@link org.spout.vanilla.component.inventory.window.Window}
 */
public enum WindowType {
	/**
	 * The default window seen when the player opens his inventory.
	 */
	DEFAULT(-1),
	/**
	 * The window seen when opening a
	 * {@link org.spout.vanilla.component.substance.material.Chest}
	 */
	CHEST(0),
	/**
	 * The window seen when using a
	 * {@link org.spout.vanilla.component.substance.material.CraftingTable}
	 */
	CRAFTING_TABLE(1),
	/**
	 * The window seen when opening a
	 * {@link org.spout.vanilla.component.substance.material.Furnace}
	 */
	FURNACE(2),
	/**
	 * The window seen when opening a
	 * {@link org.spout.vanilla.component.substance.material.Dispenser}
	 */
	DISPENSER(3),
	/**
	 * The window seen when using an
	 * {@link org.spout.vanilla.component.substance.material.EnchantmentTable}
	 */
	ENCHANTMENT_TABLE(4),
	/**
	 * The window seen when using a
	 * {@link org.spout.vanilla.component.substance.material.BrewingStand}
	 */
	BREWING_STAND(5),
	/**
	 * The window seen when trading with a
	 * {@link org.spout.vanilla.component.living.passive.Villager}
	 */
	VILLAGER(6);
	private final int id;
	private static final TIntObjectMap<WindowType> idMap = new TIntObjectHashMap<WindowType>();

	private WindowType(int id) {
		this.id = id;
	}

	/**
	 * Returns the id sent to the client in the
	 * {@link org.spout.vanilla.protocol.msg.window.WindowOpenMessage}
	 * @return id of type
	 */
	public int getId() {
		return id;
	}

	static {
		for (WindowType type : WindowType.values()) {
			idMap.put(type.getId(), type);
		}
	}

	/**
	 * Returns a window type with the specified id.
	 * @param id of window type
	 * @return window type with specified id
	 */
	public static WindowType get(int id) {
		return idMap.get(id);
	}
}
