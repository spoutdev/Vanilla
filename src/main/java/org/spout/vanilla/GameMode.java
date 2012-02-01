/*
 * This file is part of Vanilla (http://www.spout.org/).
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
package org.spout.vanilla;

/**
 * Is a representation of the different game modes that {@link org.spout.vanilla.entity.living.player.MinecraftPlayer}s may
 * have. <br/> {@link GameMode#CREATIVE} implies that the player has no restrictions
 * on flight, has infinite health, instant break, and can use any blocks.
 * {@link GameMode#SURVIVAL} implies that the player is subject to standard game
 * rules for health, movement, and inventory.
 */
public enum GameMode {
	/**
	 * Creative mode has no restrictions on flight, can build and break
	 * instantly, is invulnerable and can use any blocks.
	 */
	CREATIVE(1),

	/**
	 * Survival mode is subject to standard game rules for health, movement, and
	 * inventory.
	 */
	SURVIVAL(0);

	private final byte id;

	private GameMode(final int value) {
		id = (byte) value;
	}

	/**
	 * Gets the id for this GameMode
	 *
	 * @return GameMode id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the GameMode represented by the specified value
	 *
	 * @param id Value to check
	 * @return Associative {@link GameMode} with the given value, or null if it
	 *         doesn't exist
	 */
	public static GameMode getByValue(final int id) {
		switch (id) {
			case 0:
				return GameMode.SURVIVAL;
			case 1:
				return GameMode.CREATIVE;
			default:
				return null;
		}
	}
}