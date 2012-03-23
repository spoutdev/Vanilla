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
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;

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
		Block b = w.getBlock(cx, cy + height, cz);
		b.setMaterial(VanillaMaterials.LEAVES);
		b.clone().move(BlockFace.NORTH).setMaterial(VanillaMaterials.LEAVES);
		b.clone().move(BlockFace.EAST).setMaterial(VanillaMaterials.LEAVES);
		b.clone().move(BlockFace.SOUTH).setMaterial(VanillaMaterials.LEAVES);
		b.clone().move(BlockFace.WEST).setMaterial(VanillaMaterials.LEAVES);
	
		for (int k = 1; k <= oneWidth; k++) {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if(w.getBlockMaterial(cx + i, cy + height - k, cz + j).equals(VanillaMaterials.AIR) || w.getBlockMaterial(cx + i, cy + height - k, cz + j).equals(VanillaMaterials.VINES)) {
						b.clone().move(i, -k, j).setMaterial(VanillaMaterials.LEAVES);
					}
				}
			}
		}
		for (int k = oneWidth + 1; k <= oneWidth + twoWidth; k++) {
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if(w.getBlockMaterial(cx + i, cy + height - k, cz + j).equals(VanillaMaterials.AIR) || w.getBlockMaterial(cx + i, cy + height - k, cz + j).equals(VanillaMaterials.VINES)) {
						if(!(j == -2 && i == -2) && !(j == 2 && i == -2) && !(j == -2 && i == 2) && !(j == 2 && i == 2)) {
							b.clone().move(i, -k, j).setMaterial(VanillaMaterials.LEAVES);
						}
					}
				}
			}
		}
		for (int i = 0; i < height; i++) {
			b.move(BlockFace.BOTTOM).setMaterial(VanillaMaterials.LOG);
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
						Block vb = w.getBlock(cx + vx[i], cy + height - j - 2, cz + vz[i]);
						if(vb.getMaterial().equals(VanillaMaterials.AIR)) {
							vb.setMaterial(VanillaMaterials.VINES);
							if(vx[i] > 2) { //East
								vb.setData((short) 2);
							}
							else if(vx[i] < -2) { //West
								vb.setData((short) 8);
							}
							else if(vz[i] > 2) { //North
								vb.setData((short) 1);
							}
							else if(vz[i] < -2) { //Idk, south?
								vb.setData((short) 4);
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
		while (c.getWorld().getBlock(pozx, y, pozz).getMaterial() != VanillaMaterials.DIRT && c.getWorld().getBlock(pozx, y, pozz).getMaterial() != VanillaMaterials.GRASS) {
			y--;
			if (y == 0 || c.getWorld().getBlock(pozx, y, pozz).getMaterial() == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}