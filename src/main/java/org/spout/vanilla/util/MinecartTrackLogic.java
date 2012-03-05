/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.util;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.block.MinecartTrack;

public class MinecartTrackLogic {
	public int x, y, z;
	public World world;
	public MinecartTrack material;
	public BlockFace currentDirection;
	public boolean isPowered;

	public MinecartTrackLogic(MinecartTrack material, World world, int x, int y, int z) {
		this.material = material;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.isPowered = false; //TODO: find out powered state
		this.updateDirection();
	}

	public void updateDirection() {
		//TODO: Get direction from the block data
		//this.currentDirection = 
	}

	public static MinecartTrackLogic create(World world, int x, int y, int z) {
		BlockMaterial mat = world.getBlockMaterial(x, y, z);
		if (mat instanceof MinecartTrack) {
			return new MinecartTrackLogic((MinecartTrack) mat, world, x, y, z);
		} else {
			return null;
		}
	}

	private boolean isMinecartTrack(int x, int y, int z) {
		if (world.getBlockMaterial(x, y, z) instanceof MinecartTrack) {
			return true;
		}
		if (world.getBlockMaterial(x, y + 1, z) instanceof MinecartTrack) {
			return true;
		}
		if (world.getBlockMaterial(x, y - 1, z) instanceof MinecartTrack) {
			return true;
		}
		return false;
	}

	private MinecartTrackLogic getLogic(int x, int y, int z) {
		MinecartTrackLogic logic;
		logic = create(this.world, x, y, z);
		if (logic != null) {
			return logic;
		}
		logic = create(this.world, x, y + 1, z);
		if (logic != null) {
			return logic;
		}
		logic = create(this.world, x, y - 1, z);
		return logic;
	}

	public boolean isConnected(MinecartTrackLogic track) {
		//TODO: Check if this track connects to the given track
		return false;
	}

	public boolean canConnect(MinecartTrackLogic to) {
		if (isConnected(to)) {
			return true;
		} else {
			//TODO: Check if a connection can be made
			return false;
		}
	}

	private static BlockFace toStraightTrack(BlockFace face) {
		switch (face) {
			case NORTH:
			case SOUTH:
				return BlockFace.SOUTH;
			case EAST:
			case WEST:
				return BlockFace.WEST;
			default:
				return face;
		}
	}

	public boolean connect(BlockFace direction) {
		if (this.material.canCurve()) {
			//TODO: Force this track to face to the given direction
			//find out what other directions to look at
		} else {
			this.currentDirection = toStraightTrack(direction);
		}
		//TODO: Update block data
		return false;
	}

	public void refresh() {
		//TODO: Update this track piece based on environment
		//check if connections are made, and if new connections need to be made
	}
}
