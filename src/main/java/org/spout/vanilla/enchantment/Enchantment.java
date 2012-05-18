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
package org.spout.vanilla.enchantment;

import org.spout.vanilla.material.VanillaMaterial;

/**
 * Represents an enchantment
 */
public abstract class Enchantment {
	private final String name;
	private final int id;

	protected Enchantment(String name, int id) {
		this.name = name;
		this.id = id;
	}

	/**
	 * Whether this enchantment can enchant the given material
	 * 
	 * @param material
	 *            Material to check
	 */
	public abstract boolean canEnchant(VanillaMaterial material);

	/**
	 * Gets the maximum level this enchantment can be
	 * 
	 * @return maximum level
	 */
	public abstract int getMaximumLevel();

	/**
	 * Gets the weight of this enchantment, enchantments with higher weights have a greater chance of being selected during the enchantment process
	 * 
	 * @return Weight of this enchantment
	 */
	public abstract int getWeight();

	/**
	 * Whether this enchantment is compatible with the given enchantment while attached to the given material
	 * 
	 * @param enchantment
	 *            Enchantment to check
	 * @param material
	 *            Material that this enchantment is attached to
	 * @return true if this enchantment is compatible with the given enchantment
	 */
	public boolean compatibleWith(Enchantment enchantment, VanillaMaterial material) {
		return true;
	}

	/**
	 * Gets the id of this enchantment
	 * 
	 * @return id of this enchantment
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the name of this enchantment
	 * 
	 * @return name of this enchantment
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Whether this enchantment equals any of the given enchantments
	 * 
	 * @param enchantments
	 *            Enchantments to check
	 * @return true if this enchantment matches any a given enchantment
	 */
	public final boolean equals(Enchantment... enchantments) {
		for (Enchantment enchantment : enchantments) {
			if (equals(enchantment)) {
				return true;
			}
		}

		return false;
	}
}
