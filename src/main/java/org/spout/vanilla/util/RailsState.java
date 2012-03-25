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

import org.spout.api.material.block.BlockFace;

/**
 * Indicates the direction of a minecart track
 */
public enum RailsState {

	WEST(BlockFace.WEST, BlockFace.EAST, false),
	SOUTH(BlockFace.SOUTH, BlockFace.NORTH, false),
	SOUTH_SLOPED(BlockFace.SOUTH, BlockFace.NORTH, true),
	NORTH_SLOPED(BlockFace.NORTH, BlockFace.SOUTH, true),
	EAST_SLOPED(BlockFace.EAST, BlockFace.WEST, true),
	WEST_SLOPED(BlockFace.WEST, BlockFace.EAST, true),
	NORTH_WEST(BlockFace.NORTH, BlockFace.WEST, false),
	NORTH_EAST(BlockFace.NORTH, BlockFace.EAST, false),
	SOUTH_EAST(BlockFace.SOUTH, BlockFace.EAST, false),
	SOUTH_WEST(BlockFace.SOUTH, BlockFace.WEST, false);
	private final BlockFace[] directions;
	private final boolean curved;
	private final boolean sloped;

	public boolean isCurved() {
		return this.curved;
	}

	public boolean isSloped() {
		return this.sloped;
	}

	private RailsState(BlockFace dir1, BlockFace dir2, boolean sloped) {
		this.directions = new BlockFace[]{dir1, dir2};
		this.curved = dir1.getOpposite() != dir2;
		this.sloped = sloped;
	}

	public byte getData() {
		return (byte) this.ordinal();
	}

	public boolean isConnected(BlockFace direction) {
		return this.directions[0] == direction || this.directions[1] == direction;
	}

	public BlockFace[] getDirections() {
		return this.directions;
	}

	public static RailsState get(int data) {
		if (data >= values().length || data < 0) {
			return null;
		} else {
			return values()[data];
		}
	}

	public static RailsState get(BlockFace direction, boolean sloped) {
		for (RailsState dir : values()) {
			if (dir.isCurved()) {
				continue;
			}
			if (sloped != dir.isSloped()) {
				continue;
			}
			if (sloped) {
				if (dir.getDirections()[0] == direction) {
					return dir;
				}
			} else if (dir.isConnected(direction)) {
				return dir;
			}
		}
		return null;
	}

	public static RailsState get(BlockFace from, BlockFace to) {
		for (RailsState dir : values()) {
			if (dir.isSloped()) {
				continue;
			}
			if (dir.isConnected(from) && dir.isConnected(to)) {
				return dir;
			}
		}
		return null;
	}

}
