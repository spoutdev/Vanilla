/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.entity;

import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.protocol.reposition.RepositionManager;

/**
 * Human Entity Protocol extension that adds methods for spawning a Player Human
 * to the Player itself. This involves sending information such as Player inventory,
 * player list information and other information players can only see for themselves.
 */
public class PlayerEntityProtocol extends HumanEntityProtocol {

	/**
	 * Sends the messages to the Player (self) when destroying.
	 * This typically is used to clear Player information when changing worlds.
	 * This method should return all messages needed so that no glitched information
	 * stays behind when sending the spawn messages again later on.
	 * 
	 * @param player to be destroyed
	 */
	public void doSelfDestroy(Player player) {
	}

	/**
	 * Sends the messages to the Player (self) when spawning.
	 * The entity should spawn at the location at the last snapshot.
	 * 
	 * @param player to spawn
	 * @param rm to use for positioning adjustments
	 */
	public void doSelfSpawn(Player player, RepositionManager rm) {
		// This is for debugging the 'double-spawn' issue when players rejoin the server
		// When fixed this log line can be removed again
		System.out.println("SPAWNING PLAYER");
	}

	/**
	 * Sends the messages to update the Player to itself.
	 * This should move the entity from its snapshot position to its live position.
	 *
	 * @param player to update
	 * @param liveTransform the latest transform for the entity
	 * @param rm the reposition manager
	 * @param force true to send an absolute update even if no update is required
	 */
	public void doSelfUpdate(Player player, Transform liveTransform, RepositionManager rm, boolean force) {
	}
}
