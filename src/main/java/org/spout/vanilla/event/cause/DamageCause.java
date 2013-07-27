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
package org.spout.vanilla.event.cause;

/**
 * Represents the cause of damage.
 */
public interface DamageCause<T> {
	/**
	 * Represents the different types of damage causes.
	 */
	public enum DamageType {
		/**
		 * Damage due to an entity attack.
		 */
		ATTACK,
		/**
		 * Damage due to continuing to burn after touching a source of fire.
		 */
		BURN,
		/**
		 * Damage due to touching a cactus block.
		 */
		CACTUS,
		/**
		 * Damaged due to use of a command such as /kill.
		 */
		COMMAND,
		/**
		 * Damage due to drowning.
		 */
		DROWN,
		/**
		 * Damage due to an explosion.
		 */
		EXPLOSION,
		/**
		 * Damage due to falling.
		 */
		FALL,
		/**
		 * Damage due to making contact with a source of fire.
		 */
		FIRE_SOURCE,
		/**
		 * Damaged due to getting hit by a {@link BlazeFireball} or {@link GhastFireball}.
		 */
		FIREBALL,
		/**
		 * Damage due to getting hit by an {@link org.spout.vanilla.component.entity.substance.projectile.Projectile}.
		 */
		PROJECTILE,
		/**
		 * Damaged due to starvation.
		 */
		STARVATION,
		/**
		 * Damage due to suffocation.
		 */
		SUFFOCATION,
		/**
		 * Damaged due to an unknown source.
		 */
		UNKNOWN,
		/**
		 * Damage due to falling into the Void.
		 */
		VOID,
		/**
		 * Damaged due to the wither effect.
		 */
		WITHERED;

		public boolean equals(DamageType... types) {
			for (DamageType type : types) {
				if (equals(type)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Gets the {@link DamageType} of the cause.
	 *
	 * @return type
	 */
	public DamageType getType();
}
