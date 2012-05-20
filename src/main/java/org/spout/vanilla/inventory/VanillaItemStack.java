/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import java.util.HashMap;
import java.util.Map;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.vanilla.enchantment.Enchantment;
import org.spout.vanilla.material.VanillaMaterial;

/**
 * Represents a VanillaItemStack with enchantments
 */
public class VanillaItemStack extends ItemStack {
	private static final long serialVersionUID = 1L;

	private final Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();

	public VanillaItemStack(Material material, int amount) {
		super(material, amount);
	}

	public VanillaItemStack(Material material, short data, int amount) {
		super(material, data, amount);
	}

	/**
	 * Attempts to add the given enchantment
	 * @param enchantment Enchantment to add
	 * @return true if the enchantment was successfully attached to this item stack
	 */
	public boolean addEnchantment(Enchantment enchantment) {
		return addEnchantment(enchantment, false);
	}

	/**
	 * Attempts to add the given enchantment with an option to force.
	 * @param enchantment Enchantment to add
	 * @param force Whether to ignore enchantment rules
	 * @return true if the enchantment was successfully attached to this item stack
	 */
	public boolean addEnchantment(Enchantment enchantment, boolean force) {
		return addEnchantment(enchantment, 1, force);
	}

	/**
	 * Attempts to add the given enchantment with the given level to this item stack
	 * @param enchantment Enchantment to add
	 * @param level Level of the enchantment
	 * @param force Whether to ignore enchantment rules and force the enchantment
	 * @return true if the enchantment was successfully attached to this item stack
	 */
	public boolean addEnchantment(Enchantment enchantment, int level, boolean force) {
		if (force) {
			// Even with force, an item cannot have duplicate enchantments
			if (!enchantments.containsKey(enchantment)) {
				enchantments.put(enchantment, level);
			} else {
				return false;
			}
		} else {
			// Do not force the enchantment
			VanillaMaterial material = (VanillaMaterial) getMaterial();
			if (!enchantments.containsKey(enchantment) && enchantment.canEnchant(material)) {
				for (Enchantment conflict : enchantments.keySet()) {
					if (!enchantment.compatibleWith(conflict, material)) {
						return false;
					}
				}
				enchantments.put(enchantment, level);
			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets the level of the given enchantment
	 * @param enchantment Enchantment to check
	 * @return Level of the enchantment, or 0 if the enchantment is not attached to this item stack
	 */
	public int getEnchantmentLevel(Enchantment enchantment) {
		return enchantments.get(enchantment);
	}

	/**
	 * Whether this item stack contains the given enchantment
	 * @param enchantment Enchantment to check
	 * @return true if this item stack contains the enchantment
	 */
	public boolean hasEnchantment(Enchantment enchantment) {
		return enchantments.containsKey(enchantment);
	}

	/**
	 * Removes the given enchantment from this item stack
	 * @param enchantment Enchantment to remove
	 */
	public void removeEnchantment(Enchantment enchantment) {
		enchantments.remove(enchantment);
	}

	@Override
	public VanillaItemStack setAmount(int amount) {
		return (VanillaItemStack) super.setAmount(amount);
	}
}
