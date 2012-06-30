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
package org.spout.vanilla.world.generator.nether.object;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Vector2;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.world.generator.object.RotatableObject;

public class NetherPortalObject extends RotatableObject {
	private boolean floating = false;

	public NetherPortalObject() {
		this(null);
	}

	public NetherPortalObject(Random random) {
		super(random);
		findRandomAngle();
	}

	@Override
	public void randomize() {
		findRandomAngle();
	}

	public final void findRandomAngle() {
		switch (random.nextInt(4)) {
			case 0:
				angle = Angle.ZERO;
				return;
			case 1:
				angle = Angle.NINETY;
				return;
			case 2:
				angle = Angle.ONE_HUNDRED_AND_EIGHTY;
				return;
			case 3:
				angle = Angle.TWO_HUNDRED_AND_SEVENTY;
		}
	}

	@Override
	public boolean canPlaceObject(World world, int x, int y, int z) {
		center = new Vector2(x, z);
		for (byte yy = -1; yy < 4; yy++) {
			for (byte xx = -2; xx < 2; xx++) {
				for (byte zz = -1; zz < 2; zz++) {
					final BlockMaterial material = getBlockMaterial(world, x + xx, y + yy, z + zz);
					if (!floating && yy <= -1 && !(material instanceof Solid)
							|| yy >= 0 && (material instanceof Solid || material instanceof Liquid)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void placeObject(World world, int x, int y, int z) {
		center = new Vector2(x, z);
		for (byte xx = -2; xx < 2; xx++) {
			for (byte yy = -1; yy < 4; yy++) {
				if (xx == -2 || xx == 1 || yy == -1 || yy == 3) {
					setBlockMaterial(world, x + xx, y + yy, z, VanillaMaterials.OBSIDIAN, (short) 0, world);
				} else {
					setBlockMaterial(world, x + xx, y + yy, z, VanillaMaterials.PORTAL, (short) 0, world);
				}
			}
		}
		if (floating) {
			for (byte xx = -1; xx < 1; xx++) {
				for (byte zz = -1; zz < 2; zz++) {
					setBlockMaterial(world, x + xx, y - 1, z + zz, VanillaMaterials.OBSIDIAN, (short) 0, world);
				}
			}
		}
	}

	public void setFloating(boolean floating) {
		this.floating = floating;
	}

	public static void placePortal(World world, int x, int z, Random random) {
		final NetherPortalObject portal = new NetherPortalObject(random);
		for (byte xx = -16; xx <= 16; xx++) {
			for (byte zz = -16; zz <= 16; zz++) {
				for (byte yy = 127; yy >= 0; yy--) {
					final byte y = getHighestSolidY(world, x + xx, yy, z + zz);
					if (y != -1 && portal.canPlaceObject(world, x + xx, y, z + zz)) {
						portal.placeObject(world, x + xx, y, z + zz);
						return;
					}
				}
			}
		}
		portal.setFloating(true);
		for (byte xx = -16; xx <= 16; xx++) {
			for (byte zz = -16; zz <= 16; zz++) {
				for (byte yy = 70; yy < 118; yy++) {
					if (portal.canPlaceObject(world, x + xx, yy, z + zz)) {
						portal.placeObject(world, x + xx, yy, z + zz);
						return;
					}
				}
			}
		}
	}

	private static byte getHighestSolidY(World world, int x, byte startY, int z) {
		while (!(world.getBlockMaterial(x, startY, z) instanceof Solid)) {
			startY--;
			if (startY == 0) {
				return -1;
			}
		}
		startY++;
		return startY;
	}
}
