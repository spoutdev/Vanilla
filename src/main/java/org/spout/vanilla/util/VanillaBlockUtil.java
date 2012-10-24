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
package org.spout.vanilla.util;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Player;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.IntVector3;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.block.BlockActionEvent;
import org.spout.vanilla.material.VanillaMaterials;

public class VanillaBlockUtil {
	private static final EffectRange FARMLAND_CHECK_RANGE = new CuboidEffectRange(-1, -1, -1, 1, -1, 1);

	/**
	 * Gets if rain is falling nearby the block specified
	 * @param block to check it nearby of
	 * @return True if it is raining, False if not
	 */
	public static boolean hasRainNearby(Block block) {
		if (block.getWorld().getComponentHolder().getData().get(VanillaData.WEATHER).isRaining()) {
			for (BlockFace face : BlockFaces.NESW) {
				if (block.translate(face).isAtSurface()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets if rain is falling on top of the block specified
	 * @param block to check
	 * @return True if rain is falling on the Block, false if not
	 */
	public static boolean isRaining(Block block) {
		return block.getWorld().getComponentHolder().getData().get(VanillaData.WEATHER).isRaining() && block.isAtSurface();
	}

	/**
	 * Gets the chance of a crop block growing<br>
	 * The higher the value, the lower the chance.
	 * @param block to check
	 * @return the growth chance
	 */
	public static int getCropGrowthChance(Block block) {
		BlockMaterial material = block.getMaterial();
		float rate = 1.0f;
		float farmLandRate;
		Block rel;
		for (IntVector3 coord : FARMLAND_CHECK_RANGE) {
			rel = block.translate(coord);
			if (rel.isMaterial(VanillaMaterials.FARMLAND)) {
				if (VanillaMaterials.FARMLAND.isWet(rel)) {
					farmLandRate = 3.0f;
				} else {
					farmLandRate = 1.0f;
				}
				if (rel.getX() != 0 && rel.getZ() != 0) {
					// this is farmland at a neighbor
					farmLandRate /= 4.0f;
				}
				rate += farmLandRate;
			}
		}

		// Half, yes or no? Check for neighboring crops
		if (block.translate(-1, 0, -1).isMaterial(material) || block.translate(1, 0, 1).isMaterial(material)
				|| block.translate(-1, 0, 1).isMaterial(material) || block.translate(1, 0, -1).isMaterial(material)) {
			return (int) (50f / rate);
		}
		if ((block.translate(-1, 0, 0).isMaterial(material) || block.translate(1, 0, 0).isMaterial(material))
				&& (block.translate(0, 0, -1).isMaterial(material) || block.translate(0, 0, 1).isMaterial(material))) {
			return (int) (50f / rate);
		}
		return (int) (25f / rate);
	}

	/**
	 * Gets a vertical column of chunks
	 * @param middle chunk
	 * @return list of chunks in the column
	 */
	public static List<Chunk> getChunkColumn(Chunk middle) {
		Chunk top = middle;
		Chunk tmp;
		while (true) {
			tmp = top.getRelative(BlockFace.TOP, LoadOption.NO_LOAD);
			if (tmp != null && tmp.isLoaded()) {
				top = tmp;
			} else {
				break;
			}
		}
		List<Chunk> rval = new ArrayList<Chunk>();
		while (top != null && top.isLoaded()) {
			rval.add(top);
			top = top.getRelative(BlockFace.BOTTOM, LoadOption.NO_LOAD);
		}
		return rval;
	}
}
