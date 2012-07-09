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
package org.spout.vanilla.material.block.solid;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.RandomBlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.CubicEffectRange;
import org.spout.api.material.range.EffectIterator;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.IntVector3;

import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.item.tool.Spade;
import org.spout.vanilla.material.item.tool.Tool;

public class Grass extends Solid implements Mineable, RandomBlockMaterial, InitializableMaterial {
	private static final byte MIN_GROWTH_LIGHT = 7;
	private static final EffectRange GROWTH_RANGE = new CubicEffectRange(2);
	public Grass(String name, int id) {
		super(name, id);
		this.setHardness(0.6F).setResistance(0.8F);
	}

	@Override
	public void initialize() {
		this.setDropMaterial(VanillaMaterials.DIRT);
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Spade ? (short) 1 : (short) 2;
	}

	@Override
	public void onRandomTick(World world, int x, int y, int z) {
		final Random r = new Random(world.getAge());
		//Attempt to decay grass
		Block block = world.getBlock(x, y, z);
		if (block.translate(BlockFace.TOP).getMaterial().isOpaque()) {
			block.setMaterial(VanillaMaterials.DIRT);
		} else {
			//Attempt to grow grass
			Block around;
			EffectIterator iter = GROWTH_RANGE.getEffectIterator();
			while (iter.hasNext()) {
				IntVector3 next = iter.next();
				if (r.nextInt(4) == 0) {
					around = block.translate(next);
					if (around.isMaterial(VanillaMaterials.DIRT) && around.getLight() > MIN_GROWTH_LIGHT) {
						if (!around.translate(BlockFace.TOP).getMaterial().isOpaque()) {
							around.setMaterial(VanillaMaterials.GRASS);
						}
					}
				}
			}
		}
	}
}
