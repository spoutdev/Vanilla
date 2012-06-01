/*
 * This file is part of Vanilla.
 *
<<<<<<< HEAD
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
=======
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
>>>>>>> Update for item dropping. It is still very buggy to drop items with q but
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
