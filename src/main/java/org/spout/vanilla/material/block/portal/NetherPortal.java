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
package org.spout.vanilla.material.block.portal;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Portal;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

public class NetherPortal extends Portal {
	public NetherPortal(String name, int id) {
		super(name, id, null);
		this.setHardness(-1.0F).setResistance(0.0F);
	}

	@Override
	public BlockMaterial getFrameMaterial() {
		return VanillaMaterials.OBSIDIAN;
	}

	@Override
	public byte getLightLevel(short data) {
		return 11;
	}

	private boolean checkMirroredPosition(Block block, BlockFace mainFace) {
		return block.translate(mainFace).isMaterial(this, this.getFrameMaterial()) &&
				block.translate(mainFace.getOpposite()).isMaterial(this, this.getFrameMaterial());
	}

	@Override
	public boolean canCreate(Block block, short data, Cause<?> cause) {
		return this.checkMirroredPosition(block, BlockFace.TOP) &&
				(this.checkMirroredPosition(block, BlockFace.NORTH) ||
						this.checkMirroredPosition(block, BlockFace.EAST));
	}
}
