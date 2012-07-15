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

import org.spout.api.Source;
import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.material.VanillaBlockMaterial;

public abstract class Liquid extends VanillaBlockMaterial implements DynamicMaterial, Source {
	private final boolean flowing;
	public static final int MAX_HOLE_DISTANCE = 5;

	public Liquid(String name, int id, boolean flowing) {
		super(name, id);
		this.flowing = flowing;
		this.setLiquidObstacle(false).setHardness(100.0F).setResistance(166.7F).setOpacity(2).setCollision(CollisionStrategy.SOFT);
		this.clearDropMaterials();
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		block.dynamicUpdate(block.getWorld().getAge() + this.getFlowDelay());
	}

	@Override
	public boolean canDrop(Block block, ItemStack holding) {
		return false;
	}

	private boolean onFlow(Block block) {
		// Flow below, and if not possible, spread outwards
		if (this.onFlow(block, BlockFace.BOTTOM)) {
			return true;
		} else {
			// Find out all the hole distance
			int distance = Integer.MAX_VALUE;
			int i = 0;
			int[] distances = new int[4];
			for (BlockFace face : BlockFaces.NESW) {
				distances[i] = this.getHoleDistance(block.translate(face));
				if (distances[i] != -1 && distances[i] < distance) {
					distance = distances[i];
				}
				i++;
			}
			boolean flowed = false;
			if (distance == Integer.MAX_VALUE) {
				// No hole found, flow in all directions
				for (BlockFace face : BlockFaces.NESW) {
					flowed |= this.onFlow(block, face);
				}
			} else {
				// Flow to all matching directions
				for (i = 0; i < distances.length; i++) {
					if (distances[i] != -1 && distances[i] <= distance) {
						flowed |= this.onFlow(block, BlockFaces.NESW.get(i));
					}
				}
			}
			return flowed;
		}
	}

	/**
	 * Let's this liquid flow from the block to the direction given
	 * 
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
		Block spread = block.getWorld().getBlock(block.getX(), block.getY(), block.getZ(), this).translate(to);
		BlockMaterial spreadMat = spread.getMaterial();
		if (this.isMaterial(spreadMat)) {
			if (this.isSource(spread)) {
				// If the block above was a non-flowing source, return false to make it spread outwards
				// If the block above was not a source, return true to stop spreading
				return !this.isSource(block) || this.isFlowingDown(block);
			} else {
				// Compare levels
				if (level > this.getLevel(spread)) {
					if (spreadMat != this.getFlowingMaterial()) {
						// Make sure the material is adjusted
						spread.setMaterial(this.getFlowingMaterial(), spread.getData());
					}
					this.setLevel(spread, level);
					if (to == BlockFace.BOTTOM) {
						this.setFlowingDown(spread, true);
					}
					// Update blocks around
					return true;
				}
			}
		} else if (!isLiquidObstacle(spreadMat)) {
			// Create a new liquid
			this.onSpread(spread, level, to.getOpposite());
			return true;
		}
		return false;
	}

	public static boolean isLiquidObstacle(BlockMaterial material) {
		if (material instanceof VanillaBlockMaterial) {
			if (((VanillaBlockMaterial) material).isLiquidObstacle()) {
				return true;
			}
		} else if (material.isPlacementObstacle()) {
			return true;
		}
		return false;
	}

	/**
	 * Called when this liquid created a new liquid because it spread
	 * @param block of the Liquid that got created
	 * @param from where it spread
	 * @return True to notify spreading was allowed, False to deny
	 */
	public void onSpread(Block block, int newLevel, BlockFace from) {
		block.getMaterial().onDestroy(block);
		block.setMaterial(this.getFlowingMaterial());
		this.setLevel(block, newLevel);
		if (from == BlockFace.TOP) {
			this.setFlowingDown(block, true);
		}
	}

	/**
	 * Gets the maximum possible liquid level
	 * 
	 * @return the max level
	 */
	public abstract int getMaxLevel();

	/**
	 * Gets the liquid type to use for flowing liquids
	 * 
	 * @return the flowing material
	 */
	public abstract Liquid getFlowingMaterial();

	/**
	 * Gets the liquid type to use for stationary liquids
	 * 
	 * @return the stationary material
	 */
	public abstract Liquid getStationaryMaterial();

