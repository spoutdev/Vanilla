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
package org.spout.vanilla.material.block.misc;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.IntVector3;

import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Crop;
import org.spout.vanilla.material.block.liquid.Water;
import org.spout.vanilla.util.VanillaBlockUtil;

public class FarmLand extends VanillaBlockMaterial implements InitializableMaterial, DynamicMaterial {
	private static final EffectRange WATER_CHECK_RANGE = new CuboidEffectRange(-4, 0, -4, 4, 1, 4);
	private static final EffectRange CROP_CHECK_RANGE = new CuboidEffectRange(-1, 1, -1, 1, 1, 1);

	public FarmLand(String name, int id) {
		super(name, id);
		this.setHardness(0.6F).setResistance(1.0F).setOpaque();
		this.addMiningType(ToolType.SPADE);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().add(VanillaMaterials.DIRT);
	}

	/**
	 * Tests whether a certain farm land block is wet<br>
	 * This wet state is set gradually if water is nearby
	 * @param block of the Farm land
	 * @return True if wet, False if not
	 */
	public boolean isWet(Block block) {
		return block.getData() > 0;
	}

	/**
	 * Tests whether a certain farm land block has water nearby<br>
	 * To obtain the wet state of the block, use {@link isWet()}
	 * @param block of the Farm land
	 * @return True if water is nearby, False if not
	 */
	public boolean hasWaterNearby(Block block) {
		for (IntVector3 coord : WATER_CHECK_RANGE) {
			if (block.translate(coord).getMaterial() instanceof Water) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests whether a certain farm land block has crops nearby<br>
	 * @param block of the Farm land
	 * @return True if crops are nearby, False if not
	 */
	public boolean hasCropsNearby(Block block) {
		for (IntVector3 coord : CROP_CHECK_RANGE) {
			if (block.translate(coord).getMaterial() instanceof Crop) {
				return true;
			}
		}
		return false;
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.NEIGHBORS;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		//TODO : Delay before return to dirt ?
		b.dynamicUpdate(30000 + currentTime);
	}

	@Override
	public void onDynamicUpdate(Block block, Region region, long updateTime, int data) {
		if (VanillaBlockUtil.isRaining(block) || hasWaterNearby(block)) {
			block.setData(7);
			//TODO : Delay before return to dirt ?
			block.dynamicUpdate(updateTime + 30000);
		} else if (this.isWet(block)) {
			// gradually reduce wet state
			block.setData(block.getData() - 1);
			//TODO : Delay before return to dirt ?
			block.dynamicUpdate(updateTime + 30000);
		} else if (!hasCropsNearby(block)) {
			// not wet and has no crops connecting to this farm land, turn this block into dirt
			block.setMaterial(VanillaMaterials.DIRT);
		}
	}
}
