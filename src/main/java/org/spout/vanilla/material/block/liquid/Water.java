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
package org.spout.vanilla.material.block.liquid;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.range.EffectIterator;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;

public class Water extends Liquid implements DynamicMaterial {
	public Water(String name, int id, boolean flowing) {
		super(name, id, flowing);
		this.setFlowDelay(250);
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
	public EffectRange getDynamicRange(){
		return EffectRange.NEIGHBORS;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime){
		b.dynamicUpdate(getFlowDelay() + currentTime);
	}

	@Override
	public void onDynamicUpdate(Block block, Region region, long updateTime, int data){
		//TODO: This should really be in the tick task of the sky entity
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
		EffectIterator iterator = EffectRange.NEIGHBORS.iterator();
		while(iterator.hasNext()){
			if (!(block.translate(iterator.next()).getMaterial() instanceof Water)) {
				block.setMaterial(VanillaMaterials.ICE);
				return;
			}
		}
		
		block.dynamicUpdate(updateTime + getFlowDelay());
	}

}
