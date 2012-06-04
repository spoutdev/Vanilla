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
package org.spout.vanilla.window;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Recipe;
import org.spout.api.inventory.RecipeManager;
import org.spout.api.material.Material;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.CraftingGrid;

public abstract class CraftingWindow extends Window {
	protected final CraftingGrid craftingGrid;

	public CraftingWindow(int id, String title, VanillaPlayer owner, CraftingGrid craftingGrid) {
		super(id, title, owner);
		this.craftingGrid = craftingGrid;
	}

	public CraftingGrid getCraftingGrid() {
		return craftingGrid;
	}

	@Override
	public void onClosed() {
		super.onClosed();
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		super.onSlotSet(inventory, slot, item);
		int size = 0;
		for (InventoryBase i : getInventory().getInventories()) {
			if (i != craftingGrid.getGridInventory() && slot > i.getSize() + size) {
				size += i.getSize();
			}
		}
		for (int i : craftingGrid.getGridArray()) {
			if (i + size == slot) {
				updateOutput();
				break;
			}
		}
	}

	@Override
	public boolean onClick(int clickedSlot, boolean rightClick, boolean shift) {
		int size = 0;
		for (InventoryBase i : getInventory().getInventories()) {
			if (i != craftingGrid.getGridInventory()) {
				size += i.getSize();
			}
		}
		if (itemOnCursor != null && clickedSlot == craftingGrid.getOutputSlot() + size && !shift) {
			return false;
		}
		return super.onClick(clickedSlot, rightClick, shift);
	}

	private boolean updateOutput() {
		RecipeManager recipeManager = Spout.getEngine().getRecipeManager();
		Inventory grid = craftingGrid.getGridInventory();
		int[] gridArray = craftingGrid.getGridArray();
		int rowSize = craftingGrid.getRowSize();
		List<List<Material>> materials = new ArrayList<List<Material>>();
		List<Material> current = new ArrayList<Material>();
		List<Material> shapeless = new ArrayList<Material>();
		int cntr = 0;
		for (int slot : gridArray) {
			cntr++;
			ItemStack item = grid.getItem(slot);
			Material mat = null;
			if (item != null) {
			    mat = item.getMaterial();
			}
			current.add(mat);
			if (mat != null) {
				shapeless.add(mat);
			}
			if (cntr >= rowSize) {
				materials.add(current);
				current = new ArrayList<Material>();
				cntr = 0;
			}
		}
		Recipe recipe = null;
		recipe = recipeManager.matchShapedRecipe(materials);
		if (recipe == null) {
			recipe = recipeManager.matchShapelessRecipe(shapeless);
		}
		if (recipe != null) {
			int outputSlot = craftingGrid.getOutputSlot();
			if (grid.getItem(outputSlot) == null) {
				grid.setItem(outputSlot, recipe.getResult());
			}
			return true;
		}
		return false;
	}
}
