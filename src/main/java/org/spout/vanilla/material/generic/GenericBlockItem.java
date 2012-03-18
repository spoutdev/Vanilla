/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.material.generic;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.MathHelper;

import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.material.BlockItem;

public class GenericBlockItem extends GenericItem implements BlockItem {
	BlockMaterial onPlace = null;

	public GenericBlockItem(String name, int id, BlockMaterial onPlace) {
		super(name, id);
		this.onPlace = onPlace;
		if (onPlace == null) {
			throw new NullPointerException("Block material can not be null");
		}
	}

	public GenericBlockItem(String name, int id, int data, boolean subtypes, BlockMaterial onPlace) {
		super(name, id, data, subtypes);
		this.onPlace = onPlace;
		if (onPlace == null) {
			throw new NullPointerException("Block material can not be null");
		}
	}

	public GenericBlockItem(String name, int id, int data, BlockMaterial onPlace) {
		super(name, id, data);
		this.onPlace = onPlace;
		if (onPlace == null) {
			throw new NullPointerException("Block material can not be null");
		}
	}

	@Override
	public void onInteract(Entity entity, Point position, Action type, BlockFace clickedFace) {
		if (type == Action.RIGHT_CLICK) {
			ItemStack holding = entity.getInventory().getCurrentItem();
			if (holding == null || holding.getMaterial() != this) {
				throw new IllegalStateException("Interaction with an entity that is not holding this material!");
			}
			if (entity.getController() instanceof SurvivalPlayer) {
				if (holding.getAmount() > 1) {
					holding.setAmount(holding.getAmount() - 1);
				} else if (holding.getAmount() == 1) {
					entity.getInventory().setItem(null, entity.getInventory().getCurrentSlot());
				} else {
					throw new IllegalStateException("Entity is holding zero or negative sized item!");
				}
			}
			int x = MathHelper.floor(position.getX());
			int y = MathHelper.floor(position.getY());
			int z = MathHelper.floor(position.getZ());
			System.out.println("Placing Block " + getBlock() + " on Interact at " + position);
			position.getWorld().setBlockIdAndData(x, y, z, getBlock().getId(), getBlock().getData(), entity);
		}
	}

	@Override
	public BlockMaterial getBlock() {
		return onPlace;
	}
}
