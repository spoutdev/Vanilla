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
package org.spout.vanilla.entity.object.moving;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.object.Substance;
import org.spout.vanilla.util.ItemUtil;

/**
 * Represents a block that can move, such as sand or gravel.<br>
 * This is not a Block Controller, because unlike Block Controllers, it can move freely
 */
public class MovingBlock extends Substance {
	private BlockMaterial material;

	public MovingBlock(BlockMaterial block) {
		super(VanillaControllerTypes.FALLING_BLOCK);
		material = block;
	}

	@Override
	public boolean isSavable() {
		return false;
	}

	/**
	 * Gets the material this moving block represents
	 * @return the material
	 */
	public BlockMaterial getMaterial() {
		return this.material;
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		Block block = getParent().getWorld().getBlock(getParent().getPosition(), getParent());
		if (block.translate(BlockFace.BOTTOM).getMaterial().isSolid()) {
			//can we place here?
			if (block.getMaterial().isPlacementObstacle() || !this.material.canPlace(block, this.material.getData())) {
				//spawn an item
				ItemStack item = new ItemStack(this.material, this.material.getData(), 1);
				ItemUtil.dropItemNaturally(block.getPosition(), item);
			} else {
				this.material.onPlacement(block, this.material.getData());
			}
			getParent().kill();
		} else {
			// gravity
			this.setVelocity(this.getVelocity().subtract(0, 0.04, 0));
			this.move();
			// slow-down
			this.setVelocity(this.getVelocity().multiply(0.98));
		}
	}
}
