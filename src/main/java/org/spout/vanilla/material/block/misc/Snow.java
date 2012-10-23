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
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;

public class Snow extends GroundAttachable implements DynamicMaterial, InitializableMaterial {
	private static final byte MIN_MELT_LIGHT = 11;

	public Snow(String name, int id) {
		super(name, id);
		this.setLiquidObstacle(false).setStepSound(SoundEffects.STEP_CLOTH).setHardness(0.1F).setResistance(0.2F).setTransparent();
		this.setOcclusion((short) 0, BlockFace.BOTTOM);
		this.addMiningType(ToolType.SPADE).setMiningLevel(ToolLevel.WOOD);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().add(VanillaMaterials.SNOWBALL);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		if (block.translate(BlockFace.BOTTOM).isMaterial(VanillaMaterials.AIR)) {
			block.setMaterial(VanillaMaterials.AIR);
		}
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		//TODO : Delay before next check ?
		b.dynamicUpdate(60000 + currentTime);
	}

	@Override
	public void onDynamicUpdate(Block block, Region region, long updateTime, int data) {
		if (block.getBlockLight() > MIN_MELT_LIGHT) {
			short dataBlock = block.getData();
			if (dataBlock > 0) {
				block.setData(dataBlock - 1);
			} else {
				block.setMaterial(VanillaMaterials.AIR);
			}
		}
		//TODO : Delay before next check ?
		block.dynamicUpdate(updateTime + 60000);
	}
}
