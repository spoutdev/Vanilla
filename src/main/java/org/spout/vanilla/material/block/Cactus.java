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
package org.spout.vanilla.material.block;

import org.spout.api.Source;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.generic.Solid;

public class Cactus extends Solid {
	public Cactus() {
		super("Cactus", 81);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(Block block) {
		if (!VanillaConfiguration.CACTUS_PHYSICS.getBoolean()) {
			return;
		}

		int amount = 0;
		Block tmpblock = block;
		while ((tmpblock = tmpblock.translate(BlockFace.TOP)).getMaterial().equals(VanillaMaterials.CACTUS)) {
			amount++;
		}

		boolean destroy = false;
		BlockMaterial below = block.translate(BlockFace.BOTTOM).getMaterial();
		if (!below.equals(VanillaMaterials.SAND) && !below.equals(VanillaMaterials.CACTUS)) {
			destroy = true;
		}

		if (!destroy) {
			BlockFace faces[] = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
			for (BlockFace face : faces) {
				BlockMaterial side = block.translate(face).getMaterial();
				if (!side.equals(VanillaMaterials.AIR)) {
					destroy = true;
					break;
				}
			}
		}

		Point point = block.getPosition();
		if (destroy) {
			block.setMaterial(VanillaMaterials.AIR).update(true);
			point.getWorld().createAndSpawnEntity(point, new Item(new ItemStack(VanillaMaterials.CACTUS, amount), point.normalize()));
		}
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Source source) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				if (block.translate(i, 0, j).getMaterial() != VanillaMaterials.AIR) {
					return false;
				}
			}
		}
		return true;
	}
}
