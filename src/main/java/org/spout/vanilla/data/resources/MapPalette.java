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
package org.spout.vanilla.data.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.IOUtils;

import org.spout.api.math.GenericMath;
import org.spout.api.resource.Resource;

import org.spout.vanilla.material.map.MapColor;
import org.spout.vanilla.material.map.MapMaterialColor;

public class MapPalette extends Resource {
	public static MapPalette DEFAULT;
	private final byte[] colorPalette = new byte[128 * 256 * 256];

	private int getPaletteKey(int r, int g, int b) {
		return ((r & 0xFF) << 15) | ((g & 0xFF) << 7) | (b & 0x7F);
	}

	public void read(InputStream stream) throws IOException {
		IOUtils.readFully(stream, colorPalette);
	}

	public void write(OutputStream stream) throws IOException {
		stream.write(colorPalette);
	}

	/**
	 * Tries to save this Map Palette to file
	 * @param file to save to
	 * @return True if it was successful, False if not
	 */
	public boolean save(File file) {
		try {
			GZIPOutputStream stream = new GZIPOutputStream(new FileOutputStream(file));
			try {
				this.write(stream);
				return true;
			} finally {
				stream.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the map color nearest to the RGB color specified
	 * @param r component of the color (0 - 255)
	 * @param g component of the color (0 - 255)
	 * @param b component of the color (0 - 255)
	 * @return the MapColor nearest to the color
	 */
	public MapColor getColor(int r, int g, int b) {
		byte color = colorPalette[getPaletteKey(r, g, b)];
		if ((b & 0x80) == 0x80) {
			return MapColor.getById(color & 0x3F);
		} else {
			return MapColor.getById(color >> 4);
		}
	}

	/**
	 * Gets the map color nearest to the RGB color specified<br>
	 * Note: This is a slow function
	 * @param r component of the color (0F - 1F)
	 * @param g component of the color (0F - 1F)
	 * @param b component of the color (0F - 1F)
	 * @return the MapColor nearest to the color
	 */
	public MapColor getColor(float r, float g, float b) {
		return this.getColor((int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

	/**
	 * Sets the map color nearest for the RGB color specified
	 * @param r component of the color (0 - 255)
	 * @param g component of the color (0 - 255)
	 * @param b component of the color (0 - 255)
	 * @param color to set to
	 */
	public void setColor(int r, int g, int b, MapColor color) {
		// Set in palette
		if ((b & 0x80) == 0x80) {
			colorPalette[getPaletteKey(r, g, b)] |= color.getId();
		} else {
			colorPalette[getPaletteKey(r, g, b)] |= color.getId() << 4;
		}
	}

	/**
	 * Generates the default nearest-color palette
	 * @return default map palette
	 */
	public static MapPalette generateDefault() {
		MapPalette palette = new MapPalette();

		// Generate new Color Palette
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
					for (MapColor color : MapColor.values()) {
						if (color.getBase() == MapMaterialColor.TRANSPARENT) {
							continue;
						}
						diff = GenericMath.lengthSquared(color.getRed() - rf, color.getGreen() - gf, color.getBlue() - bf);
						if (diff < nearestDiff) {
							nearestDiff = diff;
							nearest = color;
							if (diff <= 0.02f) {
								break;
							}
						}
					}
					// Set in palette
					palette.setColor(r, g, b, nearest);
				}
			}
		}
		return palette;
	}
}
