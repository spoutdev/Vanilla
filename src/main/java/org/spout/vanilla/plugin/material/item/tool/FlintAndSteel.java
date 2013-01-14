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
package org.spout.vanilla.plugin.material.item.tool;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.event.cause.EntityCause;
import org.spout.api.event.cause.PlayerCause;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.api.inventory.Slot;
import org.spout.vanilla.plugin.data.tool.ToolType;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.util.PlayerUtil;
import org.spout.vanilla.plugin.world.generator.object.VanillaObjects;

public class FlintAndSteel extends InteractTool {
	public FlintAndSteel(String name, int id, short durability) {
		super(name, id, durability, ToolType.FLINT_AND_STEEL);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		super.onInteract(entity, block, type, clickedface);
		if (type == Action.RIGHT_CLICK) {
			BlockMaterial clickedmat = block.getMaterial();
			Cause<Entity> cause;
			if (entity instanceof Player) {
				cause = new PlayerCause((Player) entity);
			} else {
				cause = new EntityCause(entity);
			}
			if (clickedmat.equals(VanillaMaterials.TNT)) {
				// Detonate TntBlock
				VanillaMaterials.TNT.onIgnite(block, cause);
			} else {
				// Default fire creation
				Block target = block.translate(clickedface);
				// Default fire placement
				if (VanillaMaterials.FIRE.canCreate(target, (short) 0, cause)) {
					VanillaMaterials.FIRE.onCreate(target, (short) 0, cause);
					Slot held = PlayerUtil.getHeldSlot(entity);
					if (held != null && !PlayerUtil.isCostSuppressed(entity)) {
						held.addData(1);
					}
				}

				// Handle the creation of portals
				Point pos = target.translate(BlockFace.BOTTOM).getPosition();
				VanillaObjects.NETHER_PORTAL.setActive(pos.getWorld(), pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), true);
			}
		}
	}
}
