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

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Portal;

public class EndPortal extends Portal {
	public EndPortal(String name, int id) {
		super(name, id, null);
		this.setHardness(-1.0F).setResistance(6000000.0F);
	}

	@Override
	public BlockMaterial getFrameMaterial() {
		return VanillaMaterials.END_PORTAL_FRAME;
	}

	@Override
	public byte getLightLevel(short data) {
		return 15;
	}

	@Override
	public boolean canCreate(Block block, short data, Cause<?> cause) {
		for (BlockFace face : BlockFaces.NESW) {
			Block rel = block.translate(face);
			BlockMaterial mat = rel.getMaterial();
			if (mat == this) {
				continue;
			} else if (mat == VanillaMaterials.END_PORTAL_FRAME &&
					VanillaMaterials.END_PORTAL_FRAME.hasEyeOfTheEnder(rel) &&
					VanillaMaterials.END_PORTAL_FRAME.getFacing(rel).getOpposite() == face) {
				continue;
			}
			return false;
		}
		return true;
	}
}
