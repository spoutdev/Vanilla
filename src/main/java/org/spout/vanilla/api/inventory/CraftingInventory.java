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
package org.spout.vanilla.api.inventory;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.shape.Grid;
import org.spout.api.inventory.util.GridIterator;
import org.spout.api.material.Material;

import org.spout.vanilla.api.component.inventory.PlayerInventoryComponent;

/**
 * Represents an inventory that contains a crafting matrix.
 */
public abstract class CraftingInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	private final Grid grid;
	private final int outputSlot, offset;

	public CraftingInventory(Grid grid, int outputSlot, int offset) {
		super(grid.getSize() + 1);
		this.grid = grid;
		this.outputSlot = outputSlot;
		this.offset = offset;
	}

	public CraftingInventory(Grid grid, int outputSlot) {
		this(grid, outputSlot, 0);
	}

	public CraftingInventory(int length, int width, int outputSlot, int offset) {
		this(new Grid(length, width), outputSlot, offset);
	}

	public CraftingInventory(int length, int width, int outputSlot) {
		this(new Grid(length, width), outputSlot);
	}

	/**
	 * Returns the grid of the slots that will have the output slot updated.
	 * @return grid of slots to trigger update
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Returns the slot to update when a slot is updated in the crafting grid.
	 * @return slot to update
	 */
	public int getOutputSlot() {
		return outputSlot;
	}

	/**
	 * Returns the offset of the first slot in the crafting grid.
	 * @return offset of grid
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Handles a shift click on the given inventory slot
	 * @param slot
	 * @param toInventory
	 * @return true if the action completed successfully.
	 */
	public boolean onShiftClick(int slot, PlayerInventoryComponent toInventory) {
		ItemStack result = get(getOutputSlot());
		Material resultMat = (result != null ? result.getMaterial() : null);
		if (slot == getOutputSlot() + getOffset() && result != null) {
			while (result != null && !result.isEmpty()) {
				toInventory.add(result);
				set(getOutputSlot(), null);
				result = get(getOutputSlot());
				//Changed recipes, stop!
				if (result != null && !result.getMaterial().isMaterial(resultMat)) {
					break;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onSlotChanged(int slot, ItemStack item, ItemStack previous) {
		if (slot == outputSlot + offset) {
			craft();
			return;
		}
		GridIterator i = grid.iterator();
		while (i.hasNext()) {
			if (i.next() + offset == slot) {
				updateOutput();
				return;
			}
		}
	}

	/**
	 * Crafts the current recipe, subtracting all the requirements from the crafting grid
	 */
	public void craft() {
		GridIterator iterator = grid.iterator();
		while (iterator.hasNext()) {
			addAmount(iterator.next(), -1);
		}
	}

	/**
	 * Assesses the crafting matrix to determine if an {@link ItemStack} should
	 * be crafted to the {@link #outputSlot};
	 */
	public void updateOutput() {
		// TODO: Re-implement
	}

	private void updateCraftingSlot(ItemStack item) {
		//Do not update, or else it triggers the onSlotSet crafting code
		set(getOutputSlot(), item, false);
		//Manually inform viewers
		for (InventoryViewer viewer : this.getViewers()) {
			viewer.onSlotSet(this, getOutputSlot(), item, null);
		}
	}

	@Override
	public boolean canSet(int i, ItemStack item) {
		return super.canSet(i, item) && i != outputSlot;
	}
}
