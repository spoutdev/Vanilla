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
package org.spout.vanilla.material.block.component;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.substance.Item;
import org.spout.vanilla.component.substance.material.BrewingStand;
import org.spout.vanilla.component.substance.material.chest.Chest;
import org.spout.vanilla.data.drops.flag.ToolTypeFlags;

public class BrewingStandBlock extends ComponentMaterial {
	public BrewingStandBlock(String name, int id) {
		super(name, id, BrewingStand.class, (String) null);
		this.setResistance(2.5F).setHardness(10.F).setOpacity(0).setOcclusion((short) 0, BlockFace.BOTTOM);
		this.getDrops().NOT_CREATIVE.addFlags(ToolTypeFlags.PICKAXE);
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
	public byte getLightLevel(short data) {
		return 1;
	}
}
