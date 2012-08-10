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
package org.spout.vanilla.data.effect;

import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.entity.Player;

public abstract class Effect {
	private final int range;

	public Effect(int range) {
		this.range = range;
	}

	/**
	 * Gets the Block range within this Effect is shown to players
	 * @return range
	 */
	public int getRange() {
		return this.range;
	}

	/**
	 * Gets all the Players nearby a certain Point that can receive this Effect
	 * @param position of this Effect
	 * @param ignore Entity to ignore
	 * @return a Set of nearby Players
	 */
	public Set<Player> getNearbyPlayers(Point position, Entity ignore) {
		return position.getWorld().getNearbyPlayers(position, ignore, this.getRange());
	}

	protected static int getMaxRange(Effect[] effects) {
		int range = 0;
		for (Effect effect : effects) {
			range = Math.max(range, effect.getRange());
		}
		return range;
	}

	public abstract void play(Player player, Point position);

	public void play(Set<Player> players, Point position) {
		for (Player player : players) {
			this.play(player, position);
		}
	}

	/**
	 * Plays the sound globally to everyone
	 * @param position to play at
	 */
	public void playGlobal(Point position) {
		this.playGlobal(position, null);
	}

	/**
	 * Plays the sound globally to everyone near except the Player Entity specified
	 * @param position to play at
	 * @param ignore Entity to ignore
	 */
	public void playGlobal(Point position, Entity ignore) {
		this.play(getNearbyPlayers(position, ignore), position);
	}
}
