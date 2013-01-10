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
package org.spout.vanilla.plugin.material.item.bucket;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.inventory.player.PlayerQuickbar;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.item.VanillaItemMaterial;

public class EmptyBucket extends VanillaItemMaterial {
	public EmptyBucket(String name, int id) {
		super(name, id, null);
		setMaxStackSize(16);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action action, BlockFace clickedFace) {
		if (action == Action.RIGHT_CLICK) {
			BlockMaterial mat = block.getMaterial();
			PlayerQuickbar quickbar = entity.get(PlayerInventory.class).getQuickbar();
			Material filled; // material to fill the bucket with
			if (mat == VanillaMaterials.WATER) {
				filled = VanillaMaterials.WATER_BUCKET;
			} else if (mat == VanillaMaterials.LAVA) {
				filled = VanillaMaterials.LAVA_BUCKET;
			} else {
				return;
			}
			quickbar.getSelectedSlot().addAmount(-1);
			quickbar.add(new ItemStack(filled, 1));
		}
	}
}
