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
package org.spout.vanilla.plugin.material.block.solid;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.model.Model;
import org.spout.api.resource.ResourcePointer;

import org.spout.vanilla.plugin.data.tool.ToolType;
import org.spout.vanilla.plugin.material.block.Directional;
import org.spout.vanilla.plugin.material.block.Solid;
import org.spout.vanilla.plugin.util.PlayerUtil;
import org.spout.vanilla.plugin.util.resources.ModelUtil;

public class Pumpkin extends Solid implements Directional {
	private final boolean lantern;

	public Pumpkin(short dataMask, String name, int id, ResourcePointer<Model> model, boolean lantern) {
		super(dataMask, name + "_E", id, ModelUtil.getDirectionalModel(model, BlockFace.EAST));
		this.setHardness(1.0F).setResistance(1.7F).addMiningType(ToolType.AXE);
		this.lantern = lantern;
		// Register the directional submaterials
		if (model == null) {
			return;
		}
		int i = 1;
		for (BlockFace face : new BlockFaces(BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH)) {
			new Pumpkin(name + "_" + face.name().charAt(0), id, i++, this, ModelUtil.getDirectionalModel(model, face), lantern);
		}
		new Pumpkin(name, id, i, this, model, lantern);
	}

	private Pumpkin(String name, int id, int data, Pumpkin parent, ResourcePointer<Model> model, boolean lantern) {
		super(name, id, data, parent, model);
		this.setHardness(1.0F).setResistance(1.7F).addMiningType(ToolType.AXE);
		this.lantern = lantern;
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
		return lantern ? (byte) 15 : (byte) 0;
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setFacing(block, PlayerUtil.getFacing(cause).getOpposite());
	}

	/**
	 * Whether this pumpkin block material is a jack o' lantern
	 * @return true if jack o' lantern
	 */
	public boolean isLantern() {
		return lantern;
	}
}
