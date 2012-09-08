package org.spout.vanilla.inventory;

import org.spout.api.inventory.InventoryBase;

public interface InventoryOwner {
	/**
	 * Gets the inventory of this entity
	 * @return The Inventory of this entity
	 */
	public InventoryBase getInventory();
}
