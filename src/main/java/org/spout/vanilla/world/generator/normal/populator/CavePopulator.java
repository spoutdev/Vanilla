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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.Random;

import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.VanillaMathHelper;

public class CavePopulator extends Populator {
	private static final byte OVERLAP = 8;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int chunkX = chunk.getX();
		final int chunkZ = chunk.getZ();
		for (int cx = chunkX - 8; cx <= chunkX + 8; cx++) {
			for (int cz = chunkZ - 8; cz <= chunkZ + 8; cz++) {
				final Random chunkRandom = WorldGeneratorUtils.getRandom(world, cx, 4, cz, 9001);
				generateCave(world.getChunk(cx, 4, cz), chunk, chunkRandom);
			}
		}
	}

	private void generateCave(Chunk chunk, Chunk originChunk, Random random) {
		if (random.nextInt(15) != 0) {
			return;
		}

		final int numberOfCaves = random.nextInt(40);
		final World world = chunk.getWorld();

		for (int caveCount = 0; caveCount < numberOfCaves; caveCount++) {
			final Point target = new Point(world, chunk.getBlockX(random),
					random.nextInt(random.nextInt(120) + 8), chunk.getBlockZ(random));
			int numberOfSmallCaves = 1;

			if (random.nextInt(4) == 0) {
				generateLargeCaveNode(originChunk, target, new Random(random.nextLong()));
				numberOfSmallCaves += random.nextInt(4);
			}

			for (int count = 0; count < numberOfSmallCaves; count++) {
				final float randomHorizontalAngle = random.nextFloat() * (float) Math.PI * 2;
				final float randomVerticalAngle = ((random.nextFloat() - 0.5f) * 2) / 8;
				float horizontalScale = random.nextFloat() * 2 + random.nextFloat();

				if (random.nextInt(10) == 0) {
					horizontalScale *= random.nextFloat() * random.nextFloat() * 3 + 1;
				}

				generateCaveBranch(originChunk, target, horizontalScale, 1, randomHorizontalAngle, randomVerticalAngle, 0, 0, new Random(random.nextLong()));
			}
		}
	}

	private void generateCaveBranch(Chunk chunk, Point target, float horizontalScale, float verticalScale,
									float horizontalAngle, float verticalAngle, int startingNode, int nodeAmount, Random random) {

		final Vector3 middle = new Vector3(chunk.getBlockX(8), 0, chunk.getBlockZ(8));
		float horizontalOffset = 0;
		float verticalOffset = 0;

		if (nodeAmount <= 0) {
			final int size = (OVERLAP - 1) * 16;
			nodeAmount = size - random.nextInt(size / 4);
		}

		final int intersectionNode = random.nextInt(nodeAmount / 2) + nodeAmount / 4;
		final boolean extraVerticalScale = random.nextInt(6) == 0;
		final boolean lastNode;

		if (startingNode == -1) {
			startingNode = nodeAmount / 2;
			lastNode = true;
		} else {
			lastNode = false;
		}

		for (; startingNode < nodeAmount; startingNode++) {
			final float horizontalSize = (float) (1.5 + VanillaMathHelper.sin(startingNode * (float) Math.PI / nodeAmount) * horizontalScale);
			final float verticalSize = horizontalSize * verticalScale;
			final float diskXZ = VanillaMathHelper.cos(verticalAngle);
			target = target.add(VanillaMathHelper.cos(horizontalAngle) * diskXZ, VanillaMathHelper.sin(verticalAngle), VanillaMathHelper.sin(horizontalAngle) * diskXZ);

			if (extraVerticalScale) {
				verticalAngle *= 0.92;
			} else {
				verticalAngle *= 0.7;
			}

			verticalAngle += verticalOffset * 0.1;
			horizontalAngle += horizontalOffset * 0.1;
			verticalOffset *= 0.9;
			horizontalOffset *= 0.75;
			verticalOffset += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2;
			horizontalOffset += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4;

			if (!lastNode) {

				if (startingNode == intersectionNode && horizontalScale > 1 && nodeAmount > 0) {
					generateCaveBranch(chunk, target, random.nextFloat() * 0.5f + 0.5f, 1, horizontalAngle - ((float) Math.PI / 2), verticalAngle / 3, startingNode, nodeAmount, new Random(random.nextLong()));
					generateCaveBranch(chunk, target, random.nextFloat() * 0.5f + 0.5f, 1, horizontalAngle + ((float) Math.PI / 2), verticalAngle / 3, startingNode, nodeAmount, new Random(random.nextLong()));
					return;
				}

				if (random.nextInt(4) == 0) {
					continue;
				}
			}

			final float xOffset = target.getX() - middle.getX();
			final float zOffset = target.getZ() - middle.getZ();
			final float nodesLeft = nodeAmount - startingNode;
			final float offsetHorizontalScale = horizontalScale + 18;

			if ((xOffset * xOffset + zOffset * zOffset) - nodesLeft * nodesLeft > offsetHorizontalScale * offsetHorizontalScale) {
				return;
			}

			if (target.getX() < middle.getX() - 16 - horizontalSize * 2
					|| target.getZ() < middle.getZ() - 16 - horizontalSize * 2
					|| target.getX() > middle.getX() + 16 + horizontalSize * 2
					|| target.getZ() > middle.getZ() + 16 + horizontalSize * 2) {
				continue;
			}

			final Point start = new Point(chunk.getWorld(), MathHelper.floor(target.getX() - horizontalSize) - chunk.getBlockX(-1),
					MathHelper.floor(target.getY() - verticalSize) - 1, MathHelper.floor(target.getZ() - horizontalSize) - chunk.getBlockZ(-1));
			final Point end = new Point(chunk.getWorld(), MathHelper.floor(target.getX() + horizontalSize) - chunk.getBlockX(1),
					MathHelper.floor(target.getY() + verticalSize) + 1, MathHelper.floor(target.getZ() + horizontalSize) - chunk.getBlockZ(1));
			final CaveNode node = new CaveNode(chunk, start, end, target, verticalSize, horizontalSize);

			if (node.canPlace()) {
				node.place();
			}

			if (lastNode) {
				break;
			}
		}
	}

	private void generateLargeCaveNode(Chunk chunk, Point target, Random random) {
		generateCaveBranch(chunk, target, random.nextFloat() * 6 + 1, 0.5f, 0, 0, -1, -1, random);
	}

	private static class CaveNode {
		private final World world;
		private final Chunk chunk;
		private final Point start;
		private final Point end;
		private final Point target;
		private final float verticalSize;
		private final float horizontalSize;

		private CaveNode(Chunk chunk, Point start, Point end, Point target,
						 float verticalSize, float horizontalSize) {
			this.world = chunk.getWorld();
			this.chunk = chunk;
			this.start = clamp(start);
			this.end = clamp(end);
			this.target = target;
			this.verticalSize = verticalSize;
			this.horizontalSize = horizontalSize;
		}

		private boolean canPlace() {
			for (int x = start.getBlockX(); x < end.getBlockX(); x++) {
				for (int z = start.getBlockZ(); z < end.getBlockZ(); z++) {
					for (int y = end.getBlockY() + 1; y >= start.getBlockY() - 1; y--) {
						if (world.getBlockMaterial(chunk.getBlockX(x), y, chunk.getBlockZ(z)).
								equals(VanillaMaterials.WATER, VanillaMaterials.STATIONARY_WATER)) {
							return false;
						}
						if (y != start.getBlockY() - 1 && x != start.getBlockX() && x != end.getBlockX() - 1
								&& z != start.getBlockZ() && z != end.getBlockZ() - 1) {
							y = start.getBlockY();
						}
					}
				}
			}
			return true;
		}

		private void place() {
			for (int x = start.getBlockX(); x < end.getBlockX(); x++) {
				final float xOffset = (chunk.getBlockX(x) + 0.5f - target.getX()) / horizontalSize;
				for (int z = start.getBlockZ(); z < end.getBlockZ(); z++) {
					final float zOffset = (chunk.getBlockZ(z) + 0.5f - target.getZ()) / horizontalSize;
					if (xOffset * xOffset + zOffset * zOffset >= 1) {
						continue;
					}
					for (int y = end.getBlockY() - 1; y >= start.getBlockY(); y--) {
						final float yOffset = (y + 0.5f - target.getY()) / verticalSize;
						if (yOffset > -0.7 && xOffset * xOffset + yOffset * yOffset + zOffset * zOffset < 1) {
							final Block block = world.getBlock(chunk.getBlockX(x), y, chunk.getBlockZ(z), world);
							if (block.isMaterial(VanillaMaterials.STONE, VanillaMaterials.DIRT, VanillaMaterials.GRASS)) {
								if (y < 10) {
									block.setMaterial(VanillaMaterials.STATIONARY_LAVA);
								} else {
									if (block.isMaterial(VanillaMaterials.GRASS)
											&& block.translate(BlockFace.BOTTOM).isMaterial(VanillaMaterials.DIRT)) {
										block.translate(BlockFace.BOTTOM).setMaterial(VanillaMaterials.GRASS);
									}
									block.setMaterial(VanillaMaterials.AIR);
								}
							}
						}
					}
				}
			}
		}

		private static Point clamp(Point point) {
			return new Point(point.getWorld(),
					MathHelper.clamp(point.getBlockX(), 0, 16),
					MathHelper.clamp(point.getBlockY(), 1, 120),
					MathHelper.clamp(point.getBlockZ(), 0, 16));
		}
	}
}
