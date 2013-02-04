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
import java.util.Random;

import org.spout.vanilla.plugin.world.generator.normal.decorator.DeadBushDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.LilyPadDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.MushroomDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.PumpkinDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.SandAndClayDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.SugarCaneDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.TallGrassDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.TreeDecorator;
import org.spout.vanilla.plugin.world.generator.normal.object.variableheight.tree.SwampTreeObject;
import org.spout.vanilla.plugin.world.generator.normal.object.variableheight.tree.TreeObject;

public class SwampBiome extends GrassyBiome {
	public SwampBiome(int biomeId) {
		super(biomeId, new SandAndClayDecorator(), new TreeDecorator(new SwampTreeWGOFactory()),
				new TallGrassDecorator(new NormalTallGrassFactory()), new DeadBushDecorator(), new LilyPadDecorator(),
				new MushroomDecorator((byte) 1, (byte) 3), new SugarCaneDecorator((byte) 15, (byte) 2),
				new PumpkinDecorator());
		setElevation(56, 66);
		setGrassColorMultiplier(new Color(92, 105, 78));
		setFoliageColorMultiplier(new Color(73, 97, 55));
		setWaterColorMultiplier(new Color(224, 255, 174));
	}

	@Override
	public String getName() {
		return "Swampland";
	}

	private static class SwampTreeWGOFactory extends NormalTreeWGOFactory {
		@Override
		public byte amount(Random random) {
			return (byte) (2 + super.amount(random));
		}

		@Override
		public TreeObject make(Random random) {
			return new SwampTreeObject();
		}
	}
}
