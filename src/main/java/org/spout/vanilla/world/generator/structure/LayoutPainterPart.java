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
package org.spout.vanilla.world.generator.structure;

import gnu.trove.map.hash.TCharObjectHashMap;

import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.vanilla.material.VanillaMaterials;

/**
 * Draws a plane with the defined block material layout
 */
public class LayoutPainterPart extends ComponentPart {
	private Vector3 position = Vector3.ZERO;
	private Quaternion rotation = Quaternion.IDENTITY;
	private Vector3 rotationPoint = Vector3.ZERO;
	private BlockMaterialLayout layout = new BlockMaterialLayout("");

	public LayoutPainterPart(StructureComponent parent) {
		super(parent);
	}

	public void setLayout(BlockMaterialLayout layout) {
		this.layout = layout;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public void setRotationPoint(Vector3 rotationPoint) {
		this.rotationPoint = rotationPoint;
	}

	@Override
	public void fill(boolean ignoreAir) {
		for (int xx = 0; xx < layout.getRowLenght(); xx++) {
			for (int zz = 0; zz < layout.getColumnLenght(xx); zz++) {
				if (!ignoreAir || !getBlockMaterial(xx, zz).isMaterial(VanillaMaterials.AIR)) {
					setBlockMaterial(xx, zz, layout.getBlockMaterial(xx, zz));
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
					setBlockMaterial(xx, zz, layout.getBlockMaterial(xx, zz));
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
					setBlockMaterial(xx, zz, layout.getBlockMaterial(xx, zz));
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
		return MathHelper.round(MathHelper.transform(new Vector3(x, 0, z).
				subtract(rotationPoint), rotation).
				add(rotationPoint).add(position));
	}
}
