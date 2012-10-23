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
package org.spout.vanilla.inventory.window.prop;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Represents a property of
 * {@link org.spout.vanilla.inventory.window.WindowType#FURNACE}
 */
public enum FurnaceProperty implements WindowProperty {
	/**
	 * The value of the progress arrow on the furnace.
	 */
	PROGRESS_ARROW(0),
	/**
	 * The value of the fire icon on the furnace.
	 */
	FIRE_ICON(1);
	private final int id;
	private static final TIntObjectMap<FurnaceProperty> idMap = new TIntObjectHashMap<FurnaceProperty>(FurnaceProperty.values().length);

	private FurnaceProperty(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	static {
		for (FurnaceProperty prop : FurnaceProperty.values()) {
			idMap.put(prop.getId(), prop);
		}
	}

	/**
	 * Returns the property from the specified id.
	 * @param id of property
	 * @return property with specified id
	 */
	public static FurnaceProperty get(int id) {
		return idMap.get(id);
	}
}
