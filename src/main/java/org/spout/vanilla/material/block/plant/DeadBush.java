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
package org.spout.vanilla.material.block.plant;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Plant;
import org.spout.vanilla.material.block.attachable.GroundAttachable;

public class DeadBush extends GroundAttachable implements Plant, InitializableMaterial {
	protected DeadBush(String name, int id, short data, String model) {
		super(data, name, id, model);
	}

	protected DeadBush(String name, int id, int data, DeadBush parent, String model) {
		super(name, id, data, parent, model);
	}

	public DeadBush(String name, int id) {
		super(name, id, "model://Vanilla/materials/block/nonsolid/deadgrass/deadgrass.spm");
	}

	public DeadBush(short dataMask, String name, int id) {
		super(dataMask, name, id, "model://Vanilla/materials/block/nonsolid/deadgrass/deadgrass.spm");
	}

	public DeadBush(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, id, data, parent, "model://Vanilla/materials/block/nonsolid/deadgrass/deadgrass.spm");
	}

	@Override
	public void initialize() {
		this.setLiquidObstacle(false);
		this.setHardness(0.0F).setResistance(0.0F).setTransparent();
	}

	@Override
	public boolean canSupport(BlockMaterial mat, BlockFace face) {
		return false;
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (super.canAttachTo(block, face)) {
			return block.isMaterial(VanillaMaterials.SAND, VanillaMaterials.FLOWER_POT_BLOCK);
		}
		return false;
	}
}
