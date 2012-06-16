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
package org.spout.vanilla.controller;

import java.util.Collection;

import org.spout.vanilla.controller.living.player.VanillaPlayer;

/**
 * Defines a controller that can open and close windows for players
 */
public interface WindowOwner {

	/**
	 * Opens the window for the player specified
	 * @param player to open it for
	 */
	public boolean open(VanillaPlayer player);

	/**
	 * Closes an opened window for the player specified
	 * @param player to close it for
	 */
	public boolean close(VanillaPlayer player);

	/**
	 * Closes all the windows players have for this controller
	 */
	public void closeAll();

	/**
	 * Gets a collection of viewers currently using this controller
	 * @return a collection of players viewing this controller
	 */
	public Collection<VanillaPlayer> getViewers();

	/**
	 * Checks if this controller has viewers
	 * @return True if it has viewers, False if not
	 */
	public boolean hasViewers();
}
