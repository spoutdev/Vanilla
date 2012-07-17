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
package org.spout.vanilla.window;

import org.spout.api.inventory.InventoryBase;

import org.spout.vanilla.controller.TransactionWindowOwner;
import org.spout.vanilla.controller.living.player.VanillaPlayer;

/**
 * This window contains the player inventory items with additional slots above
 */
public class TransactionWindow extends Window {
	public TransactionWindow(WindowType type, String title, VanillaPlayer owner, TransactionWindowOwner... windowOwners) {
		super(type, title, owner, windowOwners);
		InventoryBase[] all = new InventoryBase[windowOwners.length + 1];
		all[0] = owner.getInventory().getMain();
		for (int i = 0; i < windowOwners.length; i++) {
			all[i + 1] = windowOwners[i].getInventory();
		}
		this.setInventory(all);
	}

	@Override
	public int getInventorySize() {
		return this.getInventory().getSize() - this.getOwner().getInventory().getMain().getSize();
	}
}
