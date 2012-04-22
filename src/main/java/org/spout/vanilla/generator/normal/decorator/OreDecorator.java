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

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;

public class OreDecorator implements BiomeDecorator {
	@Override
	public void populate(Chunk source, Random random) {
		World world = source.getWorld();
		int[] iterations = new int[]{10, 20, 20, 2, 8, 1, 1, 1};
		int[] amount = new int[]{32, 16, 8, 8, 7, 7, 6};
		BlockMaterial[] type = new BlockMaterial[]{VanillaMaterials.GRAVEL, VanillaMaterials.COAL_ORE, VanillaMaterials.IRON_ORE, VanillaMaterials.GOLD_ORE, VanillaMaterials.REDSTONE_ORE, VanillaMaterials.DIAMOND_ORE, VanillaMaterials.LAPIS_ORE};

		int[] maxHeight = new int[]{128, 128, 128, 128, 128, 64, 32, 16, 16, 32};

		for (int i = 0; i < type.length; i++) {
			for (int j = 0; j < iterations[i]; j++) {
				generateOre(world, random, source.getX() * 16 + random.nextInt(16), random.nextInt(maxHeight[i]), source.getZ() * 16 + random.nextInt(16), amount[i], type[i]);
			}
		}
	}

	private void generateOre(World world, Random random, int originX, int originY, int originZ, int amount, BlockMaterial type) {
		double angle = random.nextDouble() * Math.PI;
		double x1 = ((originX + 8) + Math.sin(angle) * amount / 8);
		double x2 = ((originX + 8) - Math.sin(angle) * amount / 8);
		double z1 = ((originZ + 8) + Math.cos(angle) * amount / 8);
		double z2 = ((originZ + 8) - Math.cos(angle) * amount / 8);
		double y1 = (originY + random.nextInt(3) + 2);
		double y2 = (originY + random.nextInt(3) + 2);

		for (int i = 0; i <= amount; i++) {
			double seedX = x1 + (x2 - x1) * i / amount;
			double seedY = y1 + (y2 - y1) * i / amount;
			double seedZ = z1 + (z2 - z1) * i / amount;
			double size = ((Math.sin(i * Math.PI / amount) + 1) * random.nextDouble() * amount / 16 + 1) / 2;

			int startX = (int) (seedX - size);
			int startY = (int) (seedY - size);
			int startZ = (int) (seedZ - size);
			int endX = (int) (seedX + size);
			int endY = (int) (seedY + size);
			int endZ = (int) (seedZ + size);

			for (int x = startX; x <= endX; x++) {
				double sizeX = (x + 0.5 - seedX) / size;
				sizeX *= sizeX;

				if (sizeX < 1) {
					for (int y = startY; y <= endY; y++) {
						double sizeY = (y + 0.5 - seedY) / size;
						sizeY *= sizeY;

						if (sizeX + sizeY < 1) {
							for (int z = startZ; z <= endZ; z++) {
								double sizeZ = (z + 0.5 - seedZ) / size;
								sizeZ *= sizeZ;
								if (sizeX + sizeY + sizeZ < 1 && world.getBlockMaterial(x, y, z) == VanillaMaterials.STONE) {
									world.getBlock(x, y, z).setMaterial(type).update(true);
								}
							}
						}
					}
				}
			}
		}
	}
}
