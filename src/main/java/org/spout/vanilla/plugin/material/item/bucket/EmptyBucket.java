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
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.event.cause.EntityCause;
import org.spout.api.event.cause.PlayerCause;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.api.inventory.Slot;
import org.spout.vanilla.plugin.component.misc.HeadComponent;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.liquid.Lava;
import org.spout.vanilla.plugin.material.block.liquid.Water;
import org.spout.vanilla.plugin.material.item.VanillaItemMaterial;
import org.spout.vanilla.plugin.util.PlayerUtil;

public class EmptyBucket extends VanillaItemMaterial {
	public EmptyBucket(String name, int id) {
		super(name, id, null);
		setMaxStackSize(16);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action action, BlockFace clickedFace) {
		if (action == Action.RIGHT_CLICK) {
			HeadComponent head = entity.get(HeadComponent.class);
			if (head == null) {
				return;
			}
			BlockMaterial mat;
			BlockIterator iterator = head.getBlockView();
			while(true) {
				if (!iterator.hasNext()) {
					return;
				}
				Block next = iterator.next();
				mat = next.getMaterial();
				if (mat instanceof Water && VanillaMaterials.WATER.isSource(block)) {
					block = next;
					break;
				}
				if (mat instanceof Lava && VanillaMaterials.LAVA.isSource(block)) {
					block = next;
					break;
				}
			}

			// Validate the clicked material to see if it can be picked up
			final Material filled; // material to fill the bucket with
			if (mat instanceof Water && VanillaMaterials.WATER.isSource(block)) {
				filled = VanillaMaterials.WATER_BUCKET;
			} else if (mat instanceof Lava && VanillaMaterials.LAVA.isSource(block)) {
				filled = VanillaMaterials.LAVA_BUCKET;
			} else {
				return;
			}

			// Change item if applicable
			Slot selected = PlayerUtil.getHeldSlot(entity);
			if (selected != null && !PlayerUtil.isCostSuppressed(entity)) {
				selected.set(new ItemStack(filled, 1));
			}

			// Change the clicked block to air
			final Cause<?> cause;
			if (entity instanceof Player) {
				cause = new PlayerCause((Player) entity);
			} else {
				cause = new EntityCause(entity);
			}
			block.setMaterial(BlockMaterial.AIR, cause);
		}
	}
}
