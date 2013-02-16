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
package org.spout.vanilla.world.generator.normal.biome.grassy;

import java.awt.*;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.decorator.HugeMushroomDecorator;
import org.spout.vanilla.world.generator.normal.decorator.MushroomDecorator;
import org.spout.vanilla.world.generator.normal.decorator.SandAndClayDecorator;
import org.spout.vanilla.world.generator.normal.populator.GroundCoverPopulator;

public class MushroomBiome extends GrassyBiome {
	public MushroomBiome(int biomeId) {
		super(biomeId);
		final MushroomDecorator mushrooms = new MushroomDecorator();
		mushrooms.setOdd(1);
		addDecorators(new SandAndClayDecorator(), new HugeMushroomDecorator(), mushrooms);
		setElevation(63, 87);
		setTopCover(new GroundCoverPopulator.GroundCoverLayer[]{
					new GroundCoverPopulator.GroundCoverUniformLayer(VanillaMaterials.MYCELIUM, VanillaMaterials.DIRT, (byte) 1),
					new GroundCoverPopulator.GroundCoverVariableLayer(VanillaMaterials.DIRT, VanillaMaterials.DIRT, (byte) 1, (byte) 4)
				});
		setGrassColorMultiplier(new Color(85, 201, 63));
		setFoliageColorMultiplier(new Color(43, 187, 15));
	}

	@Override
	public String getName() {
		return "Mushroom Island";
	}
}
