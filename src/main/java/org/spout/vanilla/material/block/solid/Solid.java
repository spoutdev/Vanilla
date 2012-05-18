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
package org.spout.vanilla.material.block.solid;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.MoveReaction;

public class Solid extends VanillaBlockMaterial {
	public Solid(String name, int id) {
		super(name, id);
	}

	public Solid(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, id, data, parent);
	}

	/**
	 * Gets whether this material can be move using a MovingBlock entity
	 */
	public boolean isMoving() {
		return false;
	}

	@Override
	public boolean isRedstoneConductor() {
		return true;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.ALLOW;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material.equals(VanillaMaterials.FIRE)) {
			//solids that can burn have fire on all sides
			//those that do not only allow fire on top
			if (this.canBurn()) {
				return true;
			} else {
				return face == BlockFace.TOP;
			}
		} else {
			return true;
		}
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		if (this.isMoving()) {
			if (!block.translate(BlockFace.BOTTOM).getMaterial().isPlacementObstacle()) {
				//Just do nothing for now...
				//block.getSubMaterial().onDestroy(block);
				//world.createAndSpawnEntity(block.getPosition(), new MovingBlock(this)); TODO: We aren't ready for this lol.
			}
		}
	}
}
