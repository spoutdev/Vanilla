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
package org.spout.vanilla.generator.normal.decorator;

import java.util.Random;
import java.util.logging.Level;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.generator.biome.BiomeType;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.generator.VanillaBiomes;
import org.spout.vanilla.world.Biome;

public class TreeDecorator implements BiomeDecorator {
	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() < 4) {
			return;
		}
		int genInChunk = random.nextInt(100);
		if (genInChunk <= 30) {
			return;
		}
		int px = random.nextInt(16);
		int pz = random.nextInt(16);
		int py = getHighestWorkableBlock(chunk, px, pz);
		px = chunk.getX() * 16 + px;
		pz = chunk.getZ() * 16 + pz;
		if (py == -1) {
			return;
		}
		generateSmallTree(chunk, random, px, py, pz);
	}

	private void generateSmallTree(Chunk c, Random ra, int cx, int cy, int cz) {
		BiomeGenerator bg = null;
		if(c.getWorld().getGenerator() instanceof BiomeGenerator) {
			bg = (BiomeGenerator) c.getWorld().getGenerator();
		}
		
		int height = 5 + ra.nextInt(2), oneWidth, twoWidth;
		if (height == 0) {
			return;
		}
		oneWidth = 1;
		twoWidth = 2;
		World w = c.getWorld();
		w.setBlockMaterial(cx, cy + height, cz, VanillaMaterials.LEAVES, c.getWorld());
		w.setBlockMaterial(cx - 1, cy + height, cz, VanillaMaterials.LEAVES, c.getWorld());
		w.setBlockMaterial(cx + 1, cy + height, cz, VanillaMaterials.LEAVES, c.getWorld());
		w.setBlockMaterial(cx, cy + height, cz - 1, VanillaMaterials.LEAVES, c.getWorld());
		w.setBlockMaterial(cx, cy + height, cz + 1, VanillaMaterials.LEAVES, c.getWorld());

		for (int k = 1; k <= oneWidth; k++) {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if(w.getBlockMaterial(cx + i, cy + height - k, cz + j).equals(VanillaMaterials.AIR) || w.getBlockMaterial(cx + i, cy + height - k, cz + j).equals(VanillaMaterials.VINES)) {
						w.setBlockMaterial(cx + i, cy + height - k, cz + j, VanillaMaterials.LEAVES, c.getWorld());
					}
				}
			}
		}
		for (int k = oneWidth + 1; k <= oneWidth + twoWidth; k++) {
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if(w.getBlockMaterial(cx + i, cy + height - k, cz + j).equals(VanillaMaterials.AIR) || w.getBlockMaterial(cx + i, cy + height - k, cz + j).equals(VanillaMaterials.VINES)) {
						if(!(j == -2 && i == -2) && !(j == 2 && i == -2) && !(j == -2 && i == 2) && !(j == 2 && i == 2)) {
							w.setBlockMaterial(cx + i, cy + height - k, cz + j, VanillaMaterials.LEAVES, c.getWorld());
						}
					}
				}
			}
		}
		for (int i = 0; i < height; i++) {
			w.setBlockMaterial(cx, cy + i, cz, VanillaMaterials.LOG, w);
		}
		if(bg != null) {
			BiomeType bio = bg.getBiome(cx, cz, c.getWorld().getSeed());
			if(bio.equals(VanillaBiomes.SWAMP)) {
				//Placements of the vines relative to the trunk
				int[] vx = {2,  2, -2, -2, 1,  1, 0,  0, -1, -1, 3, -3, 3, -3,  3, -3};
				int[] vz = {2, -2, -2,  2, 3, -3, 3, -3,  3, -3, 1,  1, 0,  0, -1, -1};
				for(int i = 0; i < vx.length; i++) {
					int h = ra.nextInt(height-2);
					for(int j = h; j >= 0; j--) {
						if(w.getBlockMaterial(cx + vx[i], cy + height - j - 2, cz + vz[i]).equals(VanillaMaterials.AIR)) {
							w.setBlockMaterial(cx + vx[i], cy + height - j - 2, cz + vz[i], VanillaMaterials.VINES, c.getWorld());
							if(vx[i] > 2) { //East
								w.setBlockData(cx + vx[i], cy + height - j - 2, cz + vz[i], (short) 2, c.getWorld());
							}
							else if(vx[i] < -2) { //West
								w.setBlockData(cx + vx[i], cy + height - j - 2, cz + vz[i], (short) 8, c.getWorld());
							}
							else if(vz[i] > 2) { //North
								w.setBlockData(cx + vx[i], cy + height - j - 2, cz + vz[i], (short) 1, c.getWorld());
							}
							else if(vz[i] < -2) { //Idk, south?
								w.setBlockData(cx + vx[i], cy + height - j - 2, cz + vz[i], (short) 4, c.getWorld());
							}
						}
					}
				}
			}
		}
	}

	private int getHighestWorkableBlock(Chunk c, int px, int pz) {
		int y = c.getY() * 16 + 15;
		int pozx = c.getX() * 16 + px;
		int pozz = c.getZ() * 16 + pz;
		while (c.getWorld().getBlock(pozx, y, pozz).getBlockMaterial() != VanillaMaterials.DIRT && c.getWorld().getBlock(pozx, y, pozz).getBlockMaterial() != VanillaMaterials.GRASS) {
			y--;
			if (y == 0 || c.getWorld().getBlock(pozx, y, pozz).getBlockMaterial() == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}