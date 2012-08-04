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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.living.Creature;

/**
 * A pathfinder that uses the A*-Algorithm to find a path.
 */
public class AStarPathfinder extends Pathfinder {
	private static final int PATH_COST_LIMIT_MULTIPLIER = 5;
	private static final byte[][] DEFAULT_NEIGHBOR_MATRIX = new byte[][]{{-1, 0, 0}, {1, 0, 0}, {0, 0, -1}, {0, 0, 1}, {-1, 0, 1}, {1, 0, 1}, {-1, 0, -1}, {1, 0, -1}, {0, -1, 0},
			{0, 1, 0}, {-1, 1, 0}, {1, 1, 0}, {-1, -1, 0}, {1, -1, 0}, {0, 1, -1}, {0, 1, 1}, {0, -1, 1}, {0, -1, -1},};

	/**
	 * Creates an A*-Pathfinder.
	 * @param creature the creature that uses this Pathfinder.
	 */
	public AStarPathfinder(Creature creature) {
		super(creature);
	}

	@Override
	public List<Vector3> findPath(Vector3 from, Vector3 to) {
		Set<PathNode> closedSet = new HashSet<PathNode>();
		Queue<PathNode> openSet = new PriorityQueue<PathNode>(20);
		PathNode start = new PathNode(from.getFloorX(), from.getFloorY(), from.getFloorZ(), this);
		PathNode target = new PathNode(to.getFloorX(), to.getFloorY(), to.getFloorZ(), this);
		openSet.add(start);
		start.upate(0, null, target);
		while (!openSet.isEmpty()) {
			PathNode node = openSet.poll();
			openSet.remove(node);
			closedSet.add(node);

			if (target.equals(node)) {
				// Path was found, rebuild and return it.
				return rebuildPath(node);
			}
			byte[][] neighborsMatrix = DEFAULT_NEIGHBOR_MATRIX;
			for (byte[] neighbor : neighborsMatrix) {
				int nx = node.getX() + neighbor[0];
				int ny = node.getY() + neighbor[1];
				int nz = node.getZ() + neighbor[2];
				if (!isWalkable(nx, ny, nz)) {
					continue;
				}
				PathNode neighborNode = new PathNode(nx, ny, nz, this);
				if (closedSet.contains(neighborNode)) {
					continue;
				}
				int pathCost = node.calcCost(neighborNode);
				if (pathCost == 0) {
					closedSet.add(neighborNode);
					continue;
				}
				pathCost += node.getPathCost();
				if (pathCost > start.getTotalCost() * PATH_COST_LIMIT_MULTIPLIER) {
					continue;
				}
				if (neighborNode.getPathCost() > 0 && neighborNode.getPathCost() <= pathCost) {
					continue;
				}
				neighborNode.upate(pathCost, node, target);
				if (!openSet.contains(neighborNode)) {
					openSet.add(neighborNode);
				}
			}
		}
		// We did not find a path, return null.
		return null;
	}

	/**
	 * Rebuilds the path into an ordered List.
	 * The list is ordered from the starting node to the target node.
	 * @param target the target node.
	 * @return the ordered list.
	 */
	private List<Vector3> rebuildPath(PathNode target) {
		List<Vector3> path = new ArrayList<Vector3>();
		PathNode node = target;
		while (node != null) {
			if (node.getParent() == null) {
				break;
			}
			path.add(0, node.toVector());
			node = node.getParent();
		}
		return path;
	}
}
