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
package org.spout.vanilla.world.generator.object;

import java.util.Random;

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Vector2;

public abstract class RotatableObject extends RandomObject {
	protected Angle angle = Angle.ZERO;
	protected Vector2 center = new Vector2(0, 0);

	protected RotatableObject(Random random, Angle angle, Vector2 center) {
		super(random);
		this.angle = angle;
		this.center = center;
	}

	protected RotatableObject(Angle angle, Vector2 center) {
		this(null, angle, center);
	}

	protected RotatableObject(Random random, Vector2 center) {
		super(random);
		this.center = center;
	}

	protected RotatableObject(Vector2 center) {
		this.center = center;
	}

	protected RotatableObject(Random random, Angle angle) {
		super(random);
		this.angle = angle;
	}

	protected RotatableObject(Angle angle) {
		this.angle = angle;
	}

	protected RotatableObject(Random random) {
		super(random);
	}

	protected RotatableObject() {
	}

	protected Block getBlock(World world, int x, int y, int z) {
		final int cx = center.getFloorX();
		final int cz = center.getFloorY();
		switch (angle) {
			case ZERO:
				return world.getBlock(x, y, z);
			case NINETY:
				return world.getBlock(cx - z + cz, y, cz + x - cx);
			case ONE_HUNDRED_AND_EIGHTY:
				return world.getBlock(cx - x + cx, y, cz - z + cz);
			case TWO_HUNDRED_AND_SEVENTY:
				return world.getBlock(cx + z - cz, y, cz - x + cx);
			default:
				return world.getBlock(x, y, z);
		}
	}

	protected void setBlockMaterial(World world, int x, int y, int z, BlockMaterial material, short data, Source source) {
		final int cx = center.getFloorX();
		final int cz = center.getFloorY();
		switch (angle) {
			case ZERO:
				world.setBlockMaterial(x, y, z, material, data, source);
				return;
			case NINETY:
				world.setBlockMaterial(cx - z + cz, y, cz + x - cx, material, data, source);
				return;
			case ONE_HUNDRED_AND_EIGHTY:
				world.setBlockMaterial(cx - x + cx, y, cz - z + cz, material, data, source);
				return;
			case TWO_HUNDRED_AND_SEVENTY:
				world.setBlockMaterial(cx + z - cz, y, cz - x + cx, material, data, source);
		}
	}

	protected BlockMaterial getBlockMaterial(World world, int x, int y, int z) {
		final int cx = center.getFloorX();
		final int cz = center.getFloorY();
		switch (angle) {
			case ZERO:
				return world.getBlockMaterial(x, y, z);
			case NINETY:
				return world.getBlockMaterial(cx - z + cz, y, cz + x - cx);
			case ONE_HUNDRED_AND_EIGHTY:
				return world.getBlockMaterial(cx - x + cx, y, cz - z + cz);
			case TWO_HUNDRED_AND_SEVENTY:
				return world.getBlockMaterial(cx + z - cz, y, cz - x + cx);
			default:
				return world.getBlockMaterial(x, y, z);
		}
	}

	public Angle getAngle() {
		return angle;
	}

	public void setAngle(Angle angle) {
		this.angle = angle;
	}

	public Vector2 getAxis() {
		return center;
	}

	public void setAxis(Vector2 axis) {
		this.center = axis;
	}

	public static enum Angle {
		ZERO, NINETY, ONE_HUNDRED_AND_EIGHTY, TWO_HUNDRED_AND_SEVENTY;
	}
}
