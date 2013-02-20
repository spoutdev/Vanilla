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
package org.spout.vanilla.inventory.entity;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;

/**
 * Represents an entity's quickbar inventory slots.
 */
public abstract class QuickbarInventory extends Inventory {
	protected int selected = 0;

	public QuickbarInventory(int size) {
		super(size);
	}

	/**
	 * Returns the currently selected itemstack
	 * @return itemstack selected
	 */
	public ItemStack getSelectedItem() {
		return getSelectedSlot().get();
	}

	/**
	 * returns the slot that is currently selected on the hotbar
	 * @return slot selected
	 */
	public Slot getSelectedSlot() {
		return new Slot(this, this.selected);
	}

	/**
	 * Sets the currently selected slot to the index given
	 * @param currentSlot
	 */
	public void setSelectedSlot(int currentSlot) {
		this.selected = currentSlot;
	}

	/**
	 * Informs a player of a certain equipment (held item) change
	 * @param entity to inform
	 */
	public abstract void updateHeldItem(Entity entity);
}
