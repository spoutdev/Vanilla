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
package org.spout.vanilla.world.generator.nether.biome;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.combiner.Displace;
import net.royawesome.jlibnoise.module.modifier.Clamp;
import net.royawesome.jlibnoise.module.modifier.ScaleBias;
import net.royawesome.jlibnoise.module.modifier.ScalePoint;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiome;

public class NetherrackBiome extends VanillaBiome {
	// main generation
	private final Perlin mainBase = new Perlin();
	private final Perlin mainDisplacer = new Perlin();
	private final Turbulence main = new Turbulence();
	// special generation for roof and floor regions
	private final ScaleBias limits = new ScaleBias();
	// bedrock generation
	private final Perlin bedrockBase = new Perlin();
	private final Clamp bedrock = new Clamp();

	public NetherrackBiome(int id) {
		super(id);

		mainBase.setFrequency(0.1D);
		mainBase.setLacunarity(1D);
		mainBase.setNoiseQuality(NoiseQuality.STANDARD);
		mainBase.setPersistence(0.7D);
		mainBase.setOctaveCount(1);

		mainDisplacer.setFrequency(0.1D);
		mainDisplacer.setLacunarity(1D);
		mainDisplacer.setNoiseQuality(NoiseQuality.STANDARD);
		mainDisplacer.setPersistence(0.7D);
		mainDisplacer.setOctaveCount(1);

		final ScalePoint mainScalePoint = new ScalePoint();
		mainScalePoint.SetSourceModule(0, mainBase);
		mainScalePoint.setxScale(0.27D);
		mainScalePoint.setyScale(0.65D);
		mainScalePoint.setzScale(0.27D);

		final Displace mainDisplace = new Displace();
		mainDisplace.SetSourceModule(0, mainScalePoint);
		mainDisplace.SetXDisplaceModule(mainDisplacer);
		mainDisplace.SetZDisplaceModule(mainDisplacer);
		mainDisplace.SetYDisplaceModule(mainDisplacer);

		main.SetSourceModule(0, mainDisplace);
		main.setFrequency(0.025D);
		main.setPower(2.5D);

		limits.SetSourceModule(0, mainBase);
		limits.setScale(2.666666D);
		limits.setBias(7D);

		bedrockBase.setFrequency(0.1D);
		bedrockBase.setLacunarity(1D);
		bedrockBase.setNoiseQuality(NoiseQuality.STANDARD);
		bedrockBase.setPersistence(4D);
		bedrockBase.setOctaveCount(1);

		final ScalePoint bedrockScalePoint = new ScalePoint();
		bedrockScalePoint.SetSourceModule(0, bedrockBase);
		bedrockScalePoint.setxScale(100D);
		bedrockScalePoint.setzScale(100D);

		final ScaleBias bedrockScaleBias = new ScaleBias();
		bedrockScaleBias.SetSourceModule(0, bedrockScalePoint);
		bedrockScaleBias.setScale(6.666666D);
		bedrockScaleBias.setBias(2D);

		bedrock.SetSourceModule(0, bedrockScaleBias);
		bedrock.setLowerBound(0);
		bedrock.setUpperBound(3);
	}

	@Override
	public void generateColumn(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		final int seed = (int) blockData.getWorld().getSeed();
		mainBase.setSeed(seed);
		mainDisplacer.setSeed(seed);
		main.setSeed(seed);
		bedrockBase.setSeed(seed);
		final int y = chunkY * 16;
		for (int yy = y; yy < y + 16; yy++) {
			if (yy == 0 || yy == 255) {
				blockData.set(x, yy, z, VanillaMaterials.BEDROCK.getId());
				continue;
			}
			if (yy > 0 && yy < 4) {
				if (yy <= bedrock.GetValue(x, yy, z)) {
					blockData.set(x, yy, z, VanillaMaterials.BEDROCK.getId());
				} else {
					blockData.set(x, yy, z, VanillaMaterials.NETHERRACK.getId());
				}
				continue;
			}
			if (yy < 255 && yy > 251) {
				if (255 - yy <= bedrock.GetValue(x, yy, z)) {
					blockData.set(x, yy, z, VanillaMaterials.BEDROCK.getId());
				} else {
					blockData.set(x, yy, z, VanillaMaterials.NETHERRACK.getId());
				}
				continue;
			}
			if (yy > 3 && yy < 10) {
				if (yy < limits.GetValue(x, yy, z)) {
					blockData.set(x, yy, z, VanillaMaterials.NETHERRACK.getId());
				}
			}
			if (yy < 252 && yy > 245) {
				if (255 - yy < limits.GetValue(x, yy, z)) {
					blockData.set(x, yy, z, VanillaMaterials.NETHERRACK.getId());
				}
			}
			if (main.GetValue(x, yy, z) > 0) {
				blockData.set(x, yy, z, VanillaMaterials.NETHERRACK.getId());
			} else {
				if (yy < 32) {
					blockData.set(x, yy, z, VanillaMaterials.STATIONARY_LAVA.getId());
				}
			}
		}
	}

	@Override
	public String getName() {
		return "Netherrack";
	}
}
