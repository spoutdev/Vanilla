/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.event.block;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.block.BlockChangeEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockSnapshot;

/**
 * Event which is called when a block forms or spreads in the world.
 * For example: Snow, Ice, etc
 * todo implement calling of this event
 */
public class BlockFormEvent extends BlockChangeEvent implements Cancellable {
	/**
	 * The different causes why a Block is formed in the world.
	 */
	public static enum FormCause {
		/**
		 * Block spread randomly
		 */
		SPREAD_RANDOM,
		/**
		 * Block spread
		 */
		SPREAD,
		/**
		 * Block formed due to world conditions (for example Snow)
		 */
		FORMING,
	}

	private static HandlerList handlers = new HandlerList();
	private final FormCause formCause;

	public BlockFormEvent(Block block, BlockSnapshot newState, Cause<?> reason, FormCause formCause) {
		super(block, newState, reason);
		this.formCause = formCause;
	}

	/**
	 * The reason why the block formed
	 * @return FormCause
	 */
	public FormCause getFormCause() {
		return formCause;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
