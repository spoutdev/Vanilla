package org.spout.vanilla.material.item;

/**
 * Represents an item material that can be enchanted
 */
public interface Enchantable {
	/**
	 * Gets the enchantibility of this item material to use in the process of enchanting
	 * @return Enchantibility level of this item material
	 */
	public int getEnchantibility();
}
