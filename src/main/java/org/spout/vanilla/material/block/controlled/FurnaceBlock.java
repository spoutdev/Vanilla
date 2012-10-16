/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.material.block.controlled;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.data.drops.flag.ToolTypeFlags;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class FurnaceBlock extends Solid implements Directional {
	public static final float SMELT_TIME = 10.f;
	private final boolean burning;

	public FurnaceBlock(String name, int id, boolean burning) {
		super(name, id);
		this.burning = burning;
		this.setHardness(3.5F).setResistance(5.8F);
		this.getDrops().NOT_CREATIVE.addFlags(ToolTypeFlags.PICKAXE);
	}

	@Override
	public byte getLightLevel(short data) {
		return burning ? (byte) 13 : (byte) 0;
	}

	/**
	 * Gets if this furnace block material is burning
	 * @return True if burning
	 */
	public boolean isBurning() {
		return this.burning;
	}

	/**
	 * Sets the burning state of a Furnace block
	 * @param block of the Furnace
	 * @param burning state to set to
	 */
	public void setBurning(Block block, boolean burning) {
		block.setMaterial(burning ? VanillaMaterials.FURNACE_BURNING : VanillaMaterials.FURNACE, block.getData());
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.EWNS.get(block.getData() - 2);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData((short) (BlockFaces.EWNS.indexOf(facing, 0) + 2));
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, clickedPos, isClickedBlock)) {
			this.setFacing(block, VanillaPlayerUtil.getFacing(block.getSource()).getOpposite());
			return true;
		}

		return false;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}
}
