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
package org.spout.vanilla.event.block;

import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.event.ProtocolEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;

public class BlockActionEvent extends ProtocolEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Block block;
	private final BlockMaterial material;
	private final byte data1;
	private final byte data2;

	public BlockActionEvent(Block block, BlockMaterial material, byte data1, byte data2) {
		this.block = block;
		this.data1 = data1;
		this.data2 = data2;
		this.material = material;
	}

	public Block getBlock() {
		return this.block;
	}

	public byte getData1() {
		return this.data1;
	}

	public byte getData2() {
		return this.data2;
	}

	public BlockMaterial getMaterial() {
		return this.material;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
