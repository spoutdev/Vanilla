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
package org.spout.vanilla.plugin.material.block.component;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.api.material.InitializableMaterial;
import org.spout.vanilla.api.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;
import org.spout.vanilla.plugin.util.RedstoneUtil;
import org.spout.vanilla.plugin.component.substance.material.CommandBlockComponent;

public class CommandBlock extends ComponentMaterial implements RedstoneTarget, InitializableMaterial {
	
	public CommandBlock(String name, int id) {
		super(name, id, CommandBlockComponent.class, VanillaMaterialModels.COMMAND_BLOCK);
	}
	
	@Override
	public void initialize() {
		getDrops().DEFAULT.clear();
	}
	
	@Override
	public boolean hasPhysics() {
		return true;
	}
	
	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
	
	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		
		CommandBlockComponent commandBlock = (CommandBlockComponent) block.getComponent();
		
		boolean powered = this.isReceivingPower(block);
		boolean wasPowered = commandBlock.setPowered(powered);
		if (powered && !wasPowered) {
			commandBlock.executeCommandInput();
		}
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}
}
