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
package org.spout.vanilla.map;

public enum MapMaterialColor {
	TRANSPARENT(0, 0, 0),
	GRASS(127, 178, 56),
	SAND(247, 233, 163),
	OTHER(167, 167, 167),
	LAVA(255, 0, 0),
	ICE(160, 160, 255),
	METAL(167, 167, 167),
	PLANTS(0, 124, 0),
	SNOW(255, 255, 255),
	CLAY(164, 168, 184),
	DIRT(183, 106, 47),
	STONE(112, 112, 112),
	WATER(64, 64, 255),
	WOOD(104, 83, 50);
	private final float r, g, b;
	protected final MapColor[] levels;

	private MapMaterialColor(int r, int g, int b) {
		this.r = (float) r / 255F;
		this.g = (float) g / 255F;
		this.b = (float) b / 255F;
		this.levels = new MapColor[4];
	}

	/**
	 * Gets the first level shaded map color
	 * @return level 1 map color
	 */
	public MapColor getLevel1() {
		return this.levels[0];
	}

	/**
	 * Gets the second level shaded map color
	 * @return level 2 map color
	 */
	public MapColor getLevel2() {
		return this.levels[1];
	}

	/**
	 * Gets the third level shaded map color
	 * @return level 3 map color
	 */
	public MapColor getLevel3() {
		return this.levels[2];
	}

	/**
	 * Gets the fourth level shaded map color
	 * @return level 4 map color
	 */
	public MapColor getLevel4() {
		return this.levels[3];
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
	 * Gets the ID of this material map color
	 * @return the id
	 */
	public byte getId() {
		return (byte) this.ordinal();
	}
}
