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
package org.spout.vanilla.entity.component.pathfinding;

import org.apache.commons.lang3.builder.EqualsBuilder;

import org.spout.api.math.Vector3;

/**
 * A PathNode represents a position in a path.
 */
public class PathNode implements Comparable<PathNode> {
	private int x;
	private int y;
	private int z;
	private int pathCost = 0;
	private int heuristicCost = 0;
	private int totalCost = 0;
	private Pathfinder pathfinder;
	private PathNode parent;

	/**
	 * Creates a new PathNode.
	 * @param x the x coordinate of the node
	 * @param y the y coordinate of the node
	 * @param z the z coordinate of the node
	 * @param pathfinder the pathfinder which uses this pathnode.
	 */
	public PathNode(int x, int y, int z, Pathfinder pathfinder) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.pathfinder = pathfinder;
	}

	/**
	 * Calculates the heuristic cost from this pathnode to another.
	 * @param to the other pathnode.
	 * @return the heuristic cost.
	 */
	public int calcHeuristicCost(PathNode to) {
		final int xCost = Math.abs(to.getX() - this.getX());
		final int yCost = Math.abs(to.getY() - this.getY());
		final int zCost = Math.abs(to.getZ() - this.getZ());
		return 10 * (xCost + yCost + zCost);
	}

	/**
	 * Calculates the cost from this pathnode to another.
	 * @param to the other pathnode.
	 * @return the cost.
	 */
	public int calcCost(PathNode to) {
		int pathCost = 10; // the basic cost.
		if (!pathfinder.isWalkable(to.toVector())) {
			return 0;
		}
		if (x != to.getX() || z != to.getZ()) {
			pathCost *= 1.5;
		}
		if (y != to.getY()) {
			pathCost *= 1.5;
		}
		return pathCost;
	}

	/**
	 * Updates the values of this pathnode.
	 * @param cost the new cost
	 * @param parent the parent pathnode
	 * @param target the target pathnode
	 */
	public void upate(int cost, PathNode parent, PathNode target) {
		this.parent = parent;
		this.pathCost = cost;
		this.heuristicCost = this.calcHeuristicCost(target);
		this.totalCost = cost + heuristicCost;
	}

	public Vector3 toVector() {
		return new Vector3(x, y, z);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getPathCost() {
		return pathCost;
	}

	public PathNode getParent() {
		return parent;
	}

	public int getHeuristicCost() {
		return heuristicCost;
	}

	public int getTotalCost() {
		return totalCost;
	}

	@Override
	public int compareTo(PathNode o) {
		if (this.totalCost > o.getTotalCost() || this.totalCost == 0) {
			return 1;
		} else if (this.totalCost < o.getTotalCost()) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "PathNode{x=" + x + ", y=" + y + ", z=" + z + ", g=" + pathCost + ", h=" + heuristicCost + ", f=" + totalCost + "}";
	}

	@Override
	public int hashCode() {
		return 31 * (31 * x + y) + z;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PathNode == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		PathNode other = (PathNode) obj;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(x, other.getX()).append(y, other.getY()).append(z, other.getZ()).isEquals();
	}
}
