/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.plugin.world.generator.structure;

import org.spout.api.material.BlockMaterial;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.math.VectorMath;

import org.spout.vanilla.plugin.material.VanillaMaterials;

/**
 * Draws a plane with the defined block material layout
 */
public class ComponentLayoutPainterPart extends ComponentPart {
	private IntVector3 position = new IntVector3(0, 0, 0);
	private Quaternion rotation = Quaternion.IDENTITY;
	private IntVector3 rotationPoint = new IntVector3(0, 0, 0);
	private BlockMaterialLayout layout = new BlockMaterialLayout("");

	public ComponentLayoutPainterPart(StructureComponent parent) {
		super(parent);
	}

	public void setLayout(BlockMaterialLayout layout) {
		this.layout = layout;
	}

	public void setPosition(int x, int y, int z) {
		setPosition(new IntVector3(x, y, z));
	}

	public void setPosition(IntVector3 position) {
		this.position = position;
	}

	public void offsetPosition(int xOff, int yOff, int zOff) {
		offsetPosition(new IntVector3(xOff, yOff, zOff));
	}

	public void offsetPosition(IntVector3 offset) {
		position.add(offset);
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public void setRotationPoint(int x, int y, int z) {
		setRotationPoint(new IntVector3(x, y, z));
	}

	public void setRotationPoint(IntVector3 rotationPoint) {
		this.rotationPoint = rotationPoint;
	}

	public void offsetRotationPoint(int xOff, int yOff, int zOff) {
		offsetRotationPoint(new IntVector3(xOff, yOff, zOff));
	}

	public void offsetRotationPoint(IntVector3 offset) {
		rotationPoint.add(offset);
	}

	@Override
	public void fill(boolean ignoreAir) {
		for (int xx = 0; xx < layout.getRowLenght(); xx++) {
			for (int zz = 0; zz < layout.getColumnLenght(xx); zz++) {
				if (!ignoreAir || !getBlockMaterial(xx, zz).isMaterial(VanillaMaterials.AIR)) {
					final BlockMaterial material = layout.getBlockMaterial(xx, zz);
					if (material != null) {
						setBlockMaterial(xx, zz, material);
					}
				}
			}
		}
	}

	@Override
	public void randomFill(float odd, boolean ignoreAir) {
		for (int xx = 0; xx < layout.getRowLenght(); xx++) {
			for (int zz = 0; zz < layout.getColumnLenght(xx); zz++) {
				if (parent.getRandom().nextFloat() > odd) {
					continue;
				}
				if (!ignoreAir || !getBlockMaterial(xx, zz).isMaterial(VanillaMaterials.AIR)) {
					final BlockMaterial material = layout.getBlockMaterial(xx, zz);
					if (material != null) {
						setBlockMaterial(xx, zz, material);
					}
				}
			}
		}
	}

	@Override
	public void sphericalFill(boolean ignoreAir) {
		final float xScale = layout.getRowLenght() + 1;
		final float zScale = layout.getColumnLenght(0) + 1;
		final float xOffset = xScale / 2;
		final float zOffset = zScale / 2;
		final int endX = layout.getRowLenght();
		final int endZ = layout.getColumnLenght(0);
		for (int xx = 0; xx <= endX; xx++) {
			final float dx = (xx - xOffset) / (xScale * 0.5f);
			for (int zz = 0; zz <= endZ; zz++) {
				final float dz = (zz - zOffset) / (zScale * 0.5f);
				if (dx * dx + dz * dz <= 1.05) {
					if (ignoreAir && !getBlockMaterial(xx, zz).isMaterial(VanillaMaterials.AIR)) {
						continue;
					}
					final BlockMaterial material = layout.getBlockMaterial(xx, zz);
					if (material != null) {
						setBlockMaterial(xx, zz, material);
					}
				}
			}
		}
	}

	private BlockMaterial getBlockMaterial(int xx, int zz) {
		final Vector3 transformed = transform(xx, zz);
		return parent.getBlockMaterial(transformed.getFloorX(), transformed.getFloorY(), transformed.getFloorZ());
	}

	private void setBlockMaterial(int xx, int zz, BlockMaterial material) {
		final Vector3 transformed = transform(xx, zz);
		parent.setBlockMaterial(transformed.getFloorX(), transformed.getFloorY(), transformed.getFloorZ(), material);
	}

	private Vector3 transform(int x, int z) {
		final Vector3 rotPoint = new Vector3(rotationPoint.getX(), rotationPoint.getY(), rotationPoint.getZ());
		return VectorMath.transform(new Vector3(x, 0, z).subtract(rotPoint), rotation).
				add(rotPoint).add(position.getX(), position.getY(), position.getZ()).round();
	}
}
