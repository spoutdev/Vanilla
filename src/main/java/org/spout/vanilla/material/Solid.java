/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

public class Solid extends VanillaBlockMaterial {
	private final boolean moving;

	public Solid(String name, int id, boolean canMove) {
		super(name, id);
		moving = canMove;
	}

	public Solid(String name, int id) {
		super(name, id);
		moving = false;
	}

	public Solid(String name, int id, int data, Material parent) {
		super(name, id, data, parent);
		moving = false;
	}

	public Solid(String name, int id, int data, Material parent, boolean canMove) {
		super(name, id, data, parent);
		moving = canMove;
	}

	@Override
	public boolean hasPhysics() {
		return moving;
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		if (moving) {
			if (!block.translate(BlockFace.BOTTOM).getMaterial().isPlacementObstacle()) {
				//Just do nothing for now...
				//block.getSubMaterial().onDestroy(block);
				//world.createAndSpawnEntity(block.getPosition(), new MovingBlock(this)); TODO: We aren't ready for this lol.
			}
		}
	}
}
