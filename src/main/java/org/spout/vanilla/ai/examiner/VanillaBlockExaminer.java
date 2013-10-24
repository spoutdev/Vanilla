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
package org.spout.vanilla.ai.examiner;

import java.util.Set;

import com.google.common.collect.Sets;

import org.spout.api.ai.pathfinder.BlockExaminer;
import org.spout.api.ai.pathfinder.BlockSource;
import org.spout.api.ai.pathfinder.PathPoint;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;

import org.spout.math.vector.Vector3f;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.plant.TallGrass;

public class VanillaBlockExaminer implements BlockExaminer {
	private boolean canStandIn(Material mat) {
		return PASSABLE.contains(mat);
	}

	private boolean canStandOn(Material mat) {
		return !UNWALKABLE.contains(mat);
	}

	private boolean contains(Material[] search, Material... find) {
		for (Material haystack : search) {
			for (Material needle : find) {
				if (haystack.equals(needle)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public float getCost(BlockSource source, PathPoint point) {
		Vector3f pos = point.getVector();
		Material above = source.getMaterialAt(pos.add(UP));
		Material below = source.getMaterialAt(pos.add(DOWN));
		Material in = source.getMaterialAt(pos);
		if (above == VanillaMaterials.WEB || in == VanillaMaterials.WEB) {
			return 1F;
		}
		if (below == VanillaMaterials.SOUL_SAND || below == VanillaMaterials.ICE) {
			return 1F;
		}
		if (isLiquid(above, below, in)) {
			return 0.5F;
		}
		return 0.5F; // TODO: add light level-specific costs
	}

	private boolean isLiquid(Material... materials) {
		return contains(materials, VanillaMaterials.WATER, VanillaMaterials.STATIONARY_WATER, VanillaMaterials.LAVA, VanillaMaterials.STATIONARY_LAVA);
	}

	@Override
	public boolean isPassable(BlockSource source, PathPoint point) {
		Vector3f pos = point.getVector();
		Material above = source.getMaterialAt(pos.add(UP));
		Material below = source.getMaterialAt(pos.add(DOWN));
		Material in = source.getMaterialAt(pos);
		if (!(below instanceof BlockMaterial) || !canStandOn(below)) {
			return false;
		}
		return !(!canStandIn(above) || !canStandIn(in));
	}

	private static final Vector3f DOWN = new Vector3f(0, -1, 0);
	private static final Set<Material> PASSABLE = Sets.newHashSet(VanillaMaterials.AIR, VanillaMaterials.DEAD_BUSH, VanillaMaterials.RAIL_DETECTOR, VanillaMaterials.REDSTONE_REPEATER,
			VanillaMaterials.REDSTONE_REPEATER_OFF, VanillaMaterials.REDSTONE_REPEATER_ON, VanillaMaterials.FENCE_GATE, VanillaMaterials.ITEM_FRAME, VanillaMaterials.LADDER, VanillaMaterials.LEVER,
			TallGrass.TALL_GRASS, VanillaMaterials.MELON_STEM, VanillaMaterials.NETHER_BRICK_FENCE, VanillaMaterials.PUMPKIN_STEM, VanillaMaterials.RAIL_POWERED, VanillaMaterials.RAIL,
			VanillaMaterials.ROSE, VanillaMaterials.RED_MUSHROOM, VanillaMaterials.REDSTONE_DUST, VanillaMaterials.REDSTONE_TORCH_OFF, VanillaMaterials.REDSTONE_TORCH_OFF,
			VanillaMaterials.REDSTONE_WIRE, VanillaMaterials.SIGN, VanillaMaterials.SIGN_POST, VanillaMaterials.SNOW, VanillaMaterials.STRING, VanillaMaterials.STONE_BUTTON,
			VanillaMaterials.SUGAR_CANE_BLOCK, VanillaMaterials.TRIPWIRE, VanillaMaterials.VINES, VanillaMaterials.WALL_SIGN, VanillaMaterials.WHEAT, VanillaMaterials.WATER, VanillaMaterials.WEB,
			VanillaMaterials.WOOD_BUTTON, VanillaMaterials.WOODEN_DOOR, VanillaMaterials.STATIONARY_WATER);
	private static final Set<Material> UNWALKABLE = Sets.union(Sets.newHashSet(VanillaMaterials.AIR, VanillaMaterials.LAVA, VanillaMaterials.STATIONARY_LAVA, VanillaMaterials.CACTUS), PASSABLE);
	private static final Vector3f UP = new Vector3f(0, 1, 0);
}
