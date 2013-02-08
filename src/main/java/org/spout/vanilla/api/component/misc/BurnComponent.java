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
package org.spout.vanilla.api.component.misc;

import org.spout.api.component.type.EntityComponent;

import org.spout.vanilla.api.data.VanillaData;

/**
 * Component handling a entity being on fire.
 */
public abstract class BurnComponent extends EntityComponent {
	private float internalTimer = 0.0f, rainTimer = 0f;

	/**
	 * Retrieve the firetick value. Any value higher than 0 means the entity is on fire.
	 * @return The firetick value.
	 */
	public float getFireTick() {
		return getOwner().getData().get(VanillaData.FIRE_TICK);
	}

	/**
	 * Check if the entity is on fire or not
	 * @return True if the entity is on fire else false.
	 */
	public boolean isOnFire() {
		return getFireTick() > 0;
	}

	/**
	 * Check if the fire hurts or not.
	 * @return True if the fire hurts and false if it doesn't
	 */
	public boolean isFireHurting() {
		return getOwner().getData().get(VanillaData.FIRE_HURT);
	}

	/**
	 * Sets the entity on fire.
	 * @param time The amount of time in seconds the entity should be on fire.
	 * @param hurt True if the fire should hurt else false.
	 */
	public abstract void setOnFire(float time, boolean hurt);
}
