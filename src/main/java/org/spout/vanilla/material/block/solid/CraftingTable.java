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
package org.spout.vanilla.material.block.solid;

import java.util.ArrayList;

import org.spout.api.entity.component.controller.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.block.CraftingTableInventory;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.Instrument;

public class CraftingTable extends Solid implements Mineable {
	public CraftingTable(String name, int id) {
		super(name, id);
		this.setHardness(4.2F);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action action, BlockFace face) {
		if (action == Action.RIGHT_CLICK) {
			Controller controller = entity.getController();
			if (!(controller instanceof VanillaPlayer)) {
				return;
			}

			// Open the crafting table
			CraftingTableInventory inventory = new CraftingTableInventory();
			inventory.open((VanillaPlayer) controller);
		}
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Axe ? (short) 1 : (short) 2;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
