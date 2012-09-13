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

import org.spout.vanilla.components.living.Human;
import org.spout.vanilla.components.misc.WindowComponent;
import org.spout.vanilla.components.misc.WindowOwner;
import org.spout.vanilla.util.intmap.SlotIndexGrid;

/**
 * This window contains the player inventory items with additional slots above
 */
public class TransactionWindow extends WindowComponent {
	protected int transactionSize;

	public TransactionWindow() {
	}

	public TransactionWindow init(WindowType type, String title, int transactionSize, WindowOwner... windowOwners) {
		super.init(type, title, windowOwners);
		this.transactionSize = transactionSize;
		return this;
	}
	@Override
	public void onAttached() {
		this.addInventory(getHolder().get(Human.class).getInventory().getInventory().getMain(), new SlotIndexGrid(9, 4, transactionSize));
		super.onAttached();
	}

	@Override
	public void open() {
		super.open();
	}

	@Override
	public void close() {
		super.close();
		this.dropItemOnCursor();
	}
}
