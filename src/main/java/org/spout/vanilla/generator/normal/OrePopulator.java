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
package org.spout.vanilla.generator.normal;

import java.util.Random;

import org.spout.api.generator.Populator;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.VanillaBlocks;

public class OrePopulator implements Populator {
	private	Random ra = new Random();

	@Override
	public void populate(Chunk c) {
		int chance;
		chance = ra.nextInt(10000);
		if (chance <= 500) {
			generateCoal(c, ra);
		}
		chance = ra.nextInt(10000);
		if (chance <= 500 && c.getY() <= 4) {
			generateIron(c, ra);
		}
		chance = ra.nextInt(10000);
		if (chance <= 400 && c.getY() <= 2) {
			generateLapis(c, ra);
		}
		chance = ra.nextInt(10000);
		if (chance <= 390 && c.getY() <= 2) {
			generateGold(c, ra);
		}
		chance = ra.nextInt(10000);
		if (chance <= 350 && c.getY() <= 1) {
			generateDiamond(c, ra);
		}
		chance = ra.nextInt(10000);
		if (chance <= 370 && c.getY() <= 1) {
			generateRedstone(c, ra);
		}
	}

	private void generateCoal(Chunk c, Random ra) {
		generateOre(c, ra, VanillaBlocks.COAL_ORE, 30);
	}

	private void generateIron(Chunk c, Random ra) {
		generateOre(c, ra, VanillaBlocks.IRON_ORE, 15);
	}

	private void generateLapis(Chunk c, Random ra) {
		generateOre(c, ra, VanillaBlocks.LAPIS_ORE, 8);
	}

	private void generateGold(Chunk c, Random ra) {
		generateOre(c, ra, VanillaBlocks.GOLD_ORE, 9);
	}

	private void generateDiamond(Chunk c, Random ra) {
		generateOre(c, ra, VanillaBlocks.DIAMOND_ORE, 9);
	}

	private void generateRedstone(Chunk c, Random ra) {
		generateOre(c, ra, VanillaBlocks.REDSTONE_ORE, 10);
	}

	private void generateOre(Chunk c, Random ra, BlockMaterial material, int maxNumber) {
		int number = ra.nextInt(maxNumber);
		int pozx = c.getX() * 16 + ra.nextInt(16);
		int pozy = c.getY() * 16 + ra.nextInt(16);
		int pozz = c.getZ() * 16 + ra.nextInt(16);
		for (int i = 1; i <= number;) {
			if (c.getWorld().getBlock(pozx, pozy, pozz).getBlockMaterial() != VanillaBlocks.AIR) {
				c.getWorld().setBlockMaterial(pozx, pozy, pozz, material, c.getWorld());
			}
			i++;
			int newDir = ra.nextInt(6);
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
