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

package org.spout.vanilla.material.block;

import org.spout.api.geo.World;
import org.spout.vanilla.material.Block;

public interface RedstoneSource extends Block {
	/**
	 * Gets how much redstone power the block at x, y, z provides to block tx, ty, tz
	 *
	 * @param world world the blocks are in
	 * @param x	 coord of source block
	 * @param y	 coord of source block
	 * @param z	 coord of source block
	 * @param tx	coord of target block
	 * @param ty	coord of target block
	 * @param tz	coord of target block
	 * @return how much power source block provides to target block
	 */
	short getRedstonePower(World world, int x, int y, int z, int tx, int ty, int tz);

	/**
	 * Gets if the block provides power to the target block
	 *
	 * @param world world the blocks are in
	 * @param x	 coord of source block
	 * @param y	 coord of source block
	 * @param z	 coord of source block
	 * @param tx	coord of target block
	 * @param ty	coord of target block
	 * @param tz	coord of target block
	 * @return if the source block provides power to the target block
	 */
	boolean providesPowerTo(World world, int x, int y, int z, int tx, int ty, int tz);

	/**
	 * Gets if the block provides an attach point for redstone wire.
	 *
	 * @param world the blocks are in
	 * @param x	 coord of this block
	 * @param y	 coord of this block
	 * @param z	 coord of this block
	 * @param tx	coord of the wire
	 * @param ty	coord of the wire
	 * @param tz	coord of the wire
	 * @return if the block provides an attachment point from the given face
	 */
	boolean providesAttachPoint(World world, int x, int y, int z, int tx, int ty, int tz);
}
