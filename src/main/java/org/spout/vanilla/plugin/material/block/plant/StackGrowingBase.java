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
package org.spout.vanilla.plugin.material.block.plant;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.api.material.block.Growing;
import org.spout.vanilla.api.material.block.Plant;

import org.spout.vanilla.plugin.material.VanillaBlockMaterial;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.attachable.GroundAttachable;

/**
 * A base class for plants that grow upwards by placing multiple blocks on one another
 */
public abstract class StackGrowingBase extends GroundAttachable implements Plant, Growing, DynamicMaterial {
	private static EffectRange dynamicRange = new CuboidEffectRange(0, 0, 0, 0, 1, 0);

	public StackGrowingBase(short dataMask, String name, int id, String model) {
		super(dataMask, name, id, model);
	}

	public StackGrowingBase(String name, int id, int data, VanillaBlockMaterial parent, String model) {
		super(name, id, data, parent, model);
	}

	public StackGrowingBase(String name, int id, String model) {
		super(name, id, model);
	}

	@Override
	public int getGrowthStageCount() {
		return 3;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 0;
	}

	/**
	 * Gets the time between two grow events of this Plant
	 * @param block of this Plant
	 * @return the time in milliseconds, can be random
	 */
	public abstract long getGrowTime(Block block);

	@Override
	public int getGrowthStage(Block block) {
		block = getBase(block);
		// go up to the max stage count
		for (int i = 0; i < this.getGrowthStageCount(); i++) {
			block = block.translate(BlockFace.TOP);
			if (!block.isMaterial(this)) {
				return i - 1;
			}
		}
		return this.getGrowthStageCount() - 1;
	}

	@Override
	public void setGrowthStage(Block block, int stage) {
		block = getBase(block);
		if (stage >= this.getGrowthStageCount()) {
			stage = this.getGrowthStageCount() - 1;
		}
		for (int i = 0; i <= stage; i++) {
			block = block.translate(BlockFace.TOP);
			block.setMaterial(this);
		}
	}

	/**
	 * Gets the base block this Plant rests on
	 * @param block of the Cactus
	 * @return the base Block
	 */
	public Block getBase(Block block) {
		// get the bottom block
		while (block.isMaterial(this)) {
			block = block.translate(BlockFace.BOTTOM);
		}
		return block;
	}

	/**
	 * Gets the top block of this Plant
	 * @param block of this Plant
	 * @return the top block
	 */
	public Block getTop(Block block) {
		// get the top block
		while (block.isMaterial(this)) {
			block = block.translate(BlockFace.TOP);
		}
		return block.translate(BlockFace.BOTTOM);
	}

	@Override
	public boolean isFullyGrown(Block block) {
		block = getBase(block);
		for (int i = 0; i < this.getGrowthStageCount(); i++) {
			block = block.translate(BlockFace.TOP);
			if (!block.isMaterial(this)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public EffectRange getDynamicRange() {
		return dynamicRange;
	}

	@Override
	public void onFirstUpdate(Block block, long currentTime) {
		block.dynamicUpdate(currentTime + getGrowTime(block), true);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		block.dynamicUpdate(block.getWorld().getAge() + getGrowTime(block), true);
		Block below = block.translate(BlockFace.BOTTOM);
		// Only fire dynamic updates for the bottom block
		if (!this.canAttachTo(below, BlockFace.TOP) || below.isMaterial(this)) {
			return;
		}
		if (isFullyGrown(block)) {
			return;
		}
		// Check the top block for availability and light level
		Block top = getTop(block).translate(BlockFace.TOP);
		if (!top.isMaterial(VanillaMaterials.AIR)) {
			return;
		}
		int minLight = this.getMinimumLightToGrow();
		if (minLight > 0 && top.getLight() < minLight) {
			return;
		}
		// Finally, grow
		this.setGrowthStage(block, this.getGrowthStage(block) + 1);
	}
}
