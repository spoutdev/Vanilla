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
package org.spout.vanilla.component.misc;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.data.VanillaData;

/**
 * Component that gives the entity the ability to interact with the world within a range
 */
public class InteractComponent extends EntityComponent {
	/**
	 * Sets the maximum distance this Living Entity can interact at
	 * @param reach distance
	 */
	public void setReach(int reach) {
		getData().put(VanillaData.INTERACT_REACH, reach);
	}

	/**
	 * Gets the maximum distance this Living Entity can interact at
	 * @return reach distance
	 */
	public int getReach() {
		return getData().get(VanillaData.INTERACT_REACH);
	}

	/**
	 * @return
	 */
	public BlockIterator getBlockView() {
		return getBlockView(getReach());
	}

	/**
	 * @param reach
	 * @return
	 */
	public BlockIterator getBlockView(int reach) {
		return new BlockIterator(getOwner().getWorld(), getOwner().getTransform().getTransform(), reach);
	}
}
