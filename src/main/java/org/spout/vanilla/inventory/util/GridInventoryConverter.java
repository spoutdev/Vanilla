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
package org.spout.vanilla.inventory.util;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.shape.Grid;
import org.spout.api.inventory.util.GridIterator;
import org.spout.api.math.Vector2;

import org.spout.vanilla.inventory.window.gui.InventorySlot;
import org.spout.vanilla.inventory.window.gui.RenderItemStack;
import org.spout.vanilla.material.VanillaMaterials;

/**
 * Represents an {@link InventoryConverter} that converts slots given the
 * length of a grid in an inventory.
 */
public class GridInventoryConverter extends InventoryConverter {
	public static final float SLOT_WIDTH = 0.1f;
	public static final float SLOT_HEIGHT = 0.14f;
	private final Grid grid;
	private final Vector2 pos;

	public GridInventoryConverter(Inventory inventory, int length, int offset, Vector2 pos) {
		super(inventory, new int[inventory.size()], new Vector2[inventory.size()], offset);
		grid = inventory.grid(length);
		this.pos = pos;
		GridIterator iter = grid.iterator();
		while (iter.hasNext()) {
			int i = iter.next(), x = iter.getX(), y = iter.getY(), size = grid.getSize();
			slots[i] = (offset + size) - (length * y) - (length - x);
			InventorySlot slot = widgets[i].get(InventorySlot.class);
			slot.setRenderItemStack(new RenderItemStack(new ItemStack(VanillaMaterials.LEATHER_CAP, 1))); // TODO: Handle this in onClick
			slot.setPosition(pos.add(x * SLOT_WIDTH, y * SLOT_HEIGHT));
		}
	}

	public GridInventoryConverter(Inventory inventory, int length, Vector2 pos) {
		this(inventory, length, 0, pos);
	}

	public GridInventoryConverter translate(int offset) {
		return new GridInventoryConverter(inventory, grid.getLength(), this.offset + offset, pos);
	}

	/**
	 * Returns the grid associated with this converter.
	 * @return grid
	 */
	public Grid getGrid() {
		return grid;
	}
}
