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
package org.spout.vanilla.controller.block;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.material.block.SolidMoving;
import org.spout.vanilla.util.ItemUtil;

/**
 * Represents a block that can move, such as sand or gravel.
 */
public class MovingBlock extends VanillaBlockController {
	private final SolidMoving material;

	public MovingBlock(SolidMoving block) {
		super(VanillaControllerTypes.FALLING_BLOCK, block);
		material = block;
	}

	@Override
	public void onAttached() {
	}

	@Override
	public void onTick(float dt) {
		Block block = this.getBlock();
		if (block.translate(BlockFace.BOTTOM).getMaterial().isSolid()) {
			//can we place here?
			if (block.getMaterial().isPlacementObstacle() || !this.material.canPlace(block, this.material.getData(), BlockFace.BOTTOM, true)) {
				//spawn an item
				ItemStack item = new ItemStack(this.material, this.material.getData(), 1);
				ItemUtil.dropItemNaturally(block.getPosition(), item);
			} else {
				this.material.onPlacement(block, this.material.getData(), BlockFace.BOTTOM, true);
			}
			getParent().kill();
		} else {
			getParent().translate(new Vector3(0, -0.5, 0).multiply(dt));
		}
	}
}
