/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.item.other;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Inventory;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.Tool;

public class FlintAndSteel extends Tool {
	public FlintAndSteel(String name, int id, short durability) {
		super(name, id, durability);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		super.onInteract(entity, block, type, clickedface);
		Block target = block.translate(clickedface);
		if (target.getMaterial().equals(VanillaMaterials.AIR)) {
			clickedface = clickedface.getOpposite();
			if (VanillaMaterials.FIRE.canPlace(target, (short) 0, clickedface, false)) {
				if (VanillaMaterials.FIRE.onPlacement(target, (short) 0, clickedface, false)) {
					Inventory inv = entity.getInventory();
					inv.addCurrentItemData(1);
				}
			}
		}
	}
}
