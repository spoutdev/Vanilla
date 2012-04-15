/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.generator.normal.biome;

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.generator.normal.decorator.BeachDecorator;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.generator.VanillaBiomeType;

public class OceanBiome extends VanillaBiomeType {
	private Perlin base = new Perlin();
	private Turbulence noise = new Turbulence();
	@SuppressWarnings("unused")
	private int blocksHit = 0;
	@SuppressWarnings("unused")
	private static Random rand = new Random();

	public OceanBiome() {
		super(0, new BeachDecorator());
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
		final int height = (int) ((noise.GetValue(x / 16.0 + 0.005, 0.05, z / 16.0 + 0.005) + 1.0) * 4.0 + 4);

		int y = chunkY * 16;

		for (int dy = y; dy < y + 16; dy++) {
			if (dy >= 63 || dy < 0) {
				continue;
			}
			blockData.set(x, dy, z, getBlockIdByLayer(height, dy));
		}
	}

	protected short getBlockIdByLayer(int top, int dy) {
		short id;
		if (dy >= top) {
			id = VanillaMaterials.WATER.getId();
		} else if (dy + 4 >= top) {
			id = VanillaMaterials.SAND.getId();
		} else if (dy + 7 >= top) {
			id = VanillaMaterials.DIRT.getId();
		} else if (dy > 0 ) {
			id = VanillaMaterials.STONE.getId();
		} else {
			id = VanillaMaterials.BEDROCK.getId();
		}
		return id;
	}

	@Override
	public String getName() {
		return "Ocean";
	}
}