	/**
	 * Checks if the material given is either the flowing or stationary type of this Liquid
	 * 
	 * @param material to check
	 * @return True if it is this liquid, False if not
	 */
	@Override
	public boolean isMaterial(Material... materials) {
		for (Material material : materials) {
			if (material.equals(this.getFlowingMaterial(), this.getStationaryMaterial())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Material... materials) {
		return super.isMaterial(materials);
	}

	/**
	 * Gets the level of a liquid
	 * 
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
	 * Gets the delay in milliseconds between updates of this liquid
	 * @return the millisecond delay of this Liquid
	 */
	public abstract int getFlowDelay();

	/**
	 * Sets whether this liquid is flowing down
	 * @param block of the liquid
	 * @param flowing down state of the liquid
	 */
	public void setFlowingDown(Block block, boolean flowing) {
		block.setDataBits(0x8, flowing);
	}

	/**
	 * Gets whether this liquid is flowing down
	 * @param block of the liquid
	 * @return True if is flowing down, False if not
	 */
	public boolean isFlowingDown(Block block) {
		return block.isDataBitSet(0x8);
	}

	/**
	 * Gets whether this liquid is a source
	 * @param block of the liquid
	 * @return True if it is a source, False if not
	 */
	public boolean isSource(Block block) {
		return block.getDataField(0x7) == 0x0;
	}

	public boolean isFlowing() {
		return flowing;
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
	}

	/**
	 * Gets the distance to the nearest hole<br>
	 * Returns -1 if no hole was found
	 * @param from which block to start looking
	 * @return the hole distance
	 */
	public int getHoleDistance(Block from) {
		return getHoleDistance(from, 0, MAX_HOLE_DISTANCE);
	}

	/**
	 * Gets the distance to the nearest hole<br>
	 * Returns -1 if no hole was found
	 * @param from which block to start looking
	 * @param currentDistance to compare with maxDistance
	 * @param maxDistance after which to stop searching
	 * @return the hole distance
	 */
	public int getHoleDistance(Block from, int currentDistance, int maxDistance) {
		//TODO: Try to implement this system in a model-like structure, like explosion models
		if (currentDistance >= maxDistance || isLiquidObstacle(from.getMaterial())) {
			// Break, because we can not flow through obstacles
			return -1;
		} else if (this.isMaterial(from.getMaterial()) && this.isSource(from)) {
			// Break, because we can not flow towards another source block
			return -1;
		} else if (!isLiquidObstacle(from.translate(BlockFace.BOTTOM).getMaterial())) {
			// Found a hole
			return 0;
		}
		currentDistance++;
		int distance = Integer.MAX_VALUE;
		int selfDistance;
		for (BlockFace face : BlockFaces.NESW) {
			selfDistance = getHoleDistance(from.translate(face), currentDistance, maxDistance);
			if (selfDistance != -1 && selfDistance < distance) {
				distance = selfDistance;
			}
		}
		if (distance == Integer.MAX_VALUE) {
			return -1;
		} else {
			return distance + 1;
		}
	}

	@Override
	public void onDynamicUpdate(Block block, Region r, long updateTime, long lastUpdateTime, int data, Object hint) {
		// Update flowing down state
		if (this.isMaterial(block.translate(BlockFace.TOP).getMaterial())) {
			// Set non-source water blocks to flow down
			if (!this.isSource(block)) {
				this.setFlowingDown(block, true);
				this.setLevel(block, this.getMaxLevel());
			}
		} else {
			// Undo state
			if (this.isFlowingDown(block)) {
				this.setFlowingDown(block, false);
				this.setLevel(block, 0);
			}
		}
		// Update liquid level for non-source blocks
		if (!this.isSource(block)) {
			int counter = 0;
			int oldlevel = this.getLevel(block);
			int newlevel = -2;
			Block neigh;
			for (BlockFace face : BlockFaces.NESW) {
				neigh = block.translate(face);
				if (this.isMaterial(neigh.getMaterial())) {
					newlevel = Math.max(newlevel, this.getLevel(neigh) - 1);
					if (this.hasFlowSource() && this.isSource(neigh) && !this.isFlowingDown(neigh)) {
						counter++;
						if (counter >= 2) {
							newlevel = this.getMaxLevel();
							break;
						}
					}
				}
			}
			// Compare old and new levels
			if (newlevel != oldlevel) {
				this.setLevel(block, newlevel);
				if (newlevel < oldlevel) {
					// Don't flow when level is reduced
					return;
				}
			}
		}
		// Flow outwards
		this.onFlow(block);
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS_AND_NEIGHBORS;
	}
}
