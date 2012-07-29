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
package org.spout.vanilla.controller.block;

import org.spout.vanilla.controller.InventoryOwner;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.block.EnchantmentTableInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.window.Window;
import org.spout.vanilla.window.block.EnchantmentTableWindow;

public class EnchantmentTable extends VanillaWindowBlockController implements InventoryOwner {
	private final EnchantmentTableInventory inventory;

	public EnchantmentTable() {
		super(VanillaControllerTypes.ENCHANTMENT_TABLE, VanillaMaterials.ENCHANTMENT_TABLE);
		inventory = new EnchantmentTableInventory();
	}

	@Override
	public void onTick(float dt) {
	}

	@Override
	public void onAttached() {
		System.out.println("Enchantment Table entity spawned and controller attached to: " + getParent().getPosition().toString());
	}

	@Override
	public EnchantmentTableInventory getInventory() {
		return inventory;
	}

	@Override
	public Window createWindow(VanillaPlayer player) {
		return new EnchantmentTableWindow(player, this);
	}
}
