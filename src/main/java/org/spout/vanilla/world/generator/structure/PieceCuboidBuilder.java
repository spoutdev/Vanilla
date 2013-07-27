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
package org.spout.vanilla.world.generator.structure;

import org.spout.api.math.IntVector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;

public class PieceCuboidBuilder extends PieceBuilder {
	protected final IntVector3 min = new IntVector3(0, 0, 0);
	protected final IntVector3 max = new IntVector3(0, 0, 0);
	private BlockMaterialPicker picker = new SimpleBlockMaterialPicker();
	private boolean ignoreAir = false;

	public PieceCuboidBuilder(StructurePiece parent) {
		super(parent);
	}

	public PieceCuboidBuilder setMax(IntVector3 max) {
		this.max.set(max);
		return this;
	}

	public PieceCuboidBuilder setMin(IntVector3 min) {
		this.min.set(min);
		return this;
	}

	public PieceCuboidBuilder setMax(int x, int y, int z) {
		max.set(x, y, z);
		return this;
	}

	public PieceCuboidBuilder setMin(int x, int y, int z) {
		min.set(x, y, z);
		return this;
	}

	public PieceCuboidBuilder setMinMax(IntVector3 min, IntVector3 max) {
		setMin(min);
		setMax(max);
		return this;
	}

	public PieceCuboidBuilder setMinMax(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		setMin(minX, minY, minZ);
		setMax(maxX, maxY, maxZ);
		return this;
	}

	public PieceCuboidBuilder offsetMin(IntVector3 offset) {
		min.add(offset);
		return this;
	}

	public PieceCuboidBuilder offsetMax(IntVector3 offset) {
		max.add(offset);
		return this;
	}

	public PieceCuboidBuilder offsetMinMax(IntVector3 minOffset, IntVector3 maxOffset) {
		offsetMin(minOffset);
		offsetMax(maxOffset);
		return this;
	}

	public PieceCuboidBuilder offsetMin(int x, int y, int z) {
		offsetMin(new IntVector3(x, y, z));
		return this;
	}

	public PieceCuboidBuilder offsetMax(int x, int y, int z) {
		offsetMax(new IntVector3(x, y, z));
		return this;
	}

	public PieceCuboidBuilder offsetMinMax(int minXOff, int minYOff, int minZOff, int maxXOff, int maxYOff, int maxZOff) {
		offsetMin(minXOff, minYOff, minZOff);
		offsetMax(maxXOff, maxYOff, maxZOff);
		return this;
	}

	public PieceCuboidBuilder setPicker(BlockMaterialPicker picker) {
		this.picker = picker;
		return this;
	}

	public PieceCuboidBuilder setIgnoreAir(boolean ignoreAir) {
		this.ignoreAir = ignoreAir;
		return this;
	}

	public PieceCuboidBuilder toggleIgnoreAir() {
		ignoreAir ^= true;
		return this;
	}

	protected boolean isOuter(int xx, int yy, int zz) {
		return xx == min.getX() || yy == min.getY() || zz == min.getZ()
				|| xx == max.getX() || yy == max.getY() || zz == max.getZ();
	}

	@Override
	public void fill() {
		final int endX = max.getX();
		final int endY = max.getY();
		final int endZ = max.getZ();
		for (int xx = min.getX(); xx <= endX; xx++) {
			for (int yy = min.getY(); yy <= endY; yy++) {
				for (int zz = min.getZ(); zz <= endZ; zz++) {
					if (!ignoreAir || !parent.getBlockMaterial(xx, yy, zz).isMaterial(VanillaMaterials.AIR)) {
						parent.setBlockMaterial(xx, yy, zz, picker.get(isOuter(xx, yy, zz)));
					}
				}
			}
		}
	}

	public void randomFill(float odd) {
		final int endX = max.getX();
		final int endY = max.getY();
		final int endZ = max.getZ();
		for (int xx = min.getX(); xx <= endX; xx++) {
			for (int yy = min.getY(); yy <= endY; yy++) {
				for (int zz = min.getZ(); zz <= endZ; zz++) {
					if (parent.getRandom().nextFloat() > odd) {
						continue;
					}
					if (!ignoreAir || !parent.getBlockMaterial(xx, yy, zz).isMaterial(VanillaMaterials.AIR)) {
						parent.setBlockMaterial(xx, yy, zz, picker.get(isOuter(xx, yy, zz)));
					}
				}
			}
		}
	}

	public void sphericalFill() {
		final float xScale = max.getX() - min.getX() + 1;
		final float yScale = max.getY() - min.getY() + 1;
		final float zScale = max.getZ() - min.getZ() + 1;
		final float xOffset = min.getX() + xScale / 2;
		final float zOffset = min.getZ() + zScale / 2;
		final int endX = max.getX();
		final int endY = max.getY();
		final int endZ = max.getZ();
		for (int xx = min.getX(); xx <= endX; xx++) {
			final float dx = (xx - xOffset) / (xScale * 0.5f);
			for (int yy = min.getY(); yy <= endY; yy++) {
				final float dy = (yy - min.getY()) / yScale;
				for (int zz = min.getZ(); zz <= endZ; zz++) {
					final float dz = (zz - zOffset) / (zScale * 0.5f);
					if (dx * dx + dy * dy + dz * dz <= 1.05) {
						if (ignoreAir && !parent.getBlockMaterial(xx, yy, zz).isMaterial(VanillaMaterials.AIR)) {
							continue;
						}
						parent.setBlockMaterial(xx, yy, zz, picker.get(false));
					}
				}
			}
		}
	}

	public boolean intersectsLiquids() {
		final int startX = min.getX();
		final int startY = min.getY();
		final int startZ = min.getZ();
		final int endX = max.getX();
		final int endY = max.getY();
		final int endZ = max.getZ();
		for (int yy = startY; yy <= endY; yy++) {
			if (yy == startY || yy == endY) {
				for (int xx = startX; xx <= endX; xx++) {
					for (int zz = startZ; zz <= endZ; zz++) {
						if (parent.getBlockMaterial(xx, yy, zz) instanceof Liquid) {
							return true;
						}
					}
				}
			} else {
				for (int xx = startX; xx <= endX; xx++) {
					if (parent.getBlockMaterial(xx, yy, startZ) instanceof Liquid
							|| parent.getBlockMaterial(xx, yy, endZ) instanceof Liquid) {
						return true;
					}
				}
				for (int zz = startZ + 1; zz < endZ; zz++) {
					if (parent.getBlockMaterial(startX, yy, zz) instanceof Liquid
							|| parent.getBlockMaterial(endX, yy, zz) instanceof Liquid) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
