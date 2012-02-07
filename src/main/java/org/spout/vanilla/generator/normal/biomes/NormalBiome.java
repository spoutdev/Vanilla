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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.generator.normal.biomes;

import java.util.ArrayList;

import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.biome.BiomeDecorator;
import org.spout.vanilla.biome.BiomeType;
import org.spout.vanilla.generator.normal.CavePopulator;
import org.spout.vanilla.generator.normal.OrePopulator;
import org.spout.vanilla.generator.normal.TreePopulator;

public class NormalBiome extends BiomeType {
	int seed = 42;
	Perlin layerCount = new Perlin(), heightMap = new Perlin();
	ArrayList<Perlin> layers = new ArrayList<Perlin>();
	
	public NormalBiome(){
		layerCount.setSeed(seed + 10);
		layerCount.setOctaveCount(5);

		heightMap.setSeed(seed);
		heightMap.setOctaveCount(5);
		
		
	}
	
	@Override
	public void registerDecorators() {
		register(new OrePopulator());
        register(new CavePopulator());
        register(new TreePopulator());
	}
	
	public Perlin getLayer(int layer) {
		if(layer >= 0) {
			if(layer < layers.size()) {
				return layers.get(layer);
			} else {
				Perlin p = new Perlin();
				p.setSeed(seed + layer);
				p.setOctaveCount(5);
				layers.add(p);
				return p;
			}
		} else {
			return null;
		}
	}


	@Override
	public void generateTerrain(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ) {
		int x = chunkX * 16;
		int y = chunkY * 16;
		int z = chunkZ * 16;

		if (y > 127) {
			blockData.flood((short)0);
			//return;
		}
		if (chunkY < 0) {
			blockData.flood(VanillaMaterials.BEDROCK.getId());
			//return;
		}

		for (int dx = x; dx < (x+16); dx++) {
			for (int dz = z; dz < (z+16); dz++) {

				int height = (int) ((heightMap.GetValue(dx / 16.0 + 0.005, 0.05, dz / 16.0 + 0.005) + 1.0) * 4.0 + 60.0);

				boolean wateredStack = height < 64;

				for(int dy = y; dy < y + 16; dy++) {
					short id;

					id = getBlockId(height, dy);

					blockData.set(dx, dy, dz, id);
				}

				if(wateredStack) {
					for(int dy = y + 15; dy >= y; dy--) {
						if(dy < 64 && blockData.get(dx, dy, dz) == VanillaMaterials.AIR.getId()) {
							blockData.set(dx, dy, dz, VanillaMaterials.WATER.getId());
						} else {
							break;
						}
					}
				}
			}
		}
	}

	public static double getPerlinValueXZ(Perlin perlin, int x, int z) {
		return perlin.GetValue(x / 16.0 + 0.05, 0.05, z / 16.0 + 0.05);
	}

	private short getBlockId(int top, int dy) {
		short id;
		if(dy > top) {
			id = VanillaMaterials.AIR.getId();
		} else if(dy == top && dy >= 63) {
			id = VanillaMaterials.GRASS.getId();
		} else if(dy + 4 >=top) {
			id = VanillaMaterials.DIRT.getId();
		} else if(dy != 0){
			id = VanillaMaterials.STONE.getId();
		} else {
			id = VanillaMaterials.BEDROCK.getId();
		}
		return id;
	}
}
