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

import java.util.List;

import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.data.effect.Effect;
import org.spout.vanilla.data.effect.store.SoundEffects;

public class PressBlockEffect extends Effect {
	private static final int SOUND_RANGE = 16;

	public PressBlockEffect() {
		this(SOUND_RANGE);
	}

	public PressBlockEffect(int range) {
		super(range);
	}

	@Override
	public void play(Player player, Point position) {
		this.play(player, position, true);
	}

	public void play(Player player, Point position, boolean pressed) {
		SoundEffects.RANDOM_CLICK.play(player, position, 0.3f, pressed ? 0.6f : 0.5f);
	}

	public void play(List<Player> players, Point position, boolean pressed) {
		for (Player player : players) {
			this.play(player, position, pressed);
		}
	}

	public void playGlobal(Point position, boolean pressed) {
		this.playGlobal(position, pressed, null);
	}

	public void playGlobal(Point position, boolean pressed, Player ignore) {
		this.play(getNearbyPlayers(position, ignore), position, pressed);
	}
}
