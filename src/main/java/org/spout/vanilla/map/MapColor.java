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

import org.spout.api.math.MathHelper;

public class MapColor {
	private static final MapColor[] colors;
	private static final byte[] colorPalette;

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

		// Generate Color Palette
		colorPalette = new byte[128 * 256 * 256];
		int r, g, b;
		float rf, gf, bf;
		double diff, nearestDiff;
		MapColor nearest = null;
		for (r = 0; r < 256; r++) {
			rf = (float) r / 255f;
			for (g = 0; g < 256; g++) {
				gf = (float) g / 255f;
				for (b = 0; b < 256; b++) {
					bf = (float) b / 255f;

					// Obtain nearest map color
					nearestDiff = Double.MAX_VALUE;
					for (MapColor color : colors) {
						if (color.getBase() == MapMaterialColor.TRANSPARENT) {
							continue;
						}
						diff = MathHelper.lengthSquared(color.r - rf, color.g - gf, color.b - bf);
						if (diff < nearestDiff) {
							nearestDiff = diff;
							nearest = color;
							if (diff <= 0.02f) {
								break;
							}
						}
					}
					// Set in palette
					if ((b & 0x80) == 0x80) {
						colorPalette[getPaletteKey(r, g, b)] |= nearest.getId();
					} else {
						colorPalette[getPaletteKey(r, g, b)] |= nearest.getId() << 4;
					}
				}
			}
		}
	}

	public static MapColor[] values() {
		return colors;
	}

	private static int getPaletteKey(int r, int g, int b) {
		return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0x7F);
	}

	/**
	 * Gets the map color nearest to the RGB color specified<br>
	 * Note: This is a slow function
	 * @param r component of the color (0F - 1F)
	 * @param g component of the color (0F - 1F)
	 * @param b component of the color (0F - 1F)
	 * @return the MapColor nearest to the color
	 */
	public static MapColor getByRGB(float r, float g, float b) {
		return getByRGB((int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

	/**
	 * Gets the map color nearest to the RGB color specified
	 * @param r component of the color (0 - 255)
	 * @param g component of the color (0 - 255)
	 * @param b component of the color (0 - 255)
	 * @return the MapColor nearest to the color
	 */
	public static MapColor getByRGB(int r, int g, int b) {
		byte color = colorPalette[getPaletteKey(r, g, b)];
		if ((b & 0x80) == 0x80) {
			return getById(color & 0x3F);
		} else {
			return getById(color >> 4);
		}
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
