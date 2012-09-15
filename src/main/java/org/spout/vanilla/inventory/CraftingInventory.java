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
package org.spout.vanilla.inventory;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Recipe;
import org.spout.api.inventory.RecipeManager;
import org.spout.api.inventory.special.InventoryRange;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.Material;

public class CraftingInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	private final int columns, rows;
	private final InventorySlot output;
	private final InventoryRange grid;

	public CraftingInventory(int columns, int rows) {
		super(columns * rows + 1);
		this.columns = columns;
		this.rows = rows;
		this.grid = this.createRange(0, this.getSize() - 1);
		this.output = this.createSlot(this.getSize() - 1);
	}

	/**
	 * Gets the output slot in the grid.
	 * @return output slot
	 */
	public InventorySlot getOutput() {
		return this.output;
	}

	/**
	 * Gets the row size of the grid.
	 * @return row size
	 */
	public int getRowSize() {
		return this.rows;
	}

	/**
	 * Gets the column size of the grid.
	 * @return column size
	 */
	public int getColumnSize() {
		return this.columns;
	}

	/**
	 * Gets the grid contained in this inventory
	 * @return grid inventory range
	 */
	public InventoryRange getGrid() {
		return this.grid;
	}

	@Override
	public void onSlotChanged(int slot, ItemStack item) {
		super.onSlotChanged(slot, item);
		if (slot != this.getOutput().getOffset()) {
			this.updateOutput();
		}
	}

	/**
	 * Crafts the current recipe, subtracting all the requirements from the crafting grid
	 */
	public void craft() {
		for (int i = 0; i < this.getGrid().getSize(); i++) {
			this.getGrid().addItemAmount(i, -1);
		}
	}

	public boolean updateOutput() {
		InventoryBase grid = this.getGrid();
		int rowSize = this.getRowSize();
		List<List<Material>> materials = new ArrayList<List<Material>>();
		List<Material> current = new ArrayList<Material>();
		List<Material> shapeless = new ArrayList<Material>();
		int cntr = 0;
		for (ItemStack item : grid) {
			cntr++;
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
		RecipeManager recipeManager = this.getRecipeManager();
		Recipe recipe = recipeManager.matchShapedRecipe(materials);
		if (recipe == null) {
			recipe = recipeManager.matchShapelessRecipe(shapeless);
		}
		if (recipe != null) {
			this.getOutput().setItem(recipe.getResult());
			return true;
		}
		this.getOutput().setItem(null);
		return false;
	}

	public RecipeManager getRecipeManager() {
		return Spout.getEngine().getRecipeManager();
	}
}
