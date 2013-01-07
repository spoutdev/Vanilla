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
package org.spout.vanilla.plugin.event.window;

import org.spout.api.event.HandlerList;

import org.spout.vanilla.plugin.inventory.window.Window;

public class WindowPropertyEvent extends WindowEvent {
	private static HandlerList handlers = new HandlerList();
	private int id;
	private int value;

	public WindowPropertyEvent(Window window, int id, int value) {
		super(window);
		this.id = id;
		this.value = value;
	}

	/**
	 * Gets the id of the Window property that got changed
	 * @return property Id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the value the property got set to
	 * @return the new property value
	 */
	public int getValue() {
		return this.value;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
