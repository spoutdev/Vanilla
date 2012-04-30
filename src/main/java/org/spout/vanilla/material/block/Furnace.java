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
package org.spout.vanilla.material.block;

import org.spout.api.Source;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.block.FurnaceController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.FurnaceInventory;
import org.spout.vanilla.material.block.generic.Solid;
import org.spout.vanilla.protocol.msg.OpenWindowMessage;

public class Furnace extends Solid {
	public Furnace() {
		super("Furnace", 61);
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Source source) {
		if (super.onPlacement(block, data, against, source)) {
			block.getWorld().createAndSpawnEntity(block.getPosition(), new FurnaceController());
			return true;
		}

		return false;
	}

	@Override
	public void onInteract(Entity entity, Point pos, PlayerInteractEvent.Action action, BlockFace face) {
		Controller controller = entity.getController();
		if (!(controller instanceof VanillaPlayer)) {
			return;
		}

		VanillaPlayer vanillaPlayer = (VanillaPlayer) controller;
		vanillaPlayer.sendPacket(vanillaPlayer.getPlayer(), new OpenWindowMessage(1, 2, "Furnace", 38));
		Inventory inventory = entity.getInventory();
		if (!(inventory instanceof PlayerInventory)) {
			return;
		}

		FurnaceInventory newInventory = new FurnaceInventory();
		for (int i = 0; i < inventory.getSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack != null) {
				newInventory.setItem(stack, i);
			}
		}
		
		vanillaPlayer.setActiveInventory(newInventory);
	}
	
	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
