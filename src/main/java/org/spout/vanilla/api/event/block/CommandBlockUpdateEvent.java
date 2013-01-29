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
package org.spout.vanilla.api.event.block;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.vanilla.plugin.component.substance.material.CommandBlockComponent;

/**
 * Fired when the command input of a command block changes
 */
public class CommandBlockUpdateEvent extends Event implements ProtocolEvent, Cancellable {
	private static HandlerList handlers = new HandlerList();
	
	private CommandBlockComponent commandBlock;
	private String command;
	private Cause<?> cause;

	public CommandBlockUpdateEvent(CommandBlockComponent commandBlock, String command, Cause<?> cause) {
		this.commandBlock = commandBlock;
		this.command = command == null ? "" : command;
		this.cause = cause;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	/**
	 * Get the cause of the changes
	 * @return cause which changes the command of the command block
	 */
	public Cause<?> getSource() {
		return this.cause;
	}
	
	/**
	 * Get the block component associated to the event
	 * @return the command block component
	 */
	public CommandBlockComponent getCommandBlock() {
		return this.commandBlock;
	}
	
	/**
	 * Get the inputed command
	 * @return the command that should be put in the command block
	 */
	public String getCommand() {
		return this.command;
	}
	
	/**
	 * Set the inputed command
	 * @param the command that should be put in the command block
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}
}
