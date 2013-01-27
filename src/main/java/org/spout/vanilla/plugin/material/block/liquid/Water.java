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
package org.spout.vanilla.plugin.material.block.liquid;

import java.util.Random;

import org.spout.api.Spout;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.EffectIterator;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.GenericMath;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.api.data.Climate;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Liquid;
import org.spout.vanilla.plugin.render.VanillaEffects;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;

public class Water extends Liquid implements DynamicMaterial {
	public Water(String name, int id, boolean flowing) {
		super(name, id, flowing, VanillaMaterialModels.WATER);
		this.setFlowDelay(250);
		if (Spout.getEngine().getPlatform() == Platform.CLIENT) {
			if (!getModel().getRenderMaterial().getBufferEffects().contains(VanillaEffects.BIOME_WATER_COLOR)) {
				getModel().getRenderMaterial().addBufferEffect(VanillaEffects.BIOME_WATER_COLOR);
			}
			if (!getModel().getRenderMaterial().getRenderEffects().contains(VanillaEffects.LIQUID)) {
				getModel().getRenderMaterial().addRenderEffect(VanillaEffects.LIQUID);
			}
		}
		//TODO: Allow this to get past the tests
		//this.setFlowDelay(VanillaConfiguration.WATER_DELAY.getInt());
	}

	@Override
	public int getMaxLevel() {
		return 7;
	}

	@Override
	public boolean hasFlowSource() {
		return true;
	}

	@Override
	public int getLevel(Block block) {
		if (this.isMaterial(block.getMaterial())) {
			return 7 - (block.getData() & 0x7);
		} else {
			return -1;
		}
	}

	@Override
	public void setLevel(Block block, int level) {
		if (level < 0) {
			block.setMaterial(VanillaMaterials.AIR);
		} else {
			if (level > 7) {
				level = 7;
			}
			block.setDataField(0x7, 7 - level);
		}
	}

	@Override
	public boolean isMaterial(Material... materials) {
		for (Material material : materials) {
			if (material instanceof Water) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Liquid getFlowingMaterial() {
		return VanillaMaterials.WATER;
	}

	@Override
	public Liquid getStationaryMaterial() {
		return VanillaMaterials.STATIONARY_WATER;
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.NEIGHBORS;
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
		b.dynamicUpdate(60000 + GenericMath.getRandom().nextInt(60000) + currentTime, true);
		super.onFirstUpdate(b, currentTime);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		super.onDynamicUpdate(block, updateTime, data);

		// Water freezing
		if (!isSource(block)) {
			return;
		}
		if (!block.isAtSurface()) {
			return;
		}
		if (!Climate.get(block).isFreezing()) {
			return;
		}
		if (VanillaMaterials.ICE.canDecayAt(block)) {
			return;
		}

		// Has nearby non-water blocks?
		final Random rand = GenericMath.getRandom();
		if (rand.nextInt(1000) == 0) {
			EffectIterator iterator = EffectRange.NEIGHBORS.iterator();
			while (iterator.hasNext()) {
				if (!(block.translate(iterator.next()).getMaterial() instanceof Water)) {
					//Make sure you don't eliminate water below
					Block below = block.translate(BlockFace.BOTTOM);
					if (below.getMaterial() == VanillaMaterials.WATER) {
						below.setMaterial(getStationaryMaterial());
					}
					block.setMaterial(VanillaMaterials.ICE);
					return;
				}
			}
		}
	}
}
