package org.spout.vanilla.inventory;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

public class PlayerArmorInventory extends Inventory {
	private static final long serialVersionUID = 1L;

	public PlayerArmorInventory() {
		super(4);
	}

	/**
	 * Returns the current {@link ItemStack} in the helmet slot (slot 44) ; can return null.
	 * @return helmet item stack
	 */
	public ItemStack getHelmet() {
		return this.getItem(0);
	}

	/**
	 * Returns the current {@link ItemStack} in the chest plate slot (slot 41) ; can return null.
	 * @return chest plate item stack
	 */
	public ItemStack getChestPlate() {
		return this.getItem(1);
	}

	/**
	 * Returns the current {@link ItemStack} in the leggings slot (slot 37) ; can return null.
	 * @return leggings item stack
	 */
	public ItemStack getLeggings() {
		return this.getItem(2);
	}

	/**
	 * Returns the current {@link ItemStack} in the boots slot (slot 36) ; can return null.
	 * @return boots item stack
	 */
	public ItemStack getBoots() {
		return this.getItem(3);
	}
}
