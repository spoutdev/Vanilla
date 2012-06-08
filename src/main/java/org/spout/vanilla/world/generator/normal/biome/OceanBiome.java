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

import net.royawesome.jlibnoise.module.modifier.ScalePoint;

public class OceanBiome extends NormalBiome {
	private final static ScalePoint NOISE = new ScalePoint();

	static {
		NOISE.SetSourceModule(0, NormalBiome.MASTER);
		NOISE.setxScale(0.077D);
		NOISE.setyScale(0.030D);
		NOISE.setzScale(0.077D);
	}

	public OceanBiome(int biomeId) {
		super(biomeId, NOISE);
		minDensityTerrainHeight = 44;
		maxDensityTerrainHeight = 53;
		minDensityTerrainThickness = 2;
		maxDensityTerrainThickness = 5;
		upperHeightMapScale = 6.7f;
		bottomHeightMapScale = 7.5f;
		densityTerrainThicknessScale = 5f;
		densityTerrainHeightScale = 5f;
	}

	@Override
	public String getName() {
		return "Ocean";
	}
}
