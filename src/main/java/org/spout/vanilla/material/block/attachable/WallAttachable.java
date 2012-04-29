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
package org.spout.vanilla.material.block.attachable;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

public class WallAttachable extends AbstractAttachable {
	public WallAttachable(String name, int id) {
		super(name, id);
	}

	@Override
	public short getDataForFace(BlockFace face) {
		switch (face) {
			case NORTH:
				return 0x1;
			case SOUTH:
				return 0x2;
			case EAST:
				return 0x3;
			case WEST:
				return 0x4;
			default:
				return 0x5; //Standing on floor, this will be default if other faces are passed, too
		}
	}

	@Override
	public BlockFace getFaceAttachedTo(short data) {
		switch (data) {
			case 0x1:
				return BlockFace.NORTH;
			case 0x2:
				return BlockFace.SOUTH;
			case 0x3:
				return BlockFace.EAST;
			case 0x4:
				return BlockFace.WEST;
			case 0x5:
				return BlockFace.BOTTOM;
			default:
				return BlockFace.BOTTOM;
		}
	}
	
	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		switch (face) {
		case BOTTOM :
		case NORTH :
		case EAST :
		case SOUTH :
		case WEST :
			return super.canAttachTo(block, face);
		}
		return false;
	}
}
