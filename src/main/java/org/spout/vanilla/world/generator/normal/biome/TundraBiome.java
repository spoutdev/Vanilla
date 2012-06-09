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

import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.configuration.BiomeConfiguration;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.NormalGenerator;

public class TundraBiome extends NormalBiome {

	private final static ScalePoint NOISE = new ScalePoint();

	static {
		NOISE.SetSourceModule(0, NormalBiome.MASTER);
		NOISE.setxScale(BiomeConfiguration.TUNDRA_X_SCALE.getDouble());
		NOISE.setyScale(BiomeConfiguration.TUNDRA_Y_SCALE.getDouble());
		NOISE.setzScale(BiomeConfiguration.TUNDRA_Z_SCALE.getDouble());
	}

	public TundraBiome(int id) {
		super(id, NOISE/*
				 * , new PondDecorator()
				 */);

		this.minDensityTerrainHeight = BiomeConfiguration.TUNDRA_MIN_DENSITY_TERRAIN_HEIGHT.getByte();
		this.maxDensityTerrainHeight = BiomeConfiguration.TUNDRA_MAX_DENSITY_TERRAIN_HEIGHT.getByte();

		this.minDensityTerrainThickness = BiomeConfiguration.TUNDRA_MIN_DENSITY_TERRAIN_THICKNESS.getByte();
		this.maxDensityTerrainThickness = BiomeConfiguration.TUNDRA_MAX_DENSITY_TERRAIN_THICKNESS.getByte();

		this.upperHeightMapScale = BiomeConfiguration.TUNDRA_UPPER_HEIGHT_MAP_SCALE.getFloat();
		this.bottomHeightMapScale = BiomeConfiguration.TUNDRA_BOTTOM_HEIGHT_MAP_SCALE.getFloat();

		this.densityTerrainThicknessScale = BiomeConfiguration.TUNDRA_DENSITY_TERRAIN_THICKNESS_SCALE.getFloat();
		this.densityTerrainHeightScale = BiomeConfiguration.TUNDRA_DENSITY_TERRAIN_HEIGHT_SCALE.getFloat();
	}

	@Override
	public String getName() {
		return "Tundra";
	}

	@Override
	protected void replaceBlocks(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		super.replaceBlocks(blockData, x, chunkY, z);
		final byte size = (byte) blockData.getSize().getY();
		final int startY = chunkY * 16;
		final int endY = startY + size;
		if (blockData.get(x, startY, z) == VanillaMaterials.AIR.getId()) {
			if (getSample(blockData.getWorld(), x, startY - 1, startY, z).get(x, startY - 1, z)
					!= VanillaMaterials.AIR.getId()) {
				blockData.set(x, startY, z, VanillaMaterials.SNOW.getId());
			}
		}
		boolean hasSurface = false;
		for (int y = startY; y < endY; y++) {
			short id = blockData.get(x, y, z);
			if (id == VanillaMaterials.GRASS.getId()) {
				hasSurface = true;
			} else if (id == VanillaMaterials.AIR.getId() && hasSurface) {
				blockData.set(x, y, z, VanillaMaterials.SNOW.getId());
				hasSurface = false;
			}
		}
		if (chunkY * 16 <= NormalGenerator.SEA_LEVEL && (chunkY + 1) * 16 > NormalGenerator.SEA_LEVEL
				&& blockData.get(x, NormalGenerator.SEA_LEVEL, z) == VanillaMaterials.STATIONARY_WATER.getId()) {
			blockData.set(x, NormalGenerator.SEA_LEVEL, z, VanillaMaterials.ICE.getId());
		}
	}
}
