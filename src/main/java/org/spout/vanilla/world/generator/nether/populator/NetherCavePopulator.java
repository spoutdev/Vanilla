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
package org.spout.vanilla.world.generator.nether.populator;

import java.util.Random;

import org.spout.api.math.MathHelper;
import org.spout.api.math.SinusHelper;
import org.spout.api.math.Vector3;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.populator.OverlapingPopulator;

public class NetherCavePopulator extends OverlapingPopulator {
	@Override
	protected void generate(CuboidShortBuffer blockData, Vector3 chunk, Vector3 originChunk, Random random) {
		if (random.nextInt(5) != 0) {
			return;
		}

		final int numberOfCaves = random.nextInt(10);

		for (int caveCount = 0; caveCount < numberOfCaves; caveCount++) {
			final Vector3 target = new Vector3(chunk.getX() + random.nextInt(16),
					random.nextInt(random.nextInt(120) + 8), chunk.getZ() + random.nextInt(16));
			int numberOfSmallCaves = 1;

			if (random.nextInt(4) == 0) {
				generateLargeCaveNode(blockData, originChunk, target, new Random(random.nextLong()));
				numberOfSmallCaves += random.nextInt(4);
			}

			for (int count = 0; count < numberOfSmallCaves; count++) {
				final double randomHorizontalAngle = random.nextDouble() * Math.PI * 2;
				final double randomVerticalAngle = ((random.nextDouble() - 0.5f) * 2) / 8;
				double horizontalScale = random.nextDouble() * 2 + random.nextDouble();
				generateCaveBranch(blockData, originChunk, target, horizontalScale, 1, randomHorizontalAngle, randomVerticalAngle, 0, 0, new Random(random.nextLong()));
			}
		}
	}

