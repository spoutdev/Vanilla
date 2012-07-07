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
import org.spout.api.inventory.*;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.Material;

import org.spout.vanilla.controller.WindowOwner;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.CraftingInventory;

public abstract class CraftingWindow extends Window {
	protected final CraftingInventory craftingGrid;

	public CraftingWindow(int id, String title, VanillaPlayer owner, CraftingInventory craftingGrid, WindowOwner... windowOwners) {
		super(id, title, owner, windowOwners);
		this.craftingGrid = craftingGrid;
	}

	public CraftingInventory getCraftingGrid() {
		return craftingGrid;
	}

	@Override
	public boolean open() {
		if (super.open()) {
			this.craftingGrid.getGrid().addViewer(this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean close() {
		if (super.close()) {
			this.craftingGrid.getGrid().removeViewer(this);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		if (inventory == this.getCraftingGrid().getGrid()) {
			updateOutput();
		} else {
			super.onSlotSet(inventory, slot, item);
		}
	}

	@Override
	public boolean onClick(InventoryBase inventory, int clickedSlot, boolean rightClick, boolean shift) {
		if (inventory == this.getCraftingGrid() && clickedSlot == this.getCraftingGrid().getOutput().getOffset()) {
			ItemStack output = this.getCraftingGrid().getOutput().getItem();
			if (output == null) {
				return true;
			}
			if (shift) {
				InventorySlot slot = this.craftingGrid.getOutput();
				ItemStack clickedItem = slot.getItem().clone();
				ItemStack[] before = this.craftingGrid.getGrid().getClonedContents();
				ItemStack items = new ItemStack(clickedItem.getMaterial(), 0);
				while (clickedItem.equalsIgnoreSize(slot.getItem())) {
					items.setAmount(items.getAmount() + 1);
					subtractFromCraftingArray();
				}
				if (!owner.getInventory().getMain().canItemFit(items, true, true)) {
					this.craftingGrid.setContents(before);
				} else {
					owner.getInventory().getMain().addItem(items);
				}
			}
		}
		return super.onClick(inventory, clickedSlot, rightClick, shift);
	}

	@Override
	public boolean onLeftClick(InventoryBase inventory, int clickedSlot, boolean shift) {
		if (inventory == this.getCraftingGrid() && clickedSlot == this.getCraftingGrid().getOutput().getOffset()) {
			InventorySlot slot = this.craftingGrid.getOutput();
			ItemStack clickedItem = slot.getItem();
			if (clickedItem == null) {
				if (this.hasItemOnCursor()) {
					return false;
				}
				return true;
			}

			if (!this.hasItemOnCursor()) {
				// clicked item > cursor
				this.setItemOnCursor(clickedItem);
				slot.setItem(null);
				subtractFromCraftingArray();
				return true;
			}
			
			// clickedItem != null && this.hasItemOnCursor()
			// clicked item + cursor
			ItemStack cursorItem = this.getItemOnCursor();
			if (!cursorItem.equalsIgnoreSize(clickedItem)) {
				return false;
			}
			
			// stack
			cursorItem.stack(clickedItem);
			slot.setItem(clickedItem.getAmount() <= 0 ? null : clickedItem);
			this.setItemOnCursor(cursorItem);
			subtractFromCraftingArray();
			return true;		
		}
		return super.onLeftClick(inventory, clickedSlot, shift);
	}

	@Override
	public boolean onRightClick(InventoryBase inventory, int clickedSlot, boolean shift) {
		if (inventory == this.getCraftingGrid() && clickedSlot == this.getCraftingGrid().getOutput().getOffset()) {
			InventorySlot slot = craftingGrid.getOutput();
			ItemStack clickedItem = slot.getItem();
			if (shift) {
				
			}
			if (clickedItem == null) {
				if (this.hasItemOnCursor()) {
					return false;
				}
				return true;
			}
			
			if (this.hasItemOnCursor()) {
				// clicked item + cursor
				ItemStack cursorItem = this.getItemOnCursor();
				if (!cursorItem.equalsIgnoreSize(clickedItem)) {
					return false;
				}
				if (cursorItem.getAmount() + clickedItem.getAmount() > cursorItem.getMaxStackSize()) {
					return false;
				}
				cursorItem.stack(clickedItem);
				slot.setItem(null);
				this.setItemOnCursor(cursorItem);
			} else {
				this.setItemOnCursor(clickedItem.clone());
				slot.setItem(null);
			}
			subtractFromCraftingArray();
			return true;
		}
		return super.onRightClick(inventory, clickedSlot, shift);
	}

	private void subtractFromCraftingArray() {
		ItemStack[] clonedContents = craftingGrid.getGrid().getClonedContents();
		for (int i = 0; i < clonedContents.length; i++) {
			ItemStack clickedItem = clonedContents[i];
			if (clickedItem == null) continue;
			clickedItem.setAmount(clickedItem.getAmount() - 1);
			if (clickedItem.isEmpty()) {
				clickedItem = null;
			}
			clonedContents[i] = clickedItem;
		}
		List<InventoryViewer> viewers = craftingGrid.getViewers();
		for (InventoryViewer viewer : viewers) {
			craftingGrid.removeViewer(viewer);
		}
		craftingGrid.getGrid().setContents(clonedContents);
		for (InventoryViewer viewer : viewers) {
			craftingGrid.addViewer(viewer);
		}
		craftingGrid.notifyViewers(craftingGrid.getGrid().getSize() - 1, craftingGrid.getGrid().getItem(craftingGrid.getGrid().getSize() - 1)); // Notify all of last change
	}
	
	private boolean updateOutput() {
		RecipeManager recipeManager = Spout.getEngine().getRecipeManager();
		InventoryBase grid = craftingGrid.getGrid();
		int rowSize = craftingGrid.getRowSize();
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
		Recipe recipe = recipeManager.matchShapedRecipe(materials);
		if (recipe == null) {
			recipe = recipeManager.matchShapelessRecipe(shapeless);
		}
		if (recipe != null) {
			if (this.getCraftingGrid().getOutput().getItem() == null) {
				this.getCraftingGrid().getOutput().setItem(recipe.getResult());
			}
			return true;
		} else {
			craftingGrid.getOutput().setItem(null);
		}
		return false;
	}
}
