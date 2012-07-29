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
package org.spout.vanilla.util.sound;

import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

/**
 * A Vanilla sound that can be sent over the network
 */
public interface VanillaSound {

	/**
	 * Plays the sound to all players near the position
	 * 
	 * @param position to play at
	 */
	public void playGlobal(Point position);

	/**
	 * Plays the sound to all players near the position
	 * 
	 * @param position to play at
	 * @param volume to play at
	 * @param pitch to play at
	 */
	public void playGlobal(Point position, float volume, float pitch);

	/**
	 * Plays the sound to all players near the position
	 * 
	 * @param position to play at
	 * @param volume to play at
	 * @param pitch to play at
	 * @param range to play in
	 */
	public void playGlobal(Point position, float volume, float pitch, int range);

	/**
	 * Plays the sound for the player at the position specified
	 * 
	 * @param player to play the sound for
	 * @param position in the world to play at
	 */
	public void play(Player player, Vector3 position);

	/**
	 * Plays the sound for the player at the position specified
	 * 
	 * @param player to play the sound for
	 * @param position in the world to play at
	 * @param volume to play at
	 * @param pitch to play at
	 */
	public void play(Player player, Vector3 position, float volume, float pitch);
}
