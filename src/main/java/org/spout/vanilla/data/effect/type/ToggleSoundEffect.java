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
import org.spout.vanilla.data.effect.store.GeneralEffects;

public class ToggleSoundEffect extends SoundEffect {
	private final SoundEffect open, close;

	public ToggleSoundEffect(SoundEffect open, SoundEffect close) {
		super(open.getName());
		this.open = open;
		this.close = close;
	}

	@Override
	public ToggleSoundEffect adjust(float volume, float pitch) {
		return new ToggleSoundEffect(this.open.adjust(volume, pitch), this.close.adjust(volume, pitch));
	}

	@Override
	public ToggleSoundEffect randomPitch(float amount) {
		return new ToggleSoundEffect(this.open.randomPitch(amount), this.close.randomPitch(amount));
	}

	@Override
	public void play(Player player, Point position) {
		GeneralEffects.RANDOM_DOOR.play(player, position);
	}

	public void play(Player player, Point position, boolean open) {
		(open ? this.open : this.close).play(player, position);
	}

	public void play(Collection<Player> players, Point position, boolean open) {
		for (Player player : players) {
			this.play(player, position, open);
		}
	}

	public void playGlobal(Point position, boolean open) {
		this.playGlobal(position, open, null);
	}

	public void playGlobal(Point position, boolean open, Player ignore) {
		this.play(getNearbyPlayers(position, ignore), position, open);
	}
}
