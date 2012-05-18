/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.redstone.RedstoneSource;

public class RedstoneUtil {
	/**
	 * Checks if the block given is a redstone conductor
	 * @param block to check
	 * @return True if it is a redstone conductor
	 */
	public static boolean isConductor(Block block) {
		return isConductor(block.getSubMaterial());
	}

	/**
	 * Checks if the block material given is a redstone conductor
	 * @param mat to check
	 * @return True if it is a redstone conductor
	 */
	public static boolean isConductor(BlockMaterial mat) {
		return mat instanceof VanillaBlockMaterial && ((VanillaBlockMaterial) mat).isRedstoneConductor();
	}

	public static boolean isReceivingPower(Block block, boolean wiresAttach) {
		if (wiresAttach) {
			for (BlockFace face : BlockFaces.BTEWNS) {
				if (isPowered(block.translate(face), face.getOpposite())) {
					return true;
				}
			}
		} else if (isPowered(block.translate(BlockFace.TOP), BlockFace.BOTTOM)) {
			return true;
		} else {
			Block relBlock;
			for (BlockFace face : BlockFaces.NESWB) {
				relBlock = block.translate(face);
				if (relBlock.getMaterial().equals(VanillaMaterials.REDSTONE_WIRE)) {
					return VanillaMaterials.REDSTONE_WIRE.hasRedstonePowerTo(relBlock, face.getOpposite(), RedstonePowerMode.ALL);
				} else if (isPowered(relBlock, face.getOpposite(), RedstonePowerMode.ALLEXCEPTWIRE)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isReceivingPower(Block block) {
		return isReceivingPower(block, true);
	}

	public static boolean isPowered(Block block) {
		return isPowered(block, RedstonePowerMode.ALL);
	}

	public static boolean isPowered(Block block, BlockFace to) {
		return isPowered(block, to, RedstonePowerMode.ALL);
	}

	public static boolean isPowered(Block block, RedstonePowerMode powerMode) {
		return isPowered(block, BlockFace.THIS, powerMode);
	}

	public static boolean isPowered(Block block, BlockFace to, RedstonePowerMode powerMode) {
		BlockMaterial mat = block.getSubMaterial();
		if (mat instanceof VanillaBlockMaterial) {
			if (((VanillaBlockMaterial) mat).hasRedstonePower(block, powerMode)) {
				return true;
			} else if (to != BlockFace.THIS && mat instanceof RedstoneSource) {
				return ((RedstoneSource) mat).hasRedstonePowerTo(block, to, powerMode);
			}
		}
		return false;
	}
}
