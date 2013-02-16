/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.world.generator.normal.decorator;

import org.spout.vanilla.world.generator.decorator.VariableAmountDecorator;
import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.material.VanillaMaterials;

public class EmeraldOreDecorator extends VariableAmountDecorator {
	private int minimumElevation = 4;
	private int elevationRandomness = 28;

	public EmeraldOreDecorator() {
		super(3, 5);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int amount = getAmount(random);
		for (byte count = 0; count < amount; count++) {
			final int x = chunk.getBlockX(random);
			final int y = random.nextInt(elevationRandomness) + minimumElevation;
			final int z = chunk.getBlockZ(random);
			final Block block = world.getBlock(x, y, z);
			if (block.isMaterial(VanillaMaterials.STONE)) {
				block.setMaterial(VanillaMaterials.EMERALD_ORE);
			}
		}
	}

	public void setMinimumElevation(int minimumElevation) {
		this.minimumElevation = minimumElevation;
	}

	public void setElevationRandomness(int elevationRandomness) {
		this.elevationRandomness = elevationRandomness;
	}
}
