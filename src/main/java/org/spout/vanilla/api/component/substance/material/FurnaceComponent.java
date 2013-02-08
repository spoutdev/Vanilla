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
package org.spout.vanilla.api.component.substance.material;

import org.spout.api.inventory.Container;

/**
 * Represents any kind of furnace.
 */
public abstract class FurnaceComponent extends ViewedBlockComponent implements Container {
	/**
	 * Retrieve the maximum time to smelt something.
	 * @return The maximum time to smelt.
	 */
	public abstract float getMaxSmeltTime();

	/**
	 * Sets the maximum amount of time to smelt something.
	 * @param maxSmeltTime The maximum time to smelt.
	 */
	public abstract void setMaxSmeltTime(float maxSmeltTime);

	/**
	 * Retrieve the current smelting time.
	 * @return The current smelting time.
	 */
	public abstract float getSmeltTime();

	/**
	 * Sets the current smelting time.
	 * @param smeltTime The current smelting time.
	 */
	public abstract void setSmeltTime(float smeltTime);

	/**
	 * Retrieve the maximum fuel amount currently set in the furnace.
	 * @return The max fuel amount.
	 */
	//TODO: Better description anyone?
	public abstract float getMaxFuel();

	/**
	 * Sets the maximum amount of fuel this furnace can hold.
	 * @param maxFuel The maximum amount of fuel.
	 */
	public abstract void setMaxFuel(float maxFuel);

	/**
	 * Retrieve the current amount of fuel in this furnace.
	 * @return The current amount of fuel.
	 */
	public abstract float getFuel();

	/**
	 * Set the current amount of fuel in this furnace.
	 * @param fuel The current amount of fuel.
	 */
	public abstract void setFuel(float fuel);

	/**
	 * Check if all the criteria for smelting is met.
	 * @return True if smelting can happen else false.
	 */
	public abstract boolean canSmelt();

	/**
	 * Activate the smelting process.
	 */
	public abstract void smelt();
}
