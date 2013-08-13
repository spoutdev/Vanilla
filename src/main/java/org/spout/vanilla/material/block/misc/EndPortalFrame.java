/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.material.block.misc;

import org.spout.api.event.Cause;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.util.PlayerUtil;
import org.spout.vanilla.world.generator.object.VanillaObjects;

public class EndPortalFrame extends VanillaBlockMaterial implements Directional {
	public EndPortalFrame(String name, int id) {
		//TODO: Box Shape
		super(name, id, VanillaMaterialModels.END_PORTAL_FRAME, null);
		this.setHardness(-1.0F).setResistance(18000000.0F);
		this.setOcclusion((short) 0, BlockFace.BOTTOM);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	public boolean hasEyeOfTheEnder(Block block) {
		return block.isDataBitSet(0x4);
	}

	public void setEyeOfTheEnder(Block block, boolean eyeOfTheEnder) {
		if (hasEyeOfTheEnder(block) == eyeOfTheEnder) {
			return;
		}
		block.setDataBits(0x4, eyeOfTheEnder);
		final World w = block.getWorld();
		final int x = block.getX();
		final int y = block.getY();
		final int z = block.getZ();
		if (eyeOfTheEnder && VanillaObjects.THE_END_PORTAL.isActive(w, x, y, z)) {
			VanillaObjects.THE_END_PORTAL.setActive(w, x, y, z, true);
		}
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.WNES.get(block.getBlockData() & 0x3);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setDataField(0x3, BlockFaces.WNES.indexOf(facing, 0));
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setFacing(block, PlayerUtil.getFacing(cause).getOpposite());
	}
}
