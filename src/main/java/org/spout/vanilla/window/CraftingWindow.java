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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Recipe;
import org.spout.api.inventory.ShapedRecipe;
import org.spout.api.inventory.ShapelessRecipe;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.object.moving.Item;
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
		// TODO: Drop items
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		super.onSlotSet(inventory, slot, item);
		for (int i : craftingGrid.getGridArray()) {
			if (i == slot) {
				System.out.println("Updating output");
				updateOutput();
				break;
			}
		}
	}

	@Override
	public boolean onClick(int clickedSlot, boolean rightClick, boolean shift) {
		if (itemOnCursor != null && clickedSlot == craftingGrid.getOutputSlot() && !shift) {
			return false;
		}
		return super.onClick(clickedSlot, rightClick, shift);
	}

	private void updateOutput() {
		Set<Recipe> recipes = Spout.getEngine().getRecipeManager().getAllRecipes();
		for (Recipe recipe : recipes) {
			boolean craft = false;
			Inventory grid = craftingGrid.getGridInventory();
			int[] gridArray = craftingGrid.getGridArray();
			if (recipe instanceof ShapelessRecipe) {
				List<Material> materials = new ArrayList<Material>(gridArray.length);
				for (int slot : gridArray) {
					ItemStack item = grid.getItem(slot);
					if (item != null) {
						materials.add(item.getMaterial());
					}
				}
				if (materials.containsAll(recipe.getIngredients())) {
					craft = true;
				}
			} else if (recipe instanceof ShapedRecipe) {
				ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
				List<List<Character>> rows = shapedRecipe.getRows();
				if (rows.size() > craftingGrid.getColumnSize()) {
					break;
				}

				int index = -1;
				for (List<Character> row : rows) {
					if (row.size() > craftingGrid.getRowSize()) {
						break;
					}

					HashMap<Character, Material> ingredientsMap = shapedRecipe.getIngredientsMap();
					for (Character character : row) {
						index++;
						Material req = ingredientsMap.get(character), actual = grid.getItem(gridArray[index]).getMaterial();
						if (!req.equals(actual)) {
							craft = false;
							break;
						}
						craft = true;
					}
				}
			}

			int outputSlot = craftingGrid.getOutputSlot();
			if (grid.getItem(outputSlot) == null && craft) {
				grid.setItem(outputSlot, recipe.getResult());
				break;
			}
		}
	}
}
