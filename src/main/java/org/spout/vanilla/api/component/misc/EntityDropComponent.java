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

import java.util.List;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.api.data.VanillaData;

/**
 * Handle item/XP drop from a entity.
 */
public abstract class EntityDropComponent extends EntityComponent {

	/**
	 * Add a amount of XP the entity drops.
	 * @param amount The amount of XP the entity drops.
	 * @return The current component.
	 */
	public EntityDropComponent addXpDrop(short amount) {
		getOwner().getData().put(VanillaData.DROP_EXPERIENCE, amount);
		return this;
	}

	/**
	 * Retrieve the amount of XP the entity drops on death.
	 * @return the amount of XP.
	 */
	public short getXpDrop() {
		return getOwner().getData().get(VanillaData.DROP_EXPERIENCE);
	}

	/**
	 * Add a item the entity drops.
	 * @param itemstack Contains the item to drop.
	 * @return The current component.
	 */
	public abstract EntityDropComponent addDrop(ItemStack itemstack);

	/**
	 * Retrieve a list of all the item drops this entity does.
	 * @return A list of all the items this entity drops.
	 */
	public abstract List<ItemStack> getDrops();
}
