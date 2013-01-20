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

import org.spout.vanilla.plugin.world.generator.normal.decorator.FlowerDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.MushroomDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.PumpkinDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.SandAndClayDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.SugarCaneDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.TallGrassDecorator;
import org.spout.vanilla.plugin.world.generator.normal.decorator.TreeDecorator;
import org.spout.vanilla.plugin.world.generator.normal.object.tree.SmallTreeObject;
import org.spout.vanilla.plugin.world.generator.normal.object.tree.TreeObject;
import org.spout.vanilla.plugin.world.generator.normal.object.tree.TreeObject.TreeType;

public class ForestBiome extends GrassyBiome {
	public ForestBiome(int biomeId) {
		super(biomeId, new SandAndClayDecorator(), new TreeDecorator(new ForestTreeWGOFactory()),
				new FlowerDecorator(), new TallGrassDecorator(new NormalTallGrassFactory(), (byte) 3),
				new MushroomDecorator(), new SugarCaneDecorator(), new PumpkinDecorator());
		setElevation(60, 81);
		setGrassColorMultiplier(new Color(121, 192, 90));
		setFoliageColorMultiplier(new Color(89, 174, 48));
	}

	@Override
	public String getName() {
		return "Forest";
	}

	private static class ForestTreeWGOFactory extends NormalTreeWGOFactory {
		@Override
		public TreeObject make(Random random) {
			if (random.nextInt(5) == 0) {
				final SmallTreeObject tree = new SmallTreeObject();
				tree.setTreeType(TreeType.BIRCH);
				return tree;
			}
			return super.make(random);
		}

		@Override
		public byte amount(Random random) {
			return (byte) (10 + super.amount(random));
		}
	}
}
