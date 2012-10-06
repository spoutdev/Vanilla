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
package org.spout.vanilla.source;

import org.spout.api.Source;

/**
 * Represents a source of damage.
 */
public enum DamageCause implements Source {
	/**
	 * Damaged by falling.
	 */
	FALL,
	/**
	 * Damaged due to drowning.
	 */
	DROWN,
	/**
	 * Damaged by another entity attacking.
	 */
	ATTACK(2),
	/**
	 * Damaged by falling into the Void.
	 */
	VOID,
	/**
	 * Damaged by making contact with a source of fire.
	 */
	FIRE_SOURCE(3),
	/**
	 * Damaged by continuing to burn after touching a source of fire.
	 */
	BURN(3),
	/**
	 * Damaged by an explosion.
	 */
	EXPLOSION(2),
	/**
	 * Damaged by getting hit by an
	 * {@link org.spout.vanilla.component.substance.Projectile}.
	 */
	PROJECTILE(3),
	/**
	 * Damaged by touching cactus.
	 */
	CACTUS(1),
	/**
	 * Damage caused by suffocation.
	 */
	SUFFOCATION,
	/**
	 * Damaged due to starvation.
	 */
	STARVATION,
	/**
	 * Damaged caused due to the wither effect.
	 */
	WITHERED,
	/**
	 * Damaged by getting hit by a {@link BlazeFireball} or {@link GhastFireball}.
	 */
	FIREBALL(2),
	/**
	 * Damaged by use of a command such as /kill.
	 */
	COMMAND,
	/**
	 * Damaged by an unknown source.
	 */
	UNKNOWN;

	private int durabilityPenalty = 0;

	private DamageCause() {
	}

	private DamageCause(int durabilityPenalty) {
		this.durabilityPenalty = durabilityPenalty;
	}

	public short getDurabilityPenalty() {
		return (short) durabilityPenalty;
	}

	public boolean equals(DamageCause... causes) {
		for (DamageCause cause : causes) {
			if (equals(cause)) {
				return true;
			}
		}

		return false;
	}
}
