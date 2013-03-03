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
package org.spout.vanilla.world.generator.skylands.biome;

import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.annotated.Load;
import org.spout.api.util.config.annotated.Save;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.biome.VanillaBiome;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome.NormalTallGrassFactory;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome.NormalTreeWGOFactory;
import org.spout.vanilla.world.generator.normal.decorator.EmeraldOreDecorator;
import org.spout.vanilla.world.generator.normal.decorator.FlowerDecorator;
import org.spout.vanilla.world.generator.normal.decorator.PumpkinDecorator;
import org.spout.vanilla.world.generator.normal.decorator.TallGrassDecorator;
import org.spout.vanilla.world.generator.normal.decorator.TreeDecorator;
import org.spout.vanilla.world.generator.normal.populator.GroundCoverPopulator.GroundCoverBiome;
import org.spout.vanilla.world.generator.normal.populator.GroundCoverPopulator.GroundCoverLayer;
import org.spout.vanilla.world.generator.normal.populator.GroundCoverPopulator.GroundCoverUniformLayer;
import org.spout.vanilla.world.generator.normal.populator.GroundCoverPopulator.GroundCoverVariableLayer;
import org.spout.vanilla.world.generator.skylands.SkylandsGenerator;

public class SkylandsBiome extends VanillaBiome implements GroundCoverBiome {
	private GroundCoverLayer[] groundCover = new GroundCoverLayer[]{
			new GroundCoverUniformLayer(VanillaMaterials.GRASS, VanillaMaterials.GRASS, (byte) 1),
			new GroundCoverVariableLayer(VanillaMaterials.DIRT, VanillaMaterials.DIRT, (byte) 1, (byte) 4)
	};

	public SkylandsBiome(int biomeId) {
		super(biomeId);
		final TreeDecorator trees = new TreeDecorator();
		trees.setFactory(new NormalTreeWGOFactory());
		final TallGrassDecorator tallGrass = new TallGrassDecorator();
		tallGrass.setFactory(new NormalTallGrassFactory());
		final EmeraldOreDecorator emeraldOre = new EmeraldOreDecorator();
		emeraldOre.setMinimumElevation(SkylandsGenerator.MINIMUM);
		emeraldOre.setElevationRandomness(12);
		emeraldOre.setBaseAmount(2);
		emeraldOre.setRandomAmount(4);
		addDecorators(trees, new FlowerDecorator(), tallGrass, new PumpkinDecorator(), emeraldOre);
	}

	@Override
	public GroundCoverLayer[] getGroundCover() {
		return groundCover;
	}

	@Override
	public String getName() {
		return "Skylands";
	}

	@Load
	private void load(ConfigurationNode node) {
		final ConfigurationNode groundCoverNode = node.getNode("ground-cover");
		final int count = groundCoverNode.getKeys(false).size();
		if (count == 0) {
			save(node);
			return;
		}
		groundCover = new GroundCoverLayer[count];
		for (int i = 0; i < count; i++) {
			groundCover[i] = GroundCoverLayer.loadNew(groundCoverNode.getNode(Integer.toString(i + 1)));
		}
	}

	@Save
	private void save(ConfigurationNode node) {
		final ConfigurationNode groundCoverNode = node.getNode("ground-cover");
		byte number = 0;
		for (GroundCoverLayer layer : groundCover) {
			layer.save(groundCoverNode.getNode(Byte.toString(++number)));
		}
	}
}
