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
package org.spout.vanilla.data.effect.type;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.data.effect.Effect;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.data.effect.store.SoundEffects;

public class DoorEffect extends Effect {
	private static final int DOOR_RANGE = 16;

	public DoorEffect() {
		super(DOOR_RANGE);
	}

	@Override
	public void play(Player player, Point position) {
		GeneralEffects.RANDOM_DOOR.play(player, position);
	}

	public void play(Player player, Point position, boolean open) {
		if (open) {
			SoundEffects.RANDOM_DOOR_OPEN.play(player, position);
		} else {
			SoundEffects.RANDOM_DOOR_CLOSE.play(player, position);
		}
	}

	public void play(List<Player> players, Point position, boolean open) {
		for (Player player : players) {
			this.play(player, position, open);
		}
	}

	public void playGlobal(Point position, boolean open) {
		this.playGlobal(position, open, null);
	}

	public void playGlobal(Point position, boolean open, Entity ignore) {
		this.play(getNearbyPlayers(position, ignore), position, open);
	}
}
