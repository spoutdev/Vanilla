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
package org.spout.vanilla.material.block;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.LogicUtil;

import org.spout.vanilla.controller.world.BlockUpdater;
import org.spout.vanilla.util.RedstonePowerMode;

public class PressurePlate extends GroundAttachable implements RedstoneSource, ScheduleUpdated {
	public PressurePlate(String name, int id) {
		super(name, id);
	}

	/**
	 * Gets whether this pressure plate is pressed down
	 * @param block to get it of
	 * @return True if pressed down, False if not
	 */
	public boolean isPressed(Block block) {
		return LogicUtil.getBit(block.getData(), 0x1);
	}

	/**
	 * Sets whether this pressure plate is pressed down
	 * @param block   to set it of
	 * @param pressed whether it is pressed
	 */
	public void setPressed(Block block, boolean pressed) {
		this.setPressed(block, pressed, true);
	}

	/**
	 * Sets whether this pressure plate is pressed down
	 * @param block     to set it of
	 * @param pressed   whether it is pressed
	 * @param doPhysics whether to perform redstone physics
	 */
	public void setPressed(Block block, boolean pressed, boolean doPhysics) {
		block.setData(LogicUtil.setBit(block.getData(), 0x1, pressed));
		if (doPhysics) {
			block.setSource(this).update().translate(BlockFace.BOTTOM).update();
		}
	}

	@Override
	public void onDelayedUpdate(Block block) {
		if (this.isPressed(block)) {
			//TODO: Check if an entity is on top and don't undo it then
			this.setPressed(block, false);
		}
	}

	public void press(Block block) {
		if (!this.isPressed(block)) {
			this.setPressed(block, true);
			BlockUpdater.schedule(block, 20);
		}
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		this.press(block); //TESTING ONLY - REMOVE AFTER
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isPressed(block);
	}

	@Override
	public short getRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return direction == BlockFace.BOTTOM && this.isPressed(block);
	}

	@Override
	public void doRedstoneUpdates(Block block) {
		block.setSource(this).update().translate(BlockFace.BOTTOM).update();
	}
}
