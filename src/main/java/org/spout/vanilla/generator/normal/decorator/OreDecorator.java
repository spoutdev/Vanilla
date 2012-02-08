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
package org.spout.vanilla.generator.normal.decorator;

import java.util.Random;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.biome.BiomeDecorator;

public class OreDecorator implements BiomeDecorator {

	@Override
	public void populate(Chunk chunk, Random random) {
		int chance;
		chance = random.nextInt(1000);
		if (chance <= 500) {
			generateCoal(chunk, random);
		}
		chance = random.nextInt(1000);
		if (chance <= 500 && chunk.getY() <= 4) {
			generateIron(chunk, random);
		}
		chance = random.nextInt(1000);
		if (chance <= 400 && chunk.getY() <= 2) {
			generateLapis(chunk, random);
		}
		chance = random.nextInt(1000);
		if (chance <= 390 && chunk.getY() <= 2) {
			generateGold(chunk, random);
		}
		chance = random.nextInt(1000);
		if (chance <= 350 && chunk.getY() <= 1) {
			generateDiamond(chunk, random);
		}
		chance = random.nextInt(1000);
		if (chance <= 370 && chunk.getY() <= 1) {
			generateRedstone(chunk, random);
		}
	}

	private void generateCoal(Chunk c, Random ra) {
		generateOre(c, ra, VanillaMaterials.COAL_ORE, 30);
	}

	private void generateIron(Chunk c, Random ra) {
		generateOre(c, ra, VanillaMaterials.IRON_ORE, 15);
	}

	private void generateLapis(Chunk c, Random ra) {
		generateOre(c, ra, VanillaMaterials.LAPIS_ORE, 8);
	}

	private void generateGold(Chunk c, Random ra) {
		generateOre(c, ra, VanillaMaterials.GOLD_ORE, 9);
	}

	private void generateDiamond(Chunk c, Random ra) {
		generateOre(c, ra, VanillaMaterials.DIAMOND_ORE, 9);
	}

	private void generateRedstone(Chunk c, Random ra) {
		generateOre(c, ra, VanillaMaterials.REDSTONE_ORE, 10);
	}

	private void generateOre(Chunk chunk, Random random, BlockMaterial material, int maxNumber) {
		int number = random.nextInt(maxNumber);
		int pozx = chunk.getX() * 16 + random.nextInt(16);
		int pozy = chunk.getY() * 16 + random.nextInt(16);
		int pozz = chunk.getZ() * 16 + random.nextInt(16);
		for (int i = 1; i <= number;) {
			if (chunk.getWorld().getBlock(pozx, pozy, pozz).getBlockMaterial().equals(VanillaMaterials.STONE)) {
				chunk.getWorld().setBlockMaterial(pozx, pozy, pozz, material, chunk.getWorld());
			}
			i++;
			int newDir = random.nextInt(6);
			switch (newDir) {
				case 0:
					pozx++;
					break;
				case 1:
					pozx--;
					break;
				case 2:
					pozy++;
					break;
				case 3:
					pozy--;
					break;
				case 4:
					pozz++;
					break;
				case 5:
					pozz--;
					break;
			}
		}
	}
}
