/*
 * This file is part of Vanilla.
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.material.item.data;

public class MapData extends ExtraData {

	private byte scale, dimension;
	private int xCenter, zCenter;
	private short height, width;
	private byte[] colors;

	public MapData(byte scale, byte dimension, short height, short width, int xCenter, int zCenter, byte[] colors) {
		this.scale = scale;
		this.dimension = dimension;
		this.height = height;
		this.width = width;
		this.xCenter = xCenter;
		this.zCenter = zCenter;
		this.colors = colors;
	}

	public MapData() {
		scale = -1;
		dimension = -1;
		height = -1;
		width = -1;
		xCenter = -1;
		zCenter = -1;
		colors = new byte[0];
	}

	@Override
	public String getName() {
		return "map";
	}

	public byte getScale() {
		return scale;
	}

	public byte getDimension() {
		return dimension;
	}

	public int getXCenter() {
		return xCenter;
	}

	public int getZCenter() {
		return zCenter;
	}

	public short getHeight() {
		return height;
	}

	public short getWidth() {
		return width;
	}

	public byte[] getColors() {
		return colors;
	}
}
