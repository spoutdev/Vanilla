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

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Inventory;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.controller.block.FurnaceController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.FurnaceInventory;
import org.spout.vanilla.inventory.Window;
import org.spout.vanilla.material.block.generic.Solid;
import org.spout.vanilla.protocol.msg.OpenWindowMessage;
import org.spout.vanilla.protocol.msg.ProgressBarMessage;
import org.spout.vanilla.util.InventoryUtil;

import static org.spout.vanilla.protocol.VanillaNetworkSynchronizer.sendPacket;

public class Furnace extends Solid {
	public static final byte PROGRESS_ARROW = 0, FIRE_ICON = 1;
	public static final float SMELT_TIME = 10.f;

	public Furnace() {
		super("Furnace", 61);
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against) {
		if (super.onPlacement(block, data, against)) {
			block.getWorld().createAndSpawnEntity(block.getPosition(), new FurnaceController());
			return true;
		}

		return false;
	}

	@Override
	public void onInteract(Entity entity, Block block, PlayerInteractEvent.Action action, BlockFace face) {
		Controller controller = entity.getController();
		if (!(controller instanceof VanillaPlayer)) {
			return;
		}

		// Get the controller and assign a new window id for the session.
		VanillaPlayer vanillaPlayer = (VanillaPlayer) controller;
		int windowId = InventoryUtil.nextWindowId();
		vanillaPlayer.setWindowId(windowId);
		Inventory inventory = entity.getInventory();
		FurnaceController furnace = (FurnaceController) block.getController();
		Window window = Window.FURNACE;

		// Dispose items into new inventory
		if (furnace == null) {
			System.out.println("Furnace is null");
			return;
		}

		FurnaceInventory furnaceInventory = furnace.getInventory();
		for (int slot = 0; slot < furnaceInventory.getSize() - 4; slot++) {
			furnaceInventory.setItem(inventory.getItem(slot), slot);
		}

		furnaceInventory.addViewer(vanillaPlayer.getPlayer().getNetworkSynchronizer());
		vanillaPlayer.setActiveInventory(furnaceInventory);
		vanillaPlayer.setActiveWindow(window);
		sendPacket(vanillaPlayer.getPlayer(), new OpenWindowMessage(windowId, window.getId(), "Furnace", inventory.getSize()), new ProgressBarMessage(windowId, FIRE_ICON, furnace.getBurnTimeTicks()), new ProgressBarMessage(windowId, PROGRESS_ARROW, furnace.getProgressTicks()));
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
