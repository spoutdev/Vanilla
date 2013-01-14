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
package org.spout.vanilla.plugin.component.substance;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.api.data.PaintingType;
import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.protocol.entity.object.PaintingEntityProtocol;

public class Painting extends EntityComponent {
	private PaintingType type;
	private BlockFace face;

	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new PaintingEntityProtocol());
	}

	public PaintingType getType() {
		return type;
	}

	public void setType(PaintingType type) {
		this.type = type;
	}

	public BlockFace getFace() {
		return face;
	}

	public void setFace(BlockFace face) {
		this.face = face;
	}

	public int getNativeFace() {
		return getNativeFace(face);
	}

	public static int getNativeFace(BlockFace face) {
		switch (face) {
			case NORTH:
				return 3;
			case SOUTH:
				return 1;
			case EAST:
				return 0;
			case WEST:
				return 2;
			default:
				return -1;
		}
	}
}
