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
package org.spout.vanilla.material.block.component;

import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.component.substance.Item;
import org.spout.vanilla.component.substance.material.BrewingStand;
import org.spout.vanilla.data.drops.flag.ToolTypeFlags;

public class BrewingStandBlock extends ComponentMaterial {
	public BrewingStandBlock(String name, int id) {
		super(name, id, BrewingStand.class, (String) null);
		this.setResistance(2.5F).setHardness(10.F).setOpacity(0).setOcclusion((short) 0, BlockFace.BOTTOM);
		this.getDrops().NOT_CREATIVE.addFlags(ToolTypeFlags.PICKAXE);
	}

	@Override
	public void onPostDestroy(Block block, Set<Flag> flags) {
		BrewingStand brewingStand = (BrewingStand) block.getComponent();
		//Drop items
		Inventory inventory = brewingStand.getInventory();
		Point position = block.getPosition();
		for (ItemStack item : inventory) {
			if (item == null) {
				continue;
			}
			Item.dropNaturally(position, item);
		}
		super.onPostDestroy(block, flags);
	}

	@Override
	public byte getLightLevel(short data) {
		return 1;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
