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
package org.spout.vanilla.material.block.solid;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.util.PlayerUtil;

public class JackOLantern extends Solid implements Directional {
	public static final JackOLantern NO_DIRECTION;
	public static final JackOLantern[] DIRECTIONS = new JackOLantern[4];

	static {
		DIRECTIONS[0] = new JackOLantern("Jack 'o' Lantern_E", 91, "model://Vanilla/materials/block/solid/jackolantern/jackolantern_E.spm");
		DIRECTIONS[1] = new JackOLantern("Jack 'o' Lantern_W", 91, 1, DIRECTIONS[0], "model://Vanilla/materials/block/solid/jackolantern/jackolantern_W.spm");
		DIRECTIONS[2] = new JackOLantern("Jack 'o' Lantern_N", 91, 2, DIRECTIONS[0], "model://Vanilla/materials/block/solid/jackolantern/jackolantern_N.spm");
		DIRECTIONS[3] = new JackOLantern("Jack 'o' Lantern_S", 91, 3, DIRECTIONS[0], "model://Vanilla/materials/block/solid/jackolantern/jackolantern_S.spm");
		NO_DIRECTION = new JackOLantern("Jack 'o' Lantern", 91, 4, DIRECTIONS[0], "model://Vanilla/materials/block/solid/jackolantern/jackolantern.spm");
	}

	private JackOLantern(String name, int id, String model) {
		super((short) 0x7, name, id, model);
		this.setHardness(1.0F).setResistance(1.7F).addMiningType(ToolType.AXE);
	}

	private JackOLantern(String name, int id, int data, JackOLantern parent, String model) {
		super(name, id, data, parent, model);
		this.setHardness(1.0F).setResistance(1.7F).addMiningType(ToolType.AXE);
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.EWNS.get(block.getData());
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData((short) BlockFaces.EWNS.indexOf(facing, 0));
	}

	@Override
	public byte getLightLevel(short data) {
		return (byte) 15;
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		if (super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause)) {
			this.setFacing(block, PlayerUtil.getFacing(cause).getOpposite());
			return true;
		}

		return false;
	}
}
