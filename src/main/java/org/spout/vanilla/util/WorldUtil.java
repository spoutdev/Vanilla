/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.util;

import org.spout.api.geo.World;
import org.spout.vanilla.world.Difficulty;

public class WorldUtil {
	private WorldUtil() {
	}

	/**
	 * Sets the {@link Difficulty} of the given world.
	 * @param world World to set the difficulty of
	 * @param difficulty Difficulty to set
	 */
	public static void setDifficulty(World world, Difficulty difficulty) {
		world.getDataMap().put("difficulty", difficulty.getId());
	}

	/**
	 * Returns the {@link Difficulty} of the given world.
	 * @param world World to check
	 * @return Difficulty of the given world
	 */
	public static Difficulty getDifficulty(World world) {
		return Difficulty.getById((Integer) world.getDataMap().get("difficulty"));
	}

	/**
	 * Whether the given world is the given {@link Difficulty}.
	 * @param world World to check
	 * @param difficulty Difficulty to check
	 * @return true if the world is the given difficulty
	 */
	public static boolean isDifficulty(World world, Difficulty difficulty) {
		return (Integer) world.getDataMap().get("difficulty") == difficulty.getId();
	}
}
