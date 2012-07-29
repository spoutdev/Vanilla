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
package org.spout.vanilla.controller.living.pathfinding;

import java.util.List;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.math.Vector3;
import org.spout.vanilla.controller.living.Creature;

/**
 * An abstract Pathfinder.
 */
public abstract class Pathfinder {

	protected Creature creature;

	/**
	 * Creates a Pathfinder.
	 *
	 * @param creature the creature that uses this Pathfinder.
	 */
	public Pathfinder(Creature creature) {
		this.creature = creature;
	}

	/**
	 * Finds the path from 'from' to 'to' and returns an ordered List of PathNodes.
	 *
	 * @param from the starting position.
	 * @param to the target position.
	 * @return an ordered list from start to target.
	 */
	public abstract List<Vector3> findPath(Vector3 from, Vector3 to);

	/**
	 * Checks if a block can be passed.
	 * A block can be passed when a creature can move to its position.
	 *
	 * @param block the block to check.
	 * @return true if the block is passable
	 */
	public boolean isPassable(Block block) {
		return !block.getMaterial().isSolid();
	}

	/**
	 * Checks if a block can be passed.
	 * A block can be passed when a creature can move to its position.
	 *
	 * @param x the x coordinate of the block
	 * @param y the y coordinate of the block
	 * @param z the z coordinate of the block
	 * @return true if the block is passable, false otherwise
	 */
	public boolean isPassable(int x, int y, int z) {
		return !creature.getParent().getWorld().getBlockMaterial(x, y, z).isSolid();
	}

	/**
	 * Checks if a position is walkable.
	 * A position is walkable if the block, which is represented by the position, can be passed,
	 * and the block beneath the position is solid.
	 *
	 * @param x the x coordinate of the block
	 * @param y the y coordinate of the block
	 * @param z the z coordinate of the block
	 * @return true if the block is walkable, false otherwise
	 */
	public boolean isWalkable(int x, int y, int z) {
		return isPassable(x, y, z) && !isPassable(x, y - 1, z);
	}

	/**
	 * Checks if a position is walkable.
	 * A position is walkable if the block, which is represented by the position, can be passed,
	 * and the block beneath the position is solid.
	 *
	 * @param vector the position in question
	 * @return true if the block is walkable, false otherwise
	 */
	public boolean isWalkable(Vector3 vector) {
		return isWalkable(vector.getFloorX(), vector.getFloorY(), vector.getFloorZ());
	}
}