	private void generateCaveBranch(CuboidShortBuffer blockData, Vector3 chunk, Vector3 target, double horizontalScale, double verticalScale,
			double horizontalAngle, double verticalAngle, int startingNode, int nodeAmount, Random random) {

		final Vector3 middle = new Vector3(chunk.getX() + 8, 0, chunk.getZ() + 8);
		double horizontalOffset = 0;
		double verticalOffset = 0;
		random = new Random(random.nextLong());

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
			final double horizontalSize = 1.5 + SinusHelper.sin((float) (startingNode * Math.PI / nodeAmount)) * horizontalScale;
			final double verticalSize = horizontalSize * verticalScale;
			target = target.add(SinusHelper.get3DAxis((float) horizontalAngle, (float) verticalAngle));

			if (extraVerticalScale) {
				verticalAngle *= 0.92;
			} else {
				verticalAngle *= 0.7;
			}

			verticalAngle += verticalOffset * 0.1;
			horizontalAngle += horizontalOffset * 0.1;
			verticalOffset *= 0.9;
			horizontalOffset *= 0.75;
			verticalOffset += (random.nextDouble() - random.nextDouble()) * random.nextDouble() * 2;
			horizontalOffset += (random.nextDouble() - random.nextDouble()) * random.nextDouble() * 4;

			if (!lastNode) {

				if (startingNode == intersectionNode && horizontalScale > 1) {
					generateCaveBranch(blockData, chunk, target, random.nextDouble() * 0.5f + 0.5f, 1, horizontalAngle - ((float) Math.PI / 2), verticalAngle / 3, startingNode, nodeAmount, new Random(random.nextLong()));
					generateCaveBranch(blockData, chunk, target, random.nextDouble() * 0.5f + 0.5f, 1, horizontalAngle + ((float) Math.PI / 2), verticalAngle / 3, startingNode, nodeAmount, new Random(random.nextLong()));
					return;
				}

				if (random.nextInt(4) == 0) {
					continue;
				}
			}

			final double xOffset = target.getX() - middle.getX();
			final double zOffset = target.getZ() - middle.getZ();
			final double nodesLeft = nodeAmount - startingNode;
			final double offsetHorizontalScale = horizontalScale + 18;

			if ((xOffset * xOffset + zOffset * zOffset) - nodesLeft * nodesLeft > offsetHorizontalScale * offsetHorizontalScale) {
				return;
			}

			if (target.getX() < middle.getX() - 16 - horizontalSize * 2
					|| target.getZ() < middle.getZ() - 16 - horizontalSize * 2
					|| target.getX() > middle.getX() + 16 + horizontalSize * 2
					|| target.getZ() > middle.getZ() + 16 + horizontalSize * 2) {
				continue;
			}

			final Vector3 start = new Vector3(MathHelper.floor(target.getX() - horizontalSize) - chunk.getFloorX() - 1,
					MathHelper.floor(target.getY() - verticalSize) - 1, MathHelper.floor(target.getZ() - horizontalSize) - chunk.getFloorZ() - 1);
			final Vector3 end = new Vector3(MathHelper.floor(target.getX() + horizontalSize) - chunk.getFloorX() + 1,
					MathHelper.floor(target.getY() + verticalSize) + 1, MathHelper.floor(target.getZ() + horizontalSize) - chunk.getFloorZ() + 1);
			final NetherCaveNode node = new NetherCaveNode(blockData, chunk, start, end, target, verticalSize, horizontalSize);

			if (node.canPlace()) {
				node.place();
			}

			if (lastNode) {
				break;
			}
		}
	}

	private void generateLargeCaveNode(CuboidShortBuffer blockData, Vector3 chunk, Vector3 target, Random random) {
		generateCaveBranch(blockData, chunk, target, random.nextFloat() * 6 + 1, 0.5f, 0, 0, -1, -1, random);
	}

	private static class NetherCaveNode {
		private final CuboidShortBuffer blockData;
		private final Vector3 chunk;
		private final Vector3 start;
		private final Vector3 end;
		private final Vector3 target;
		private final double verticalSize;
		private final double horizontalSize;

		private NetherCaveNode(CuboidShortBuffer blockData, Vector3 chunk, Vector3 start, Vector3 end, Vector3 target,
				double verticalSize, double horizontalSize) {
			this.blockData = blockData;
			this.chunk = chunk;
			this.start = clamp(start);
			this.end = clamp(end);
			this.target = target;
			this.verticalSize = verticalSize;
			this.horizontalSize = horizontalSize;
		}

		private boolean canPlace() {
			for (int x = start.getFloorX(); x < end.getFloorX(); x++) {
				for (int z = start.getFloorZ(); z < end.getFloorZ(); z++) {
					for (int y = end.getFloorY() + 1; y >= start.getFloorY() - 1; y--) {
						if (blockData.get(chunk.getFloorX() + x, y, chunk.getFloorZ() + z)
								== VanillaMaterials.LAVA.getId()) {
							return false;
						}
						if (y != start.getFloorY() - 1 && x != start.getFloorX() && x != end.getFloorX() - 1
								&& z != start.getFloorZ() && z != end.getFloorZ() - 1) {
							y = start.getFloorY();
						}
					}
				}
			}
			return true;
		}

		private void place() {
			for (int x = start.getFloorX(); x < end.getFloorX(); x++) {
				final double xOffset = (chunk.getX() + x + 0.5f - target.getX()) / horizontalSize;
				for (int z = start.getFloorZ(); z < end.getFloorZ(); z++) {
					final double zOffset = (chunk.getZ() + z + 0.5f - target.getZ()) / horizontalSize;
					if (xOffset * xOffset + zOffset * zOffset >= 1) {
						continue;
					}
					for (int y = end.getFloorY() - 1; y >= start.getFloorY(); y--) {
						final double yOffset = (y + 0.5f - target.getY()) / verticalSize;
						if (yOffset > -0.7 && xOffset * xOffset + yOffset * yOffset + zOffset * zOffset < 1) {
							final int xx = chunk.getFloorX() + x;
							final int zz = chunk.getFloorZ() + z;
							final short id = blockData.get(xx, y, zz);
							if (id == VanillaMaterials.NETHERRACK.getId()) {
								blockData.set(xx, y, zz, VanillaMaterials.AIR.getId());
							}
						}
					}
				}
			}
		}

		private static Vector3 clamp(Vector3 point) {
			return new Vector3(
					MathHelper.clamp(point.getFloorX(), 0, 16),
					MathHelper.clamp(point.getFloorY(), 1, 120),
					MathHelper.clamp(point.getFloorZ(), 0, 16));
		}
	}
}
