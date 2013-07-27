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
package org.spout.vanilla.world.generator.normal.structure.stronghold;

import java.util.Random;

import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.solid.StoneBrick;
import org.spout.vanilla.world.generator.structure.BlockMaterialPicker;

public class StrongholdBlockMaterialPicker implements BlockMaterialPicker {
	private final Random random;

	public StrongholdBlockMaterialPicker(Random random) {
		this.random = random;
	}

	@Override
	public BlockMaterial get(boolean outer) {
		if (!outer) {
			return VanillaMaterials.AIR;
		}
		final float draw = random.nextFloat();
		if (draw < 0.2) {
			return StoneBrick.CRACKED_STONE;
		} else if (draw < 0.5) {
			return StoneBrick.MOSSY_STONE;
		} else if (draw < 0.55) {
			// TODO: should be stone bricks (metadata 2)
			return VanillaMaterials.SILVERFISH_STONE;
		} else {
			return StoneBrick.STONE;
		}
	}
}
