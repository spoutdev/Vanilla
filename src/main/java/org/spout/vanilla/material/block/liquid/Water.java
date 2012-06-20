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

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;

public class Water extends Liquid {
	public Water(String name, int id, boolean flowing) {
		super(name, id, flowing);
	}

	@Override
	public int getTickDelay() {
		return 20;
	}

	@Override
	public int getMaxLevel() {
		return 7;
	}

	@Override
	public boolean hasFlowSource() {
		return false;
	}

	@Override
	public int getLevel(Block block) {
		if (block.getMaterial().equals(this)) {
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
	public Liquid getFlowingMaterial() {
		return VanillaMaterials.WATER;
	}

	@Override
	public Liquid getStationaryMaterial() {
		return VanillaMaterials.STATIONARY_WATER;
	}
}
