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
package org.spout.vanilla.material.attachable;

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.material.generic.GenericBlock;

public abstract class AbstractAttachable extends GenericBlock implements Attachable {
	protected AbstractAttachable(String name, int id) {
		super(name, id);
	}

	protected AbstractAttachable(String name, int id, int data) {
		super(name, id, data);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(World world, int x, int y, int z) {
		if (getBlockAttachedTo(world, x, y, z).getBlockMaterial().equals(VanillaMaterials.AIR)) {
			world.setBlockMaterial(x, y, z, VanillaMaterials.AIR, world);
		}
	}

	@Override
	public Block getBlockAttachedTo(World world, int x, int y, int z) {
		BlockFace base = getFaceAttachedTo(world.getBlockData(x, y, z));
		Vector3 offset = base.getOffset();
		return world.getBlock((int) (x + offset.getX()), (int) (y + offset.getY()), (int) (z + offset.getZ()));
	}
	
	@Override
	public boolean onPlacement(World world, int x, int y, int z, short data, BlockFace against, Source source) {
		return super.onPlacement(world, x, y, z, this.getDataForFace(against.getOpposite()), against, source);
	}
	
}
