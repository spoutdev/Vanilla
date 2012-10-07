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
package org.spout.vanilla.material.item.tool;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.material.VanillaMaterials;

public class FlintAndSteel extends InteractTool {
	public FlintAndSteel(String name, int id, short durability) {
		super(name, id, durability, ToolType.FLINT_AND_STEEL);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		super.onInteract(entity, block, type, clickedface);
		if (type == Action.RIGHT_CLICK) {
			BlockMaterial clickedmat = block.getMaterial();
			if (clickedmat.equals(VanillaMaterials.TNT)) {
				// Detonate TntBlock
				VanillaMaterials.TNT.onIgnite(block);
				return;
			} else {
				// Default fire creation
				Block target = block.translate(clickedface);

				// Handle the creation of portals
				if (VanillaMaterials.PORTAL.createPortal(target.translate(BlockFace.BOTTOM))) {
					return;
				}

				// Default fire placement
				clickedface = clickedface.getOpposite();
				if (VanillaMaterials.FIRE.canPlace(target, (short) 0)) {
					if (VanillaMaterials.FIRE.onPlacement(target, (short) 0)) {
						PlayerQuickbar inv = entity.get(Human.class).getInventory().getQuickbar();
						inv.addData(inv.getCurrentSlot(), 1);
					}
				}
			}
		}
	}
}
