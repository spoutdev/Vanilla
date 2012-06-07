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
package org.spout.vanilla.material.block;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.util.LogicUtil;

import org.spout.vanilla.material.VanillaBlockMaterial;

public abstract class Liquid extends VanillaBlockMaterial implements DynamicMaterial {

	private final boolean flowing;
	private static final Vector3[] maxRange = new Vector3[]{new Vector3(1, 1, 1), new Vector3(1, 1, 1)};

	public Liquid(String name, int id, boolean flowing) {
		super(name, id);
		this.flowing = flowing;
		this.setHardness(100.0F).setResistance(166.7F).setOpacity(1).setCollision(CollisionStrategy.SOFT);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		int level;
		if (this.isSource(block)) {
			level = this.getMaxLevel();
			// Still flowing down?
			if (this.isFlowingDown(block) && !this.isMaterial(block.translate(BlockFace.TOP).getMaterial())) {
				this.setFlowingDown(block, false);
				this.setLevel(block, level - 1);
				block.update();
			} else {
				this.onFlow(block, true);
				return;
			}
		}
		// Update level of liquid
		level = this.getReceivingLevel(block);
		int oldlevel = this.getLevel(block);
		if (level != oldlevel) {
			this.setLevel(block, level);
			if (level < oldlevel) {
				block.update();
				return;
			}
		}
		this.onFlow(block, true);
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		block.setMaterial(this);
		if (this.isFlowing()) {
			block.update();
		}
		return true;
	}

	public abstract Liquid getSourceMaterial();

	public abstract Liquid getFlowingMaterial();

	public boolean isMaterial(BlockMaterial material) {
		return material.equals(this.getFlowingMaterial(), this.getSourceMaterial());
	}

	private void onFlow(Block block, boolean useTickDelay) {
		if (useTickDelay) {
			block.dynamicUpdate(this.getTickDelay() + block.getWorld().getAge());
		} else {
			// Flow below, and if not possible, spread outwards
			if (!this.onFlow(block, BlockFace.BOTTOM)) {
				for (BlockFace face : BlockFaces.NESW) {
					this.onFlow(block, face);
				}
			}
		}
	}

	/**
	 * Let's this liquid flow from the block to the direction given
	 * @param block to flow from
	 * @param to flow to
	 * @return True if flowing was successful
	 */
	public boolean onFlow(Block block, BlockFace to) {
		int level;
		if (to == BlockFace.BOTTOM) {
			level = this.getMaxLevel();
		} else {
			level = this.getLevel(block) - 1;
			if (level < 0) {
				return false;
			}
		}
		block = block.translate(to);
		BlockMaterial material = block.getMaterial();
		if (material.isPlacementObstacle()) {
			return false;
		}
		if (this.isMaterial(material) && this.isSource(block)) {
			return true;
		}
		// Actual flow logic here
		if (level > this.getLevel(block)) {
			block.setMaterial(this);
			this.setLevel(block, level);
			if (to == BlockFace.BOTTOM) {
				this.setFlowingDown(block, true);
			}
			block.update();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the maximum possible water level
	 * @return the max level
	 */
	public abstract int getMaxLevel();

	/**
	 * Gets the level a liquid receives from nearby blocks<br>
	 * The level equals the expected level of the block specified
	 * @param block of the liquid
	 * @return the level, or negative if it has no liquids nearby to use
	 */
	public int getReceivingLevel(Block block) {
		if (this.isMaterial(block.translate(BlockFace.TOP).getMaterial())) {
			return this.getMaxLevel();
		} else {
			int max = -2;
			int counter = 0;
			Block neigh;
			for (BlockFace face : BlockFaces.NESW) {
				neigh = block.translate(face);
				if (this.isMaterial(neigh.getMaterial())) {
					max = Math.max(max, this.getLevel(neigh) - 1);
					if (this.hasFlowSource() && this.isSource(neigh) && !this.isFlowingDown(neigh)) {
						counter++;
						if (counter >= 2) {
							return this.getMaxLevel();
						}
					}
				}
			}
			return max;
		}
	}

	/**
	 * Gets the level of a liquid
	 * @param block of the liquid
	 * @return the level, or negative if it has no liquid
	 */
	public abstract int getLevel(Block block);

	/**
	 * Sets the level of a liquid<br>
	 * A level lower than 0 converts the liquid into air
	 * @param block of the liquid
	 * @param level to set to
	 */
	public abstract void setLevel(Block block, int level);

	/**
	 * Gets if this liquid can create sources by flowing
	 * @return True if it can make sources when flowing, False if not
	 */
	public abstract boolean hasFlowSource();

	/**
	 * Gets the tick delay between updates of this liquid
	 * @return the tick delay of this Liquid
	 */
	public abstract int getTickDelay();

	/**
	 * Sets whether this liquid is flowing down
	 * @param block of the liquid
	 * @param flowing down state of the liquid
	 */
	public void setFlowingDown(Block block, boolean flowing) {
		block.setData(LogicUtil.setBit(block.getData(), 0x8, flowing));
	}

	/**
	 * Gets whether this liquid is flowing down
	 * @param block of the liquid
	 * @return True if is flowing down, False if not
	 */
	public boolean isFlowingDown(Block block) {
		return LogicUtil.getBit(block.getData(), 0x8);
	}

	/**
	 * Gets whether this liquid is a source
	 * @param block of the liquid
	 * @return True if it is a source, False if not
	 */
	public boolean isSource(Block block) {
		return (block.getData() & 0x7) == 0x0;
	}

	public boolean isFlowing() {
		return flowing;
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	@Override
	public Vector3[] maxRange() {
		return maxRange;
	}

	@Override
	public long onPlacement(Block b, Region r, long currentTime) {
		return currentTime + this.getTickDelay();
	}

	@Override
	public long update(Block block, Region r, long updateTime, long lastUpdateTime, Object hint) {
		this.onFlow(block, false);
		return -1;
	}
}
