/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.block;

import org.spout.vanilla.material.block.attachable.GroundAttachable;

public class DoorBlock extends GroundAttachable {
	private final boolean openByHand;
	public static final short HINGE_NORTH_WEST = 0x0;
	public static final short HINGE_NORTH_EAST = 0x1;
	public static final short HINGE_SOUTH_EAST = 0x2;
	public static final short HINGE_SOUTH_WEST = 0x3;

	public DoorBlock(String name, int id, boolean openByHand) {
		super(name, id);
		this.openByHand = openByHand;
	}

	public boolean isOpenByHand() {
		return openByHand;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
