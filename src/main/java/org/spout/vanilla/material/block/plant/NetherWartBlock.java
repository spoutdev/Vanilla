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
package org.spout.vanilla.material.block.plant;

import java.util.Random;
import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.EffectRange;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.data.drops.flag.BlockFlags;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Growing;
import org.spout.vanilla.material.block.Plant;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.world.generator.biome.VanillaBiomes;

public class NetherWartBlock extends GroundAttachable implements Plant, Growing, DynamicMaterial {
	public NetherWartBlock(String name, int id) {
		super(name, id);
		this.setLiquidObstacle(false);
		this.setResistance(0.0F).setHardness(0.0F).setOpacity((byte) 0);
		this.getDrops().DEFAULT.clear();
		this.getDrops().DEFAULT.forFlags(BlockFlags.FULLY_GROWN.NOT).add(VanillaMaterials.NETHER_WART);
		this.getDrops().DEFAULT.forFlags(BlockFlags.FULLY_GROWN).addRange(VanillaMaterials.NETHER_WART, 2, 5);
	}

	@Override
	public void getBlockFlags(Block block, Set<Flag> flags) {
		super.getBlockFlags(block, flags);
		if (this.isFullyGrown(block)) {
			flags.add(BlockFlags.FULLY_GROWN);
		}
	}

	@Override
	public int getGrowthStageCount() {
		return 3;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 0;
	}

	@Override
	public int getGrowthStage(Block block) {
		return block.getDataField(0x3);
	}

	@Override
	public void setGrowthStage(Block block, int stage) {
		block.setData(stage & 0x3);
	}

	@Override
	public boolean isFullyGrown(Block block) {
		return block.getData() == 0x3;
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (face == BlockFace.TOP) {
			return block.isMaterial(VanillaMaterials.SOUL_SAND);
		}
		return false;
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS_AND_NEIGHBORS;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		//TODO : Delay before first grow
		b.dynamicUpdate(10000 + currentTime);
	}

	@Override
	public void onDynamicUpdate(Block block, Region region, long updateTime, int data) {
		if (this.isFullyGrown(block) || block.getBiomeType() != VanillaBiomes.NETHERRACK) {
			return;
		}
		Random rand = new Random(block.getWorld().getAge());
		if (rand.nextInt(10) != 0) {
			//TODO : Delay before first grow
			block.dynamicUpdate(updateTime + 10000);
			return;
		}
		int minLight = this.getMinimumLightToGrow();
		if (minLight > 0 && block.translate(BlockFace.TOP).getLight() < minLight) {
			//TODO : Delay before first grow
			block.dynamicUpdate(updateTime + 10000);
			return;
		}
		this.setGrowthStage(block, this.getGrowthStage(block) + 1);
		//TODO : Delay before first grow
		block.dynamicUpdate(updateTime + 10000);
	}
}
