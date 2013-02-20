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
package org.spout.vanilla.data.effect.type;

import java.util.Collection;

import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.data.effect.SoundEffect;

public class NoteSoundEffect extends SoundEffect {
	public NoteSoundEffect(String name) {
		super(name, 3.0f, 0.5f);
	}

	public void play(Player player, Point position, int tone) {
		this.play(player, position, this.getDefaultVolume(), tone);
	}

	public void play(Player player, Point position, float volume, int tone) {
		// calculate pitch
		float pitch = (float) Math.pow(2.0, (double) (tone - 12) / 12.0);
		this.play(player, position, volume, pitch);
	}

	public void play(Collection<Player> players, Point position, int tone) {
		this.play(players, position, this.getDefaultVolume(), tone);
	}

	public void play(Collection<Player> players, Point position, float volume, int tone) {
		for (Player player : players) {
			this.play(player, position, volume, tone);
		}
	}

	public void playGlobal(Point position, int tone) {
		this.playGlobal(position, null, this.getDefaultVolume(), tone);
	}

	public void playGlobal(Point position, float volume, int tone) {
		this.play(getNearbyPlayers(position, null, volume), position, volume, tone);
	}

	public void playGlobal(Point position, Player ignore, int tone) {
		this.playGlobal(position, ignore, this.getDefaultVolume(), tone);
	}

	public void playGlobal(Point position, Player ignore, float volume, int tone) {
		this.play(getNearbyPlayers(position, ignore, volume), position, volume, tone);
	}
}
