/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiome;
import org.spout.vanilla.world.generator.normal.decorator.BeachDecorator;
import org.spout.vanilla.world.generator.normal.decorator.CaveDecorator;
import org.spout.vanilla.world.generator.normal.decorator.DungeonDecorator;
import org.spout.vanilla.world.generator.normal.decorator.FlowerDecorator;
import org.spout.vanilla.world.generator.normal.decorator.GrassDecorator;
import org.spout.vanilla.world.generator.normal.decorator.PondDecorator;
import org.spout.vanilla.world.generator.normal.decorator.TreeDecorator;
import org.spout.vanilla.world.populator.SmoothPopulator;

public class PlainBiome extends VanillaBiome {
	private Perlin base = new Perlin();
	private Turbulence noise = new Turbulence();

	public PlainBiome(int id) {
		super(id, new SmoothPopulator(), new CaveDecorator(), new GrassDecorator(), new FlowerDecorator(), new PondDecorator(), new BeachDecorator(), new TreeDecorator(), new DungeonDecorator());
		base.setNoiseQuality(NoiseQuality.BEST);
		base.setOctaveCount(6);
		base.setFrequency(0.3);
		base.setPersistence(0.12);
		base.setLacunarity(0.5);
		noise.SetSourceModule(0, base);
		noise.setFrequency(0.3);
		noise.setRoughness(2);
		noise.setPower(0.5);
	}

	@Override
	public void generateColumn(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		base.setSeed((int) blockData.getWorld().getSeed());
		noise.setSeed((int) blockData.getWorld().getSeed());

		final int y = chunkY * 16;
		final int height = (int) ((noise.GetValue(x / 16.0 + 0.005, 0.05, z / 16.0 + 0.005) + 1.0) * 4.0 + 60.0);

		for (int dy = y; dy < y + 16; dy++) {
			blockData.set(x, dy, z, getBlockId(height, dy));
		}
	}

	@Override
	public String getName() {
		return "Plains";
	}

	protected short getBlockId(int top, int dy) {
		short id;
		if (dy > top) {
			id = VanillaMaterials.AIR.getId();
		} else if (dy == top && dy >= 63) {
			id = VanillaMaterials.GRASS.getId();
		} else if (dy + 4 >= top) {
			id = VanillaMaterials.DIRT.getId();
		} else if (dy != 0) {
			id = VanillaMaterials.STONE.getId();
		} else {
			id = VanillaMaterials.BEDROCK.getId();
		}
		return id;
	}
}
