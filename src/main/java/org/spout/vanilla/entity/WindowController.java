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
package org.spout.vanilla.entity;

import java.util.Collection;

import org.spout.vanilla.window.Window;

/**
 * Defines a entity that can open and close windows for players
 */
public interface WindowController extends VanillaController {
	/**
	 * Opens the window for the player specified
	 * @param player to open it for
	 */
	public boolean open(VanillaPlayerController player);

	/**
	 * Closes an opened window for the player specified
	 * @param player to close it for
	 */
	public boolean close(VanillaPlayerController player);

	/**
	 * Closes all the windows players have for this entity
	 */
	public void closeAll();

	/**
	 * Removes a viewer from this entity<br>
	 * Does not create close a Window, use close instead
	 * @param player that viewed
	 * @return window that the player viewed, ur null if not contained
	 */
	public Window removeViewer(VanillaPlayerController player);

	/**
	 * Adds a new viewer to this entity<br>
	 * Does not create a new Window, use open instead
	 * @param player that views
	 * @param window that the player views
	 */
	public void addViewer(VanillaPlayerController player, Window window);

	/**
	 * Gets a collection of viewers currently using this entity
	 * @return a collection of players viewing this entity
	 */
	public Collection<VanillaPlayerController> getViewers();

	/**
	 * Checks if this entity has viewers
	 * @return True if it has viewers, False if not
	 */
	public boolean hasViewers();
}
