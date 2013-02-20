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
package org.spout.vanilla.material.block;

import org.spout.api.entity.Entity;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;

import org.spout.vanilla.component.entity.substance.object.FallingBlock;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;

public class SolidMoving extends Solid {
	public SolidMoving(String name, int id, int data, VanillaBlockMaterial parent) {
		this(name, id, data, parent, null);
	}

	public SolidMoving(String name, int id, int data, VanillaBlockMaterial parent, String model) {
		super(name, id, data, parent, model);
	}

	public SolidMoving(String name, int id, String model) {
		super(name, id, model);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (!block.translate(BlockFace.BOTTOM).getMaterial().isPlacementObstacle()) {
			// turn this block into a mobile block
			Entity e = block.getWorld().createAndSpawnEntity(block.getPosition(), FallingBlock.class, LoadOption.NO_LOAD);
			e.add(FallingBlock.class).setMaterial(this);
			block.setMaterial(VanillaMaterials.AIR);
		}
	}

	/**
	 * Simulates the result of a blocks falling inside a buffer. If the blocks
	 * reach the bottom of the buffer without hitting any obstacles they may
	 * either be removed or stopped at the bottom. If the block at (x, y, z) is
	 * not a SolidMoving, nothing will happen, else it and the blocks above will
	 * be subject to the simulation. If the blocks fall on a placement obstacle
	 * (such as a torch) they will be removed.
	 * @param buffer The buffer in which to simulate the fall.
	 * @param x The x coordinate of the block.
	 * @param y The y coordinate of the block.
	 * @param z The Z coordinate of the block.
	 * @param remove If the blocks should be removed after reaching the bottom
	 * of the buffer, or stopped.
	 */
	public static void simulateFall(CuboidBlockMaterialBuffer buffer, int x, int y, int z, boolean remove) {
		if (!buffer.isInside(x, y, z)) {
			return;
		}
		BlockMaterial falling = buffer.get(x, y, z);
		if (!(falling instanceof SolidMoving)) {
			return;
		}
		int baseY = buffer.getBase().getFloorY();
		for (int obstacleY = y; --obstacleY >= baseY; ) {
			if (FallingBlock.isFallingObstacle(buffer.get(x, obstacleY, z))) {
				// obstacle found
				if (obstacleY == y - 1) {
					// right underneath, nowhere to fall
					return;
				}
				if (buffer.get(x, ++obstacleY, z).isPlacementObstacle()) {
					// blocks can't stay here. Delete them
					remove = true;
					break;
				}
				do {
					// move the blocks above the obstacle
					buffer.set(x, obstacleY++, z, falling);
					buffer.set(x, y++, z, VanillaMaterials.AIR);
				} while ((falling = buffer.get(x, y, z)) instanceof SolidMoving);
				return;
			}
		}
		// no obstacle found
		if (remove) {
			// delete the blocks
			final int topY = buffer.getTop().getFloorY() - 1;
			do {
				buffer.set(x, y++, z, VanillaMaterials.AIR);
			} while (y <= topY && buffer.get(x, y, z) instanceof SolidMoving);
		} else {
			// move the blocks to the bottom of the buffer
			do {
				buffer.set(x, baseY++, z, falling);
				buffer.set(x, y++, z, VanillaMaterials.AIR);
			} while ((falling = buffer.get(x, y, z)) instanceof SolidMoving);
		}
	}
}
