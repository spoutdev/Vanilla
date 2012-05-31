package org.spout.vanilla.inventory;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

public class PlayerCraftingGrid extends Inventory implements CraftingGrid {
	private static final long serialVersionUID = 1L;

	public PlayerCraftingGrid() {
		super(5);
	}

	@Override
	public int getOutputSlot() {
		return 4;
	}

	/**
	 * Returns the current {@link ItemStack} in the top left input in the crafting grid slot (slot 42) ; can return null.
	 * @return top left input item stack
	 */
	public ItemStack getTopLeftInput() {
		return this.getItem(0);
	}

	/**
	 * Returns the current {@link ItemStack} in the top right input in the crafting grid slot (slot 43) ; can return null.
	 * @return top right item stack
	 */
	public ItemStack getTopRightInput() {
		return this.getItem(1);
	}

	/**
	 * Returns the current {@link ItemStack} in the bottom left input in the crafting grid slot (slot 38) ; can return null.
	 * @return bottom left input item stack
	 */
	public ItemStack getBottomLeftInput() {
		return this.getItem(2);
	}

	/**
	 * Returns the current {@link ItemStack} in the bottom right input in the crafting grid slot (slot 39) ; can return null.
	 * @return bottom right input item stack
	 */
	public ItemStack getBottomRightInput() {
		return this.getItem(3);
	}

	/**
	 * Returns the current {@link ItemStack} in the output slot (slot 40) ; can return null.
	 * @return output item stack
	 */
	public ItemStack getOutput() {
		return this.getItem(4);
	}
}
