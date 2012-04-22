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
package org.spout.vanilla.material.block.specific.redstone;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.material.block.attachable.WallAttachable;
import org.spout.vanilla.material.block.generic.VanillaBlockMaterial;

public class RedstoneTorch extends WallAttachable implements RedstoneSource, RedstoneTarget {
	public static final short REDSTONE_POWER = 15;
	private static final Vector3 possibleOutgoing[] = {new Vector3(1, 0, 0), new Vector3(-1, 0, 0), new Vector3(0, 0, 1), new Vector3(0, 0, -1), new Vector3(0, -1, 0), new Vector3(0, 2, 0),};
	private boolean powered;

	public RedstoneTorch(String name, int id, boolean powered) {
		super(name, id);
		this.powered = powered;
	}

	@Override
	public boolean providesAttachPoint(World world, int x, int y, int z, int tx, int ty, int tz) {
		return ty == y;
	}

	@Override
	public short getRedstonePower(World world, int x, int y, int z, int tx, int ty, int tz) {
		if (providesPowerTo(world, x, y, z, tx, ty, tz)) {
			return (short) (powered ? REDSTONE_POWER : 0);
		}
		return 0;
	}

	@Override
	public boolean providesPowerTo(World world, int x, int y, int z, int tx, int ty, int tz) {
		if (y == ty) {
			return true;
		}
		if (tx == x && tz == z) {
			return tx - x == -2 || tx - x == 1;
		}
		return false;
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(World world, int x, int y, int z) {
		super.onUpdate(world, x, y, z);
		if (!VanillaConfiguration.REDSTONE_PHYSICS.getBoolean()) {
			return;
		}

		BlockFace face = getFaceAttachedTo(world.getBlockData(x, y, z));
		Vector3 offset = face.getOffset();
		int tx = (int) (x + offset.getX()), ty = (int) (y + offset.getY()), tz = (int) (z + offset.getZ());
		BlockMaterial mat = world.getBlockMaterial(tx, ty, tz);
		if (mat instanceof VanillaBlockMaterial) {
			BlockMaterial newmat;
			VanillaBlockMaterial va = (VanillaBlockMaterial) mat;
			if (va.getIndirectRedstonePower(world, tx, ty, tz) > 0) {
				//Power off.
				newmat = VanillaMaterials.REDSTONE_TORCH_OFF;
			} else {
				//Seems to be no incoming power, turn on!
				newmat = VanillaMaterials.REDSTONE_TORCH_ON;
			}
			if (newmat != this) {
				short data = world.getBlockData(x, y, z);
				world.setBlockMaterial(x, y, z, newmat, data, false, world);

				//Update other redstone inputs
				for (Vector3 offset2 : possibleOutgoing) {//TODO changed the values below from offset to offset2
					tx = (int) (x + offset2.getX());
					ty = (int) (y + offset2.getY());
					tz = (int) (z + offset2.getZ());
					mat = world.getBlockMaterial(tx, ty, tz);
					if (mat instanceof RedstoneTarget) {
						world.updatePhysics(tx, ty, tz);
						//mat.updatePhysics(world, tx, ty, tz);
					}
				}
			}
		}
	}
}
