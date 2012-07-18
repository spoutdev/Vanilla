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

public class Map {
	private final short width, height;
	private final byte[] colors;
	private final int id;
	private boolean changed = false;

	public Map(int width, int height) {
		this.width = (short) width;
		this.height = (short) height;
		this.colors = new byte[128 * 128];
		this.id = 0;
	}

	/**
	 * Gets the Instance Id of this Map
	 * 
	 * @return Map Instance Id
	 */
	public int getInstanceId() {
		return this.id;
	}

	/**
	 * Gets the width dimension of this map
	 * @return the width dimension
	 */
	public short getWidth() {
		return this.width;
	}

	/**
	 * Gets the height dimension of this map
	 * @return the height dimension
	 */
	public short getHeight() {
		return this.height;
	}

	/**
	 * Sets whether this map is changed
	 * @param changed state to set to
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * Gets whether this map got changed
	 * @return True if it got changed, False if not
	 */
	public boolean isChanged() {
		return this.changed;
	}

	/**
	 * Sets the map color at the coordinates to the map color specified
	 * @param x coordinate
	 * @param y coordinate
	 * @param color to set to
	 */
	public void setColor(int x, int y, MapColor color) {
		this.colors[x + y * this.width] = (byte) color.getId();
		this.changed = true;
	}

	/**
	 * Gets the map color at the coordinates specified
	 * @param x coordinate
	 * @param y coordinate
	 * @return the map color
	 */
	public MapColor getColor(int x, int y) {
		return MapColor.getById(this.colors[x + y * this.width]);
	}
}
