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
package org.spout.vanilla.material.block.component.chest;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.component.substance.material.chest.AbstractChest;
import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.component.ComponentMaterial;
import org.spout.vanilla.util.PlayerUtil;

public abstract class AbstractChestBlock extends ComponentMaterial implements Directional, Fuel {
	public final float BURN_TIME = 15;

	public AbstractChestBlock(String name, int id, Class<? extends AbstractChest> componentType, String model) {
		super(name, id, componentType, model, null);
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASS_GUITAR;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
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
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		if (super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause)) {
			BlockFace facing = PlayerUtil.getFacing(cause).getOpposite();
			//search for neighbor and align
			Block neigh;
			for (BlockFace face : BlockFaces.NESW) {
				if ((neigh = block.translate(face)).getMaterial().equals(this)) {
					if (face == facing || face == facing.getOpposite()) {
						if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
							facing = BlockFace.WEST;
						} else {
							facing = BlockFace.SOUTH;
						}
					}
					this.setFacing(neigh, facing);
					break;
				}
			}
			this.setFacing(block, facing);
			return true;
		}
		return false;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
