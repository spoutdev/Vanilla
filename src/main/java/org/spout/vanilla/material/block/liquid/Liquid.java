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
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaBlockMaterial;

public abstract class Liquid extends VanillaBlockMaterial {
	private final boolean flowing;

	public Liquid(String name, int id, boolean flowing) {
		super(name, id);
		this.flowing = flowing;
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(100.0F).setResistance(166.7F);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		int level = this.getReceivingLevel(block);
		this.setLevel(block, level);
		if (level > 0) {

		}
	}

	public abstract Liquid getSourceMaterial();

	public abstract Liquid getFlowingMaterial();

	public boolean isMaterial(BlockMaterial material) {
		return material.equals(this.getFlowingMaterial(), this.getSourceMaterial());
	}

	public abstract void onFlow(Block block, BlockFace to);

	/**
	 * Gets the maximum possible water level
	 * @return the max level
	 */
	public abstract int getMaxLevel();

	/**
	 * Gets the level of a liquid
	 * @param block of the liquid
	 * @return the level
	 */
	public abstract int getLevel(Block block);

	/**
	 * Gets the level a liquid receives from nearby blocks
	 * @param block of the liquid
	 * @return the level
	 */
	public abstract int getReceivingLevel(Block block);

	/**
	 * Sets the level of a liquid<br>
	 * A level of 0 or below converts the liquid into air
	 * @param block of the liquid
	 * @param level to set to
	 */
	public abstract void setLevel(Block block, int level);

	public boolean isFlowing() {
		return flowing;
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}
}
