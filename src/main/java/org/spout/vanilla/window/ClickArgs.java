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

/**
 * Stores information of a Window item click event
 */
public class ClickArgs {
	private final boolean rightClick, shift;
	public ClickArgs(boolean rightClick, boolean shift) {
		this.rightClick = rightClick;
		this.shift = shift;
	}

	/**
	 * Gets whether it was a right mouse click
	 * 
	 * @return True if it was a right click, False if not
	 */
	public boolean isRightClick() {
		return this.rightClick;
	}

	/**
	 * Gets whether it was a left mouse click
	 * 
	 * @return True if it was a left click, False if not
	 */
	public boolean isLeftClick() {
		return !this.rightClick;
	}

	/**
	 * Gets whether shift was down while clicking
	 * 
	 * @return True if shift was down, False if not
	 */
	public boolean isShiftDown() {
		return this.shift;
	}
}
