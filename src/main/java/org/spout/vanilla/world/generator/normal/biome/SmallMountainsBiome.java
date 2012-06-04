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

public class SmallMountainsBiome extends VanillaNormalBiome {

	private final static ScalePoint NOISE = new ScalePoint();

	static {
		NOISE.SetSourceModule(0, VanillaNormalBiome.MASTER);
		NOISE.setxScale(0.085D);
		NOISE.setyScale(0.035D);
		NOISE.setzScale(0.085D);
	}

	public SmallMountainsBiome(int biomeId) {
		super(biomeId, NOISE);
		minDensityTerrainHeight = 64;
		maxDensityTerrainHeight = 74;
		minDensityTerrainThickness = 2;
		maxDensityTerrainThickness = 6;
		upperHeightMapScale = 5f;
		bottomHeightMapScale = 6f;
		densityTerrainThicknessScale = 5.5f;
		densityTerrainHeightScale = 5.5f;
	}

	@Override
	public String getName() {
		return "Small Mountains";
	}
}
