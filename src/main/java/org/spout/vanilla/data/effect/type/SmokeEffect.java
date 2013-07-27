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
package org.spout.vanilla.data.effect.type;

import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.effect.GeneralEffect;

public class SmokeEffect extends GeneralEffect {
	private static final int SMOKE_NORTH_EAST = 0;
	private static final int SMOKE_EAST = 1;
	private static final int SMOKE_SOUTH_EAST = 2;
	private static final int SMOKE_NORTH = 3;
	private static final int SMOKE_MIDDLE = 4;
	private static final int SMOKE_SOUTH = 5;
	private static final int SMOKE_NORTH_WEST = 6;
	private static final int SMOKE_WEST = 7;
	private static final int SMOKE_SOUTH_WEST = 8;

	public SmokeEffect(int id) {
		super(id, SMOKE_MIDDLE);
	}

	/**
	 * Gets the data effect data to use for the smoke particle effect
	 *
	 * @param direction the smoke goes
	 * @return the data of that direction
	 */
	private static int getSmokeDirection(Vector3 direction) {
		if (direction.equals(Vector3.ZERO)) {
			return SMOKE_MIDDLE;
		}
		float dx = direction.getX();
		float dz = direction.getZ();
		if (dz < 0) {
			if (dx < 0) {
				if (dx * 2 < dz) {
					return 2 * dz < dx ? SMOKE_NORTH_EAST : SMOKE_NORTH;
				} else {
					return SMOKE_EAST;
				}
			} else {
				if (dz * -2 > dx) {
					return -2 * dx < dz ? SMOKE_SOUTH_EAST : SMOKE_EAST;
				} else {
					return SMOKE_SOUTH;
				}
			}
		} else {
			if (dx < 0) {
				if (-2 * dz < dx) {
					return -2 * dx > dz ? SMOKE_NORTH_WEST : SMOKE_WEST;
				} else {
					return SMOKE_NORTH;
				}
			} else {
				if (2 * dx > dz) {
					return 2 * dz > dx ? SMOKE_SOUTH_WEST : SMOKE_SOUTH;
				} else {
					return SMOKE_WEST;
				}
			}
		}
	}

	public void play(Player player, Point position, Vector3 direction) {
		this.play(player, position, getSmokeDirection(direction));
	}

	public void playGlobal(Point position, Vector3 direction) {
		this.playGlobal(position, getSmokeDirection(direction));
	}
}
