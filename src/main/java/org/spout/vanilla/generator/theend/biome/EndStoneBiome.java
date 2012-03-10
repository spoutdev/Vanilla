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
package org.spout.vanilla.generator.theend.biome;

import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.generator.VanillaBiomeType;
import org.spout.vanilla.generator.theend.decorator.SpireDecorator;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

public class EndStoneBiome extends VanillaBiomeType {
	private Perlin base = new Perlin();
	private Turbulence noise = new Turbulence();

	public EndStoneBiome() {
		super(9, new SpireDecorator());
		base.setNoiseQuality(NoiseQuality.BEST);
		base.setOctaveCount(4);
		base.setFrequency(0.1);
		base.setPersistence(0.10);
		base.setLacunarity(0.4);
		noise.SetSourceModule(0, base);
		noise.setFrequency(0.2);
		noise.setRoughness(2);
		noise.setPower(0.3);
	}

	@Override
	public void generateColumn(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		base.setSeed((int) blockData.getWorld().getSeed());
		noise.setSeed((int) blockData.getWorld().getSeed());
		int y = chunkY * 16;
		double seaLevel = 60.0;
		double perlinRange = 0.005;
		double colSize = 16.0;
		int height = (int) ((noise.GetValue(x / colSize + perlinRange, 0.05, z / colSize + perlinRange) + 1.0) * 4.0 + seaLevel);
		
		for (int dy = y; dy < y + 16; dy++) {
			blockData.set(x, dy, z, getBlockId(height, dy));
		}
	}

	private short getBlockId(int noise, int dy) {
		short id;
		if (dy > noise) {
			id = VanillaMaterials.AIR.getId();
		} else {
			id = VanillaMaterials.END_STONE.getId();
		}
		return id;
	}
}
