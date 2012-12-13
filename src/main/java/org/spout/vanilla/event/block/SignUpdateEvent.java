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

import java.util.Arrays;

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
	private final Cause<?> cause;
	private String[] newLines;

	public SignUpdateEvent(Sign sign, String[] newLines, Cause<?> cause) {
		this.sign = sign;
		changeLines(newLines);
		this.cause = cause;
	}

	public static HandlerList getHandlerList() {
		return handlers;
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
		return Arrays.copyOf(newLines, newLines.length);
	}

	/**
	 * Set the text which should be placed on the sign.
	 * @param newLines text which is placed on the sign 1 - 4 lines
	 */
	public void setLines(String[] newLines) {
		this.newLines = Arrays.copyOf(newLines, newLines.length);
	}

	/**
	 * Sets a single line for the sign involved in this event. Index below 0 and above the ArraySize of the sign will throw an
	 * {@link ArrayIndexOutOfBoundsException}
	 * @param index index of the line to set
	 * @param line text to set
	 */
	public void setLine(int index, String line) {
		newLines[checkIndex(index)] = line;
	}

	/**
	 * Gets a single line from the sign involved in this event. Index below 0 and above the ArraySize of the sign will throw an
	 * {@link ArrayIndexOutOfBoundsException}
	 * @param index index of the line to get
	 * @return string of the line which is at the index
	 */
	public String getLine(int index) {
		return newLines[checkIndex(index)];
	}

	private void changeLines(String[] overwriteLines) {
		for (int i = 0; i < checkIndex(overwriteLines.length); i++) {
			newLines[i] = overwriteLines[i];
		}
	}

	private int checkIndex(int index) {
		if (!(index >= 0 && index <= newLines.length)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return index;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
