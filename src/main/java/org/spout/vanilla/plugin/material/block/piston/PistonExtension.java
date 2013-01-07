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
package org.spout.vanilla.plugin.material.block.piston;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.plugin.data.MoveReaction;
import org.spout.vanilla.plugin.material.block.Directional;
import org.spout.vanilla.plugin.material.block.Solid;

public class PistonExtension extends Solid implements Directional {
	public PistonExtension(String name, int id) {
		super(name, id, (String) null);
		this.setHardness(0.5F).setResistance(0.8F).setTransparent();
	}

	@Override
	public boolean onDestroy(Block block, Cause<?> cause) {
		block = block.translate(this.getFacing(block).getOpposite());
		BlockMaterial mat = block.getMaterial();
		if (mat instanceof PistonBlock) {
			return mat.onDestroy(block, cause);
		} else {
			return super.onDestroy(block, cause);
		}
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace facing, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		return false;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public BlockFace getFacing(Block block) {
		return PistonBlock.BTEWNS.get(block.getDataField(0x7));
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData(PistonBlock.BTEWNS.indexOf(facing, 1));
	}
}
