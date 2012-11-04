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
package org.spout.vanilla.material.block.component.chest;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.component.substance.Item;
import org.spout.vanilla.component.substance.material.chest.Chest;

public class ChestBlock extends AbstractChestBlock {
	public final float BURN_TIME = 15;

	public ChestBlock(String name, int id) {
		super(name, id, Chest.class, (String)null );
		this.setHardness(2.5F).setResistance(4.2F).setTransparent();
	}

	/**
	 * Gets the other half of a double chest
	 * @param block of the Double chest
	 * @return the other half, or null if there is none
	 */
	public Block getOtherHalf(Block block) {
		for (BlockFace face : BlockFaces.NESW) {
			if (block.translate(face).getMaterial().equals(this)) {
				return block.translate(face);
			}
		}
		return null;
	}

	/**
	 * Gets whether a certain chest block is a double chest
	 * @param block of the Chest
	 * @return True if it is a double chest, False if it is a single chest
	 */
	public boolean isDouble(Block block) {
		return getOtherHalf(block) != null;
	}

	@Override
	public void onDestroy(Block block, Cause<?> cause) {
		Chest chest = (Chest) block.getComponent();
		//Drop items
		Inventory inventory = chest.getInventory();
		Point position = block.getPosition();
		for (ItemStack item : inventory) {
			if (item == null) {
				continue;
			}
			Item.dropNaturally(position, item);
		}
		super.onDestroy(block, cause);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		if (super.canPlace(block, data, against, clickedPos, isClickedBlock)) {
			//no surrounding double-chest blocks?
			int count = 0;
			for (BlockFace face : BlockFaces.NESW) {
				if (block.translate(face).getMaterial().equals(this)) {
					count++;
					if (count == 2 || this.isDouble(block.translate(face))) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
}
