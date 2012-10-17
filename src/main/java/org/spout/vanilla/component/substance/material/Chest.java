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

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.spout.api.entity.Player;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.component.inventory.window.block.DoubleChestWindow;
import org.spout.vanilla.component.inventory.window.block.SingleChestWindow;
import org.spout.vanilla.inventory.Container;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.block.DoubleChestInventory;
import org.spout.vanilla.util.ItemUtil;

public class Chest extends WindowBlockComponent implements Container {
	private ChestInventory inventory;

	public Chest() {
		inventory = new ChestInventory();
	}

	@Override
	public ChestInventory getInventory() {
		return inventory;
	}

	@Override
	public void openWindow(Player player) {
		if (inventory instanceof DoubleChestInventory) {
			//player.add(DoubleChestWindow.class).init(inventory, inventory);
		}
		player.add(SingleChestWindow.class).init(inventory).open();
		// TODO: Chest logic
	}

	public void addNeighbor(Block block, boolean first) {
		Chest neighbor = (Chest) block.getComponent();
		if (first) {
			inventory = DoubleChestInventory.convert(inventory, neighbor.inventory);
		} else {
			inventory = DoubleChestInventory.convert(neighbor.inventory, inventory);
		}
	}

	public void onDestroy(Block block) {
		ItemStack[] items = inventory.toArray(new ItemStack[inventory.size()]);
		Point position = block.getPosition();
		for (ItemStack item : items) {
			if (item == null) {
				continue;
			}
			ItemUtil.dropItemNaturally(position, item);
		}
	}

	/**
	 * 
	 * @param block
	 * @param top whether to drop the top
	 */
	public void removeNeighbor(Block block, boolean top) {
		ItemStack[] toDrop;
		if (top) {
			toDrop = ArrayUtils.subarray(inventory.toArray(new ItemStack[27]), 0, 27);
			inventory = new ChestInventory();
			inventory.addAll(Arrays.asList(ArrayUtils.subarray(inventory.toArray(new ItemStack[27]), 27, 52)));
			
		} else {
			toDrop = ArrayUtils.subarray(inventory.toArray(new ItemStack[27]), 27, 52);
			inventory = new ChestInventory();
			inventory.addAll(Arrays.asList(ArrayUtils.subarray(inventory.toArray(new ItemStack[27]), 0, 27)));
		}
		Point position = block.getPosition();
		for (ItemStack item : toDrop) {
			if (item == null) {
				continue;
			}
			ItemUtil.dropItemNaturally(position, item);
		}
	}
	
}
