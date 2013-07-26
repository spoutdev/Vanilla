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
package org.spout.vanilla.material.map;

import java.util.ArrayList;
import java.util.List;
import org.spout.api.event.ProtocolEvent;

import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Vector2;

import org.spout.vanilla.event.item.MapItemUpdateEvent;
import org.spout.vanilla.material.item.VanillaItemMaterial;

public class Map extends VanillaItemMaterial {
	private final short width, height;
	private final byte[] colors;
	private final int id;
	private boolean changed = false;

	public Map(String name, int id, int width, int height, Vector2 pos) {
		super(name, id, null);
		this.width = (short) width;
		this.height = (short) height;
		this.colors = new byte[128 * 128];
		this.id = 0;
	}

	/**
	 * Gets the Instance Id of this Map
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
		this.colors[x + y * this.width] = color.getId();
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

	public List<ProtocolEvent> drawRectangle(ItemStack item, int bx, int by, int tx, int ty, int col) {
		if (bx < 0 || tx >= getWidth() || by < 0 || ty >= getHeight() || bx > tx || by > ty) {
			throw new IllegalArgumentException("Rectangle out of range");
		}
		List<ProtocolEvent> events = new ArrayList<ProtocolEvent>();
		for (int x = bx; x < tx; x++) {
			byte[] data = new byte[4 + ty - by];
			int i = 0;
			data[i++] = 0;
			data[i++] = (byte) x;
			data[i++] = (byte) by;
			for (int y = by; y <= ty; y++) {
				data[i++] = (byte) col;
			}
			events.add(new MapItemUpdateEvent(x, by, item.getData(), data));
		}
		return events;
	}

	public List<ProtocolEvent> flood(ItemStack item, int col) {
		return drawRectangle(item, 0, 0, getWidth() - 1, getHeight() - 1, col);
	}
}
