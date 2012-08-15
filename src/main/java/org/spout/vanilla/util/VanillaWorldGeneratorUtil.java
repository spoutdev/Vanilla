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
package org.spout.vanilla.util;

import net.royawesome.jlibnoise.module.Module;

public class VanillaWorldGeneratorUtil {
	public static double[] fastNoise(Module noiseGenerator, int xSize, int samplingRate, int x, int y, int z) {
		if (noiseGenerator == null) {
			throw new IllegalArgumentException("noiseGenerator cannot be null");
		}
		if (samplingRate == 0) {
			throw new IllegalArgumentException("samplingRate cannot be 0");
		}
		if (xSize % samplingRate != 0) {
			throw new IllegalArgumentException("(xSize % samplingRate) must return 0");
		}
		final double[] noiseArray = new double[xSize + 1];
		for (int xx = 0; xx <= xSize; xx += samplingRate) {
			noiseArray[xx] = noiseGenerator.GetValue(xx + x, y, z);
		}
		for (int xx = 0; xx < xSize; xx++) {
			if (xx % samplingRate != 0) {
				int nx = (xx / samplingRate) * samplingRate;
				noiseArray[xx] = VanillaMathHelper.lerp(xx, nx, nx + samplingRate,
						noiseArray[nx], noiseArray[nx + samplingRate]);
			}
		}
		return noiseArray;
	}

	public static double[][] fastNoise(Module noiseGenerator, int xSize, int zSize, int samplingRate, int x, int y, int z) {
		if (noiseGenerator == null) {
			throw new IllegalArgumentException("noiseGenerator cannot be null");
		}
		if (samplingRate == 0) {
			throw new IllegalArgumentException("samplingRate cannot be 0");
		}
		if (xSize % samplingRate != 0) {
			throw new IllegalArgumentException("(xSize % samplingRate) must return 0");
		}
		if (zSize % samplingRate != 0) {
			throw new IllegalArgumentException("(zSize % samplingRate) must return 0");
		}
		final double[][] noiseArray = new double[xSize + 1][zSize + 1];
		for (int xx = 0; xx <= xSize; xx += samplingRate) {
			for (int zz = 0; zz <= zSize; zz += samplingRate) {
				noiseArray[xx][zz] = noiseGenerator.GetValue(xx + x, y, z + zz);
			}
		}
		for (int xx = 0; xx < xSize; xx++) {
			for (int zz = 0; zz < zSize; zz++) {
				if (xx % samplingRate != 0 || zz % samplingRate != 0) {
					int nx = (xx / samplingRate) * samplingRate;
					int nz = (zz / samplingRate) * samplingRate;
					noiseArray[xx][zz] = VanillaMathHelper.biLerp(xx, zz, noiseArray[nx][nz],
							noiseArray[nx][nz + samplingRate], noiseArray[nx + samplingRate][nz],
							noiseArray[nx + samplingRate][nz + samplingRate], nx, nx + samplingRate,
							nz, nz + samplingRate);
				}
			}
		}
		return noiseArray;
	}

	public static double[][][] fastNoise(Module noiseGenerator, int xSize, int ySize, int zSize,
			int samplingRate, int x, int y, int z) {
		if (noiseGenerator == null) {
			throw new IllegalArgumentException("noiseGenerator cannot be null");
		}
		if (samplingRate == 0) {
			throw new IllegalArgumentException("samplingRate cannot be 0");
		}
		if (xSize % samplingRate != 0) {
			throw new IllegalArgumentException("(xSize % samplingRate) must return 0");
		}
		if (zSize % samplingRate != 0) {
			throw new IllegalArgumentException("(zSize % samplingRate) must return 0");
		}
		if (ySize % samplingRate != 0) {
			throw new IllegalArgumentException("(ySize % samplingRate) must return 0");
		}
		final double[][][] noiseArray = new double[xSize + 1][ySize + 1][zSize + 1];
		for (int xx = 0; xx <= xSize; xx += samplingRate) {
			for (int yy = 0; yy <= ySize; yy += samplingRate) {
				for (int zz = 0; zz <= zSize; zz += samplingRate) {
					noiseArray[xx][yy][zz] = noiseGenerator.GetValue(xx + x, y + yy, z + zz);
				}
			}
		}
		for (int xx = 0; xx < xSize; xx++) {
			for (int yy = 0; yy < ySize; yy++) {
				for (int zz = 0; zz < zSize; zz++) {
					if (xx % samplingRate != 0 || yy % samplingRate != 0 || zz % samplingRate != 0) {
						int nx = (xx / samplingRate) * samplingRate;
						int ny = (yy / samplingRate) * samplingRate;
						int nz = (zz / samplingRate) * samplingRate;
						noiseArray[xx][yy][zz] = VanillaMathHelper.triLerp(xx, yy, zz,
								noiseArray[nx][ny][nz], noiseArray[nx][ny + samplingRate][nz],
								noiseArray[nx][ny][nz + samplingRate], noiseArray[nx][ny + samplingRate][nz + samplingRate],
								noiseArray[nx + samplingRate][ny][nz], noiseArray[nx + samplingRate][ny + samplingRate][nz],
								noiseArray[nx + samplingRate][ny][nz + samplingRate], noiseArray[nx + samplingRate][ny + samplingRate][nz + samplingRate],
								nx, nx + samplingRate, ny, ny + samplingRate, nz, nz + samplingRate);
					}
				}
			}
		}
		return noiseArray;
	}
}
