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
package org.spout.vanilla.material.block.solid;

import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CubicEffectRange;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.SpreadingSolid;
import org.spout.vanilla.world.generator.nether.NetherGenerator;

public class Ice extends SpreadingSolid implements InitializableMaterial {
	private static final byte MIN_MELT_LIGHT = 11;
	private static final EffectRange ICE_SPREAD_RANGE = new CubicEffectRange(1);

	public Ice(String name, int id) {
		super(name, id);
		this.setHardness(0.5F).setResistance(0.8F).setOcclusion((short) 0, BlockFaces.NONE).setOpacity((byte) 2);
		this.getDrops().clear();
	}

	@Override
	public void initialize() {
		setReplacedMaterial(VanillaMaterials.WATER);
		getDrops().DEFAULT.clear();
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}

	@Override
	public void onDestroy(Block block) {
		if (!(block.getWorld().getGenerator() instanceof NetherGenerator) || block.translate(BlockFace.BOTTOM).getMaterial() != VanillaMaterials.AIR) {
			//TODO: Fix detroying ice
		}
		super.onDecay(block);
	}

	@Override
	public EffectRange getSpreadRange() {
		return ICE_SPREAD_RANGE;
	}

	@Override
	public int getMinimumLightToSpread() {
		return 0;
	}

	@Override
	public boolean canDecayAt(Block block) {
		if ((block.getBlockLight() + this.getOpacity() + 1) > MIN_MELT_LIGHT) {
			return true;
		}
		if (Climate.get(block).isMelting()) {
			if ((block.getSkyLight() + this.getOpacity() + 1) > MIN_MELT_LIGHT) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canSpreadFrom(Block block) {
		return Climate.get(block).isFreezing();
	}

	@Override
	public boolean canSpreadTo(Block from, Block to) {
		return super.canSpreadTo(from, to) && VanillaMaterials.WATER.isSource(to);
	}

	@Override
	public long getSpreadingTime(Block b) {
		return 120000L + new Random(b.getWorld().getAge()).nextInt(60000) * 5;
	}
}
