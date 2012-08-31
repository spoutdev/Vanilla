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
package org.spout.vanilla.world.generator.normal.biome;

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.block.plant.TallGrass;
import org.spout.vanilla.world.generator.VanillaBiome;
import org.spout.vanilla.world.generator.normal.decorator.TallGrassDecorator.TallGrassFactory;
import org.spout.vanilla.world.generator.normal.decorator.TreeDecorator.TreeWGOFactory;
import org.spout.vanilla.world.generator.normal.object.tree.TreeObject;
import org.spout.vanilla.world.generator.object.VanillaObjects;

public abstract class NormalBiome extends VanillaBiome {
	// a perlin for determining groud cover depth
	protected static final Perlin BLOCK_REPLACER = new Perlin();
	// elevation values
	protected float min;
	protected float max;

	static {
		BLOCK_REPLACER.setFrequency(0.35);
		BLOCK_REPLACER.setLacunarity(1);
		BLOCK_REPLACER.setNoiseQuality(NoiseQuality.FAST);
		BLOCK_REPLACER.setPersistence(0.7);
		BLOCK_REPLACER.setOctaveCount(1);
	}

	protected NormalBiome(int biomeId, Decorator... decorators) {
		super(biomeId, decorators);
	}

	public int placeGroundCover(World world, int x, int y, int z) {
		BLOCK_REPLACER.setSeed((int) world.getSeed() * 127);
		return 0;
	}

	protected void setMinMax(float min, float max) {
		this.min = min;
		this.max = max;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public static class NormalTreeWGOFactory implements TreeWGOFactory {
		@Override
		public TreeObject make(Random random) {
			if (random.nextInt(10) == 0) {
				return VanillaObjects.BIG_OAK_TREE;
			} else {
				return VanillaObjects.SMALL_OAK_TREE;
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
}
