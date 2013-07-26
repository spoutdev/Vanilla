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
package org.spout.vanilla.event.block.network;

import org.spout.api.event.HandlerList;
import org.spout.api.event.ProtocolEvent;
import org.spout.api.geo.cuboid.Block;

import org.spout.nbt.CompoundMap;

public class EntityTileDataEvent extends ProtocolEvent {
	public static final byte SET_MONSTER_SPAWNER_CREATURE = 1;
	private static final HandlerList handlers = new HandlerList();
	private final Block block;
	private final byte action;
	private final CompoundMap data;

	/**
	 * Constructs a new Data event for the block specified
	 * @param block for the data
	 * @param action to perform
	 * @param data to use, max 3 elements
	 */
	public EntityTileDataEvent(Block block, byte action, CompoundMap data) {
		this.block = block;
		this.action = action;
		this.data = data;
	}

	/**
	 * Gets the Block the data is meant for
	 * @return Block
	 */
	public Block getBlock() {
		return this.block;
	}

	/**
	 * Gets the action to perform
	 * @return action Id
	 */
	public byte getAction() {
		return this.action;
	}

	/**
	 * Gets the data to use
	 * @return data array
	 */
	public CompoundMap getData() {
		return this.data;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
