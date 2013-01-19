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
package org.spout.vanilla.plugin.world.generator.normal.biome;

import java.util.Random;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.annotated.Load;
import org.spout.api.util.config.annotated.Save;
import org.spout.api.util.config.annotated.Setting;

import org.spout.vanilla.api.world.generator.biome.VanillaBiome;

import org.spout.vanilla.plugin.material.block.plant.TallGrass;
import org.spout.vanilla.plugin.world.generator.normal.decorator.TallGrassDecorator.TallGrassFactory;
import org.spout.vanilla.plugin.world.generator.normal.decorator.TreeDecorator.TreeWGOFactory;
import org.spout.vanilla.plugin.world.generator.normal.object.tree.BigTreeObject;
import org.spout.vanilla.plugin.world.generator.normal.object.tree.SmallTreeObject;
import org.spout.vanilla.plugin.world.generator.normal.object.tree.TreeObject;
import org.spout.vanilla.plugin.world.generator.normal.populator.GroundCoverPopulator.GroundCoverLayer;

public abstract class NormalBiome extends VanillaBiome {
	// elevation values
	@Setting({"elevation", "min"})
	private float minElevation;
	@Setting({"elevation", "max"})
	private float maxElevation;
	// ground cover
	protected GroundCoverLayer[] groundCover = new GroundCoverLayer[0];

	protected NormalBiome(int biomeId, Decorator... decorators) {
		super(biomeId, decorators);
	}

	protected void setElevation(float min, float max) {
		this.minElevation = min;
		this.maxElevation = max;
	}

	public float getMinElevation() {
		return minElevation;
	}

	public float getMaxElevation() {
		return maxElevation;
	}

	protected void setTopCover(GroundCoverLayer[] groundCover) {
		this.groundCover = groundCover;
	}

	public GroundCoverLayer[] getGroundCover() {
		return groundCover;
	}

	public static class NormalTreeWGOFactory implements TreeWGOFactory {
		@Override
		public TreeObject make(Random random) {
			if (random.nextInt(10) == 0) {
				return new BigTreeObject();
			} else {
				return new SmallTreeObject();
			}
		}

		@Override
		public byte amount(Random random) {
			if (random.nextInt(10) == 0) {
				return 1;
			}
			return 0;
		}
	}

	public static class NormalTallGrassFactory implements TallGrassFactory {
		@Override
		public TallGrass make(Random random) {
			return TallGrass.TALL_GRASS;
		}
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
