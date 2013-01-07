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
package org.spout.vanilla.plugin.material.map;

public class MapColor {
	private static final MapColor[] colors;

	static {
		// Generate map colors out of material colors
		MapMaterialColor[] materialColors = MapMaterialColor.values();
		colors = new MapColor[materialColors.length * 4];
		int id = 0;
		for (MapMaterialColor color : materialColors) {
			color.levels[3] = colors[id] = new MapColor(id++, color, 180);
			color.levels[1] = colors[id] = new MapColor(id++, color, 220);
			color.levels[0] = colors[id] = new MapColor(id++, color, 255);
			color.levels[2] = colors[id] = new MapColor(id++, color, 220);
		}
	}

	public static MapColor[] values() {
		return colors;
	}

	/**
	 * Gets a map color by color ID
	 * @param id of the color
	 * @return Map Color
	 */
	public static MapColor getById(int id) {
		return colors[id];
	}

	private final float r, g, b;
	private final MapMaterialColor base;
	private final byte id;

	private MapColor(int id, MapMaterialColor base, int factor) {
		this.id = (byte) id;
		this.base = base;
		this.r = base.getRed() * ((float) factor / 255F);
		this.g = base.getGreen() * ((float) factor / 255F);
		this.b = base.getBlue() * ((float) factor / 255F);
	}

	/**
	 * Gets the base map material color
	 * @return the base map material color
	 */
	public MapMaterialColor getBase() {
		return this.base;
	}

	/**
	 * Gets the red component
	 * @return the red component (0F - 1F)
	 */
	public float getRed() {
		return this.r;
	}

	/**
	 * Gets the green component
	 * @return the green component (0F - 1F)
	 */
	public float getGreen() {
		return this.g;
	}

	/**
	 * Gets the blue component
	 * @return the blue component (0F - 1F)
	 */
	public float getBlue() {
		return this.b;
	}

	/**
	 * Gets the ID of this map color
	 * @return the id
	 */
	public byte getId() {
		return this.id;
	}
}
