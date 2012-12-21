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
package org.spout.vanilla.event.cause;

/**
 * Represents the cause for a living entity spawning.
 */
public enum LivingSpawnCause {
	/**
	 * This spawn occurred as the result of breeding.
	 */
	BIRTH,
	/**
	 * This spawn occurred as the result of being built.
	 */
	BUILT,
	/**
	 * This spawn occurred due to chunk generation.
	 */
	CHUNK_GENERATION,
	/**
	 * This spawn occurred due to a custom reason.
	 */
	CUSTOM,
	/**
	 * This spawn occurred due to an entity using an egg.
	 */
	EGG,
	/**
	 * This spawn occurred due to a lightning strike.
	 */
	LIGHTNING,
	/**
	 * This spawn occurred naturally.
	 */
	NATURAL,
	/**
	 * This spawn occurred due to a spawner.
	 */
	SPAWNER,
	/**
	 * This spawn occurred due to a split event.
	 */
	SPLIT,
	/**
	 * This spawn occurred as part of a village event.
	 */
	VILLAGE,
	/**
	 * This spawn occurred due to an unknown reason.
	 */
	UNKNOWN;
	
	public boolean equals(LivingSpawnCause... causes) {
		for (LivingSpawnCause cause : causes) {
			if (equals(cause)) {
				return true;
			}
		}
		return false;
	}
}
