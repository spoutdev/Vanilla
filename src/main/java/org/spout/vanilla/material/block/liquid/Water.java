/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;

public class Water extends Liquid {
	public Water(String name, int id, boolean flowing) {
		super(name, id, flowing);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setOpacity((byte) 2);
	}

	@Override
	public Liquid getSourceMaterial() {
		return VanillaMaterials.STATIONARY_WATER;
	}

	@Override
	public Liquid getFlowingMaterial() {
		return VanillaMaterials.WATER;
	}

	@Override
	public void onFlow(Block block, BlockFace to) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLevel(Block block) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReceivingLevel(Block block) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLevel(Block block, int level) {
		// TODO Auto-generated method stub

	}
}
