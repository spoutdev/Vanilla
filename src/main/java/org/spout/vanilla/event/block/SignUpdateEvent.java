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
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.protocol.event.ProtocolEvent;

import org.spout.vanilla.component.substance.material.Sign;

/**
 * Event which is called when the text on a sign changes
 */
public class SignUpdateEvent extends Event implements ProtocolEvent, Cancellable {
	private static HandlerList handlers = new HandlerList();
	private final Sign sign;
	private final String[] newLines;
	private final Cause<?> cause;

	public SignUpdateEvent(Sign sign, String[] newLines, Cause<?> cause) {
		this.sign = sign;
		this.newLines = newLines;
		this.cause = cause;
	}

	/**
	 * Get the cause of the changes
	 * @return cause which changes the text on a sign
	 */
	public Cause<?> getSource() {
		return cause;
	}

	/**
	 * Get the sign which text is being changed
	 * @return sign which is being changed
	 */
	public Sign getSign() {
		return sign;
	}

	/**
	 * Get the text which should be placed on the sign.
	 * @return text which is placed on the sign 1 - 4 lines
	 */
	public String[] getLines() {
		return newLines;
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
