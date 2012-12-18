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
package org.spout.vanilla.component.substance.material;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.block.SignUpdateEvent;

public class Sign extends VanillaBlockComponent {
	/**
	 * Gets a copy of the text from this sign
	 * @return copy of the text
	 */
	public String[] getText() {
		String[] raw = getData().get(VanillaData.SIGN_TEXT);
		String[] copy = new String[raw.length];
		System.arraycopy(raw, 0, copy, 0, raw.length);
		return copy;
	}

	/**
	 * Sets the text on this sign. The text must be 4 lines, no longer than 16 chars in length
	 * @param text to set
	 * @param cause of the sign change
	 */
	public void setText(String[] text, Cause<?> cause) {
		if (text == null || text.length != 4) {
			throw new IllegalArgumentException("Text must be an array of length 4");
		}
		if (cause == null) {
			throw new IllegalArgumentException("Source may not be null");
		}
		for (int i = 0; i < 4; i++) {
			if (text[i].length() > 16) {
				text[i] = text[i].substring(0, 16);
			}
		}
		SignUpdateEvent event = new SignUpdateEvent(this, text, cause);
		//Call event to plugins, allow them to alter it
		Spout.getEventManager().callEvent(event);
		//Send event to protocol
		if (!event.isCancelled()) {
			for (Player p : this.getOwner().getChunk().getObservingPlayers()) {
				p.getNetworkSynchronizer().callProtocolEvent(event);
			}
			getData().put(VanillaData.SIGN_TEXT, event.getLines());
		}
	}
}
