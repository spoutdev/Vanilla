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
package org.spout.vanilla.material.item;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.material.Block;
import org.spout.vanilla.material.attachable.WallAttachable;
import org.spout.vanilla.material.block.RedstoneSource;
import org.spout.vanilla.material.block.RedstoneTarget;

public class RedstoneTorch extends WallAttachable implements RedstoneSource, RedstoneTarget {
	public static final short REDSTONE_POWER = 15;
	private static final Vector3 possibleOutgoing[] = {Vector3.UNIT_X, Vector3.UNIT_X.multiply(-1), Vector3.UNIT_Z, Vector3.UNIT_Z.multiply(-1), Vector3.UNIT_Y.multiply(-1), new Vector3(0, 2, 0),};
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
		if (mat instanceof Block) {
			short updateId = getId();
			Block va = (Block) mat;
			if (va.getIndirectRedstonePower(world, tx, ty, tz) > 0) {
				//Power off.
				updateId = VanillaMaterials.REDSTONE_TORCH_OFF.getId();
			} else {
				//Seems to be no incoming power, turn on!
				updateId = VanillaMaterials.REDSTONE_TORCH_ON.getId();
			}
			if (updateId != getId()) {
				short data = world.getBlockData(x, y, z);
				world.setBlockIdAndData(x, y, z, updateId, data, false, world);

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
