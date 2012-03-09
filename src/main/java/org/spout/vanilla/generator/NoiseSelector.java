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
package org.spout.vanilla.generator;

import org.spout.api.generator.biome.BiomeSelector;

import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Voronoi;

public class NoiseSelector extends BiomeSelector {
	private Turbulence noise = new Turbulence();
	private Voronoi base = new Voronoi();

	public NoiseSelector(double vorFreq, double displacement, int roughness, double turFreq, double power) {
		base.setFrequency(vorFreq);
		base.setDisplacement(displacement);
		noise.SetSourceModule(0, base);
		noise.setRoughness(roughness);
		noise.setFrequency(turFreq);
		noise.setPower(power);
	}

	@Override
	public int pickBiome(int x, int y, int z, long seed) {
		noise.setSeed((int) seed);
		//Pick a biome at 256 height for both x and z
		return (int) (noise.GetValue(x / 256.0 + 0.05, y + 0.05, z / 256.0 + 0.05) * 64);
	}
}
