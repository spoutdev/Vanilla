/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.material.block.plant;

import java.util.Random;
import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.GenericMath;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.data.drops.flag.BlockFlags;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Growing;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.world.generator.biome.VanillaBiomes;
import org.spout.vanilla.world.lighting.VanillaLighting;

public class NetherWartBlock extends GroundAttachable implements Plant, Growing, DynamicMaterial {
	public NetherWartBlock(String name, int id) {
		super(name, id, null, null);
		this.setLiquidObstacle(false).setResistance(0.0F).setHardness(0.0F).setOpacity((byte) 0).setGhost(true);
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
		return block.getBlockData() == 0x3;
	}

	@Override
	public boolean grow(Block block, Material material) {
		return false;
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
	public void onFirstUpdate(Block b, long currentTime) {
		//TODO : Delay before first grow
		b.dynamicUpdate(getGrowthTime(b) + currentTime, true);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		if (this.isFullyGrown(block) || block.getBiomeType() != VanillaBiomes.NETHERRACK) {
			return;
		}
		final Random rand = GenericMath.getRandom();
		if (rand.nextInt(10) != 0) {
			//TODO : Delay before first grow
			block.dynamicUpdate(updateTime + getGrowthTime(block), true);
			return;
		}
		int minLight = this.getMinimumLightToGrow();
		if (minLight > 0 && VanillaLighting.getLight(block.translate(BlockFace.TOP)) < minLight) {
			//TODO : Delay before first grow
			block.dynamicUpdate(updateTime + 10000, true);
			return;
		}
		this.setGrowthStage(block, this.getGrowthStage(block) + 1);
		//TODO : Delay before first grow
		block.dynamicUpdate(updateTime + getGrowthTime(block), true);
	}

	private long getGrowthTime(Block block) {
		return 120000L + GenericMath.getRandom().nextInt(120000);
	}
}
