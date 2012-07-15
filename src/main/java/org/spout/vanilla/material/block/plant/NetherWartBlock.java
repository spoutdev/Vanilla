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

import java.util.ArrayList;
import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.RandomBlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Growing;
import org.spout.vanilla.material.block.Plant;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.world.generator.VanillaBiomes;

public class NetherWartBlock extends GroundAttachable implements Plant, Growing, RandomBlockMaterial {
	public NetherWartBlock(String name, int id) {
		super(name, id);
		this.setLiquidObstacle(false);
		this.setResistance(0.0F).setHardness(0.0F).setOpacity((byte) 0);
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
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		return material.equals(VanillaMaterials.SOUL_SAND) && super.canAttachTo(material, face);
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block, ItemStack holding) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(VanillaMaterials.NETHER_WART, this.isFullyGrown(block) ? new Random().nextInt(4) + 2 : 1));
		return drops;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}

	@Override
	public void onRandomTick(Block block) {
		if (this.isFullyGrown(block) || block.getBiomeType() != VanillaBiomes.NETHERRACK) {
			return;
		}
		Random rand = new Random(block.getWorld().getAge());
		if (rand.nextInt(10) != 0) {
			return;
		}
		int minLight = this.getMinimumLightToGrow();
		if (minLight > 0 && block.translate(BlockFace.TOP).getLight() < minLight) {
			return;
		}
		this.setGrowthStage(block, this.getGrowthStage(block) + 1);
	}
}
