/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.material.block.redstone;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.RedstonePowerMode;

/**
 * Defines a material that can power other redstone target materials, excluding solid blocks<br> Includes methods to obtain the redstone power level of the material itself
 */
public interface IndirectRedstoneSource {
	/**
	 * Gets how much redstone power this redstone source block provides to the direction given.<br> This is indirect power, which only powers redstone targets, not other solid blocks
	 *
	 * @param block of this redstone source
	 * @param direction it provides power to
	 * @param powerMode to use to get the power
	 * @return how much power this block provides to the given direction
	 */
	short getIndirectRedstonePower(Block block, BlockFace direction, RedstonePowerMode powerMode);

	/**
	 * Gets if this redstone source block provides power to the direction given.<br> This is indirect power, which only powers redstone targets, not other solid blocks
	 *
	 * @param block of this redstone source
	 * @param direction it provides power to
	 * @param powerMode to use to get the power
	 * @return True if this redstone source block provides power
	 */
	boolean hasIndirectRedstonePower(Block block, BlockFace direction, RedstonePowerMode powerMode);

	/**
	 * Gets the power level of this material at a block
	 *
	 * @param block to get it of
	 * @return the redstone power level
	 */
	short getRedstonePower(Block block);

	/**
	 * Gets the power level of this material at a block
	 *
	 * @param block to get it of
	 * @param powerMode to use to find the power
	 * @return the redstone power level
	 */
	short getRedstonePower(Block block, RedstonePowerMode powerMode);

	/**
	 * Gets if this material is powered at a block
	 *
	 * @param block to get it of
	 * @return True if the block receives power
	 */
	boolean hasRedstonePower(Block block);

	/**
	 * Gets if this material is powered at a block
	 *
	 * @param block to get it of
	 * @param powerMode to use to find out the power levels
	 * @return True if the block receives power
	 */
	boolean hasRedstonePower(Block block, RedstonePowerMode powerMode);

	/**
	 * Gets whether this material acts as a redstone conductor<br> A redstone conductor can conduct power from direct redstone sources, and power other blocks indirectly
	 *
	 * @return True if it is a conductor, False if not
	 */
	boolean isRedstoneConductor();
}
