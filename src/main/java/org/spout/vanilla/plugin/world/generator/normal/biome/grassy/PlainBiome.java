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
package org.spout.vanilla.plugin.world.generator.normal.biome.grassy;

import java.awt.Color;

import org.spout.vanilla.plugin.world.generator.normal.decorator.FlowerDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.MushroomDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.PumpkinDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.SandAndClayDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.SugarCaneDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.TallGrassDecorator;

public class PlainBiome extends GrassyBiome {
	public PlainBiome(int id) {
		super(id, new SandAndClayDecorator(), new FlowerDecorator((byte) 4),
				new TallGrassDecorator(new NormalTallGrassFactory(), (byte) 5),
				new MushroomDecorator(), new SugarCaneDecorator(), new PumpkinDecorator());
		setMinMax(64, 75);
		setGrassColorMultiplier(new Color(145, 189, 89));
		setFoliageColorMultiplier(new Color(119, 171, 47));
	}

	@Override
	public String getName() {
		return "Plains";
	}
}
