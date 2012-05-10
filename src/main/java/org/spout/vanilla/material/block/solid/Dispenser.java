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
package org.spout.vanilla.material.block.solid;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Inventory;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.vanilla.controller.block.DispenserController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.DispenserInventory;
import org.spout.vanilla.inventory.Window;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.item.MiningTool;
import org.spout.vanilla.material.item.tools.Pickaxe;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Dispenser extends Solid implements Mineable, Directional {
	public Dispenser(String name, int id) {
		super(name, id);
	}
	
	@Override
	public void loadProperties() {
		super.loadProperties();
		this.setHardness(3.5F).setResistance(5.8F);
	}

	@Override
	public void onDestroy(Block block) {
		super.onDestroy(block);
		block.setController(null);
	}

	@Override
	public short getDurabilityPenalty(MiningTool tool) {
		return tool instanceof Pickaxe ? (short) 1 : (short) 2;
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.EWNS.get(block.getData() - 2);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData((short) (BlockFaces.EWNS.indexOf(facing, 0) + 2));
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			this.setFacing(block, VanillaPlayerUtil.getFacing(block.getSource()).getOpposite());
			//TODO: Get block.setController to function correctly
			block.getWorld().createAndSpawnEntity(block.getPosition(), new DispenserController());
			return true;
		}
		return false;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action action, BlockFace face) {
		if (action == Action.RIGHT_CLICK) {
			Controller controller = entity.getController();
			if (!(controller instanceof VanillaPlayer)) {
				return;
			}

			// Get the controller and assign a new window id for the session.
			VanillaPlayer vanillaPlayer = (VanillaPlayer) controller;
			Inventory inventory = entity.getInventory();
			DispenserController dispenser = (DispenserController) block.getController();
			Window window = Window.DISPENSER;

			if (dispenser == null) {
				System.out.println("Dispenser is null");
				return;
			}

			// Dispose items into new inventory
			DispenserInventory dispenserInventory = dispenser.getInventory();
			for (int slot = 0; slot < 36; slot++) {
				dispenserInventory.setItem(slot, inventory.getItem(slot));
			}

			// Add the player who opened the inventory as a viewer
			dispenserInventory.addViewer(vanillaPlayer.getPlayer().getNetworkSynchronizer());
			vanillaPlayer.setActiveInventory(dispenserInventory);
			vanillaPlayer.openWindow(window, "Dispenser", dispenserInventory.getSize());
		}
	}
}
