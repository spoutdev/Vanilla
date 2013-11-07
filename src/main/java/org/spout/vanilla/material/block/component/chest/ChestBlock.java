/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.material.block.component.chest;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.math.vector.Vector3f;
import org.spout.vanilla.component.block.material.chest.Chest;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.data.resources.VanillaMaterialModels;

public class ChestBlock extends AbstractChestBlock {
	public ChestBlock(String name, int id) {
		super(name, id, Chest.class, VanillaMaterialModels.CHEST);
		this.setHardness(2.5F).setResistance(12.5F).setTransparent();
	}

	/**
	 * Gets the other half of a double chest
	 *
	 * @param block of the Double chest
	 * @return the other half, or null if there is none
	 */
	public Block getOtherHalf(Block block) {
		for (BlockFace face : BlockFaces.NESW) {
			if (block.translate(face).get(Chest.class) != null) {
				return block.translate(face);
			}
		}
		return null;
	}

	/**
	 * Gets whether a certain chest block is a double chest
	 *
	 * @param block of the Chest
	 * @return True if it is a double chest, False if it is a single chest
	 */
	public boolean isDouble(Block block) {
		return getOtherHalf(block) != null;
	}

	@Override
	public boolean onDestroy(Block block, Cause<?> cause) {
		Chest c = block.get(Chest.class);
		Inventory inventory;
		if (c != null) {
			inventory = c.getInventory();
		} else {
			return false;
		}
		boolean shouldD = super.onDestroy(block, cause);
		if (shouldD) {
			Point position = block.getPosition();
			for (ItemStack item : inventory) {
				if (item == null) {
					continue;
				}
				Item.dropNaturally(position, item);
			}
		}
		return shouldD;
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3f clickedPos, boolean isClickedBlock, Cause<?> cause) {
		if (super.canPlace(block, data, against, clickedPos, isClickedBlock, cause)) {
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
