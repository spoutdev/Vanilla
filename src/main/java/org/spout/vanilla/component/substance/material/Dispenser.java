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
package org.spout.vanilla.component.substance.material;

import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;

import org.spout.vanilla.component.inventory.window.block.DispenserWindow;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.Container;
import org.spout.vanilla.inventory.block.DispenserInventory;

public class Dispenser extends WindowBlockComponent implements Container {
	private final DispenserInventory inventory = new DispenserInventory();

	public boolean isPowered() {
		return getData().get(VanillaData.IS_POWERED);
	}

	public void setPowered(boolean powered) {
		getData().put(VanillaData.IS_POWERED, powered);
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void openWindow(Player player) {
		player.add(DispenserWindow.class).init(inventory).open();
	}
}
