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
package org.spout.vanilla.material.block.misc;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.block.attachable.AbstractAttachable;
import org.spout.vanilla.material.block.attachable.PointAttachable;
import org.spout.vanilla.util.resources.ModelUtil;

public class Torch extends AbstractAttachable implements PointAttachable {
	public Torch(short dataMask, String name, int id, String model) {
		super(dataMask, name, id, model);
		this.setAttachable(BlockFaces.NSEWB).setLiquidObstacle(false).setHardness(0.0F).setResistance(0.0F).setTransparent();
		// Register the directional submaterials
		if (model == null) {
			return;
		}
		int i = 1;
		for (BlockFace face : BlockFaces.NSEWB) {
			new Torch(name + "_" + face.name().charAt(0), id, i++, this, ModelUtil.getDirectionalModel(model, face));
		}
	}

	private Torch(String name, int id, int data, Torch parent, String model) {
		super(name, id, data, parent, model);
		this.setAttachable(BlockFaces.NSEWB).setLiquidObstacle(false).setHardness(0.0F).setResistance(0.0F).setTransparent();
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		block.setData((short) (BlockFaces.NSEWB.indexOf(attachedFace, 4) + 1), cause);
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.NSEWB.get(data - 1, BlockFace.BOTTOM);
	}

	@Override
	public byte getLightLevel(short data) {
		return 14;
	}

	@Override
	public boolean canSeekAttachedAlternative() {
		return true;
	}
}
