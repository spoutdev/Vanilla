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
package org.spout.vanilla.plugin.material.block.component;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.plugin.component.substance.material.Anvil;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Directional;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;
import org.spout.vanilla.plugin.util.PlayerUtil;

public class AnvilBlock extends ComponentMaterial implements Directional {
	public AnvilBlock(String name, int id) {
		super(name, id, Anvil.class, VanillaMaterialModels.ANVIL);
		this.setHardness(5.0F).setResistance(6000.0F);
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
			// block.getWorld().createAndSpawnEntity(block.getPosition(), block.getComponent().getClass(), LoadOption.NO_LOAD);
			block.setMaterial(VanillaMaterials.AIR);
		}
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setFacing(block, PlayerUtil.getFacing(cause));
	}

	/**
	 * Gets the damage state of an Anvil, a value from 0 to 3
	 * 
	 * @param block to get the damage of
	 * @return Damage value
	 */
	public int getDamage(Block block) {
		return block.getDataField(0xc);
	}

	/**
	 * Sets the damage state of an Anvil
	 * 
	 * @param block to set the damage for
	 * @param damage state to set to, a value from 0 to 3
	 */
	public void setDamage(Block block, int damage) {
		block.setDataField(0xc, damage);
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.NESW.get(block.getDataField(0x3));
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setDataField(0x3, BlockFaces.NESW.indexOf(facing, 0));
	}
}
