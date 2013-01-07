/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.inventory.window.block.chest;

import org.spout.api.entity.Player;

import org.spout.vanilla.plugin.component.substance.material.chest.AbstractChest;
import org.spout.vanilla.plugin.inventory.window.Window;
import org.spout.vanilla.plugin.inventory.window.WindowType;

public class AbstractChestWindow extends Window {
	private AbstractChest chest;

	public AbstractChestWindow(Player owner, AbstractChest chest, WindowType type, String title, int offset) {
		super(owner, type, title, offset);
		this.chest = chest;
	}

	public AbstractChestWindow(Player owner, WindowType type, String title, int offset) {
		super(owner, type, title, offset);
	}

	public AbstractChest getChest() {
		return chest;
	}

	@Override
	public void close() {
		if (chest != null) {
			chest.close(getPlayer());
		}
		super.close();
	}
}
