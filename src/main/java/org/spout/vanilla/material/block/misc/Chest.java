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
package org.spout.vanilla.material.block.misc;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Inventory;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.block.ChestController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.ChestInventory;
import org.spout.vanilla.inventory.Window;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.item.MiningTool;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Chest extends VanillaBlockMaterial implements Fuel, Mineable, Directional {
	public final float BURN_TIME = 15.f;

	public Chest(String name, int id) {
		super(name, id);
	}

	public void loadProperties() {
		super.loadProperties();
		setHardness(2.5F).setResistance(4.2F);
		setController(VanillaControllerTypes.CHEST);
	}
	
	public boolean isDouble(Block block) {
		Point pos = block.getPosition();
		World world = pos.getWorld();
		VanillaBlockMaterial chest = VanillaMaterials.CHEST;
		if (world.getBlock(pos.add(1, 0, 0)).getMaterial() == chest || world.getBlock(pos.subtract(1, 0, 0)).getMaterial() == chest || world.getBlock(pos.add(0, 0, 1)).getMaterial() == chest || world.getBlock(pos.subtract(0, 0, 1)).getMaterial() == chest) {
			return true;
		}
		return false;
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public short getDurabilityPenalty(MiningTool tool) {
		return tool instanceof Axe ? (short) 1 : (short) 2;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
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
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			this.setFacing(block, VanillaPlayerUtil.getFacing(block.getSource()).getOpposite());
			block.setController(new ChestController(isDouble(block)));
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
			ChestController chest = (ChestController) getController(block);
			boolean doubleChest = isDouble(block);
			chest.setOpened(true);

			// Dispose items into new inventory
			ChestInventory chestInventory = chest.getInventory();
			for (int slot = 0; slot < 36; slot++) {
				chestInventory.setItem(slot, inventory.getItem(slot));
			}

			// Add the player who opened the inventory as a viewer
			//chestInventory.addViewer(vanillaPlayer.getPlayer().getNetworkSynchronizer());
			vanillaPlayer.setActiveInventory(chestInventory);
			vanillaPlayer.openWindow(Window.CHEST, doubleChest ? "Large chest" : "Chest", doubleChest ? 54 : 27);
		}
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
