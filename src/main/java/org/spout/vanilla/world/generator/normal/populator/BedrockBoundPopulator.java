/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.world.generator.normal.populator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;

import org.spout.math.vector.Vector3f;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.MathHelper;

public class BedrockBoundPopulator implements GeneratorPopulator {
	private final List<BedrockBound> bounds = new ArrayList<BedrockBound>();

	@Override
	public void populate(CuboidBlockMaterialBuffer blockData, int x, int y, int z, BiomeManager biomes, long seed) {
		final Vector3f size = blockData.getSize();
		final int sizeX = size.getFloorX();
		final int sizeY = size.getFloorY();
		final int sizeZ = size.getFloorZ();
		final int bufferStart = y;
		final int bufferEnd = bufferStart + sizeY;
		for (BedrockBound bound : bounds) {
			final int bedrockStart = bound.getStart();
			final int bedrockEnd = bound.getEnd();
			if (bedrockStart >= bufferStart && bedrockStart < bufferEnd
					|| bedrockEnd >= bufferStart && bedrockEnd < bufferEnd) {
				final int start = Math.max(bedrockStart, bufferStart);
				final int end = Math.min(bedrockEnd, bufferEnd);
				for (int xx = 0; xx < sizeX; xx++) {
					for (int zz = 0; zz < sizeZ; zz++) {
						if (bound.isFlipped()) {
							final int offset = Math.max(bedrockEnd - bufferEnd, 0);
							final int depth = Math.max(end - bound.getDepth(x + xx, z + zz, (int) seed) + offset, start);
							for (int yy = depth; yy < end; yy++) {
								blockData.set(x + xx, yy, z + zz, VanillaMaterials.BEDROCK);
							}
						} else {
							final int offset = Math.max(bufferStart - bedrockStart, 0);
							final int depth = Math.min(start + bound.getDepth(x + xx, z + zz, (int) seed) - offset, end);
							for (int yy = start; yy < depth; yy++) {
								blockData.set(x + xx, yy, z + zz, VanillaMaterials.BEDROCK);
							}
						}
					}
				}
			}
		}
	}

	public void addBound(int position, int min, int max) {
		addBound(new BedrockBound(position, min, max));
	}

	public void addBound(BedrockBound bound) {
		bounds.add(bound);
	}

	public void addBounds(BedrockBound... bounds) {
		addBounds(Arrays.asList(bounds));
	}

	public void addBounds(Collection<BedrockBound> bounds) {
		this.bounds.addAll(bounds);
	}

	public BedrockBound getBound(int index) {
		return bounds.get(index);
	}

	public void removeBound(int index) {
		bounds.remove(index);
	}

	public void clearBounds() {
		bounds.clear();
	}

	public void sortBounds() {
		Collections.sort(bounds);
	}

	public static class BedrockBound implements Comparable<BedrockBound> {
		private int position;
		private int min;
		private int max;
		private boolean flipped = false;

		public BedrockBound(int position, int min, int max) {
			this.position = position;
			this.min = min;
			this.max = max;
		}

		public int getDepth(int x, int z, int seed) {
			return (int) (MathHelper.hashToFloat(x, z, seed) * (max - min + 1)) + min;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			this.min = min;
		}

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}

		public int getStart() {
			return getPosition();
		}

		public int getEnd() {
			return getStart() + max;
		}

		public boolean isFlipped() {
			return flipped;
		}

		public void setFlipped(boolean flipped) {
			this.flipped = flipped;
		}

		@Override
		public int compareTo(BedrockBound other) {
			return position - other.position;
		}
	}
}
