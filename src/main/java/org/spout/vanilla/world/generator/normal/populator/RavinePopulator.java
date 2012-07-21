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

public class RavinePopulator extends Populator {
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
				generateRavine(world.getChunk(cx, 4, cz), chunk, chunkRandom);
			}
		}
	}

	private void generateRavine(Chunk chunk, Chunk originChunk, Random random) {
		if (random.nextInt(50) != 0) {
			return;
		}

		final Point target = new Point(chunk.getWorld(), chunk.getBlockX(random),
				random.nextInt(random.nextInt(40) + 8) + 20, chunk.getBlockZ(random));
		final float randomHorizontalAngle = (float) (random.nextFloat() * Math.PI * 2);
		final float randomVerticalAngle = ((random.nextFloat() - 0.5f) * 2) / 8;
		final float horizontalScale = (random.nextFloat() * 2 + random.nextFloat()) * 2;
		generateRavineNodes(originChunk, target, horizontalScale, 3, randomHorizontalAngle, randomVerticalAngle, 0, 0, random);
	}

	private void generateRavineNodes(Chunk chunk, Point target, float horizontalScale, float verticalScale,
									 float horizontalAngle, float verticalAngle, int startingNode, int nodeAmount, Random random) {

		final Vector3 middle = new Vector3(chunk.getBlockX(8), 0, chunk.getBlockZ(8));
		float horizontalOffset = 0;
		float verticalOffset = 0;

		if (nodeAmount <= 0) {
			final int size = (OVERLAP - 1) * 16;
			nodeAmount = size - random.nextInt(size / 4);
		}

		final boolean lastNode;

		if (startingNode == -1) {
			startingNode = nodeAmount / 2;
			lastNode = true;
		} else {
			lastNode = false;
		}

		final float[] horizontalScales = new float[128];

		for (short y = 0; y < 128; y++) {
			final float xzScale = y == 0 || random.nextInt(3) == 0 ? 1 + random.nextFloat() * random.nextFloat() : 1;
			horizontalScales[y] = xzScale * xzScale;
		}

		for (; startingNode < nodeAmount; startingNode++) {
			float horizontalSize = (float) (1.5 + VanillaMathHelper.sin(startingNode * (float) Math.PI / nodeAmount) * horizontalScale);
			float verticalSize = horizontalSize * verticalScale;
			final float diskXZ = (float) VanillaMathHelper.cos(verticalAngle);
			target = target.add(VanillaMathHelper.cos(horizontalAngle) * diskXZ, VanillaMathHelper.sin(verticalAngle), VanillaMathHelper.sin(horizontalAngle) * diskXZ);

			horizontalSize *= random.nextFloat() * 0.25 + 0.75;
			verticalSize *= random.nextFloat() * 0.25 + 0.75;

			verticalAngle *= 0.7;
			verticalAngle += verticalOffset * 0.05;
			horizontalAngle += horizontalOffset * 0.05;
			verticalOffset *= 0.8;
			horizontalOffset *= 0.5;
			verticalOffset += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2;
			horizontalOffset += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4;

			if (!lastNode && random.nextInt(4) == 0) {
				continue;
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
			final RavineNode node = new RavineNode(chunk, start, end, target, verticalSize, horizontalSize, horizontalScales);

			if (node.canPlace()) {
				node.place();
			}

			if (lastNode) {
				break;
			}
		}
	}

	private static class RavineNode {
		private final World world;
		private final Chunk chunk;
		private final Point start;
		private final Point end;
		private final Point target;
		private final float verticalSize;
		private final float horizontalSize;
		private final float[] horizontalScales;

		private RavineNode(Chunk chunk, Point start, Point end, Point target,
						   float verticalSize, float horizontalSize, float[] horizontalScales) {
			this.world = chunk.getWorld();
			this.chunk = chunk;
			this.start = clamp(start);
			this.end = clamp(end);
			this.target = target;
			this.verticalSize = verticalSize;
			this.horizontalSize = horizontalSize;
			this.horizontalScales = horizontalScales;
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
						if ((xOffset * xOffset + zOffset * zOffset) * horizontalScales[y] + (yOffset * yOffset) / 6 < 1) {
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
