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
package org.spout.vanilla.material.block.specific;

import org.spout.vanilla.material.block.Solid;
import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.configuration.VanillaConfiguration;

public class Cactus extends Solid {
	public Cactus(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(World world, int x, int y, int z) {
		if (!VanillaConfiguration.CACTUS_PHYSICS.getBoolean()) {
			return;
		}

		int amount = 0;
		int off = 1;
		while (world.getBlockMaterial(x, y + off, z).equals(VanillaMaterials.CACTUS)) {
			off++;
			amount++;
		}

		boolean destroy = false;
		BlockMaterial below = world.getBlockMaterial(x, y - 1, z);
		if (!below.equals(VanillaMaterials.SAND) && !below.equals(VanillaMaterials.CACTUS)) {
			destroy = true;
		}

		if (!destroy) {
			BlockFace faces[] = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
			for (BlockFace face : faces) {
				int tx = (int) (x + face.getOffset().getX());
				int tz = (int) (z + face.getOffset().getZ());
				BlockMaterial side = world.getBlockMaterial(tx, y, tz);
				if (!side.equals(VanillaMaterials.AIR)) {
					destroy = true;
					break;
				}
			}
		}

		Point point = new Point(world, x, y, z);
		if (destroy) {
			world.setBlockMaterial(x, y, z, VanillaMaterials.AIR, (short) 0, true, world);
			world.createAndSpawnEntity(point, new Item(new ItemStack(VanillaMaterials.CACTUS, amount), point.normalize()));
		}
	}

	@Override
	public boolean onPlacement(World world, int x, int y, int z, short data, BlockFace against, Source source) {
		for(int i=-1;i<=1;i++)
			for(int j=-1;j<=1;j++) {
				if(i==0&&j==0) continue;
				if(world.getBlockMaterial(x+i, y, z+j)!=VanillaMaterials.AIR)
					return false;
			}
		return true;
	}
}
