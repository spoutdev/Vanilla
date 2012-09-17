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
package org.spout.vanilla.components.inventory.window;

import org.spout.vanilla.components.inventory.PlayerInventory;
import org.spout.vanilla.inventory.util.InventoryConverter;
import org.spout.vanilla.inventory.util.InventoryGridConverter;
import org.spout.vanilla.inventory.window.WindowType;

public class DefaultWindow extends Window {
	@Override
	public void onAttached() {
		init(WindowType.DEFAULT, "Inventory", 9);
		PlayerInventory inventory = getHuman().getInventory();
		converters.add(new InventoryGridConverter(inventory.getArmor(), 1, 5));
		converters.add(new InventoryConverter(inventory.getCraftingGrid(), "3-4, 1-2, 0"));
		super.onAttached();
	}

	@Override
	public int getInstanceId() {
		return 0;
	}
}
