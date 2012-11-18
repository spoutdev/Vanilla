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
package org.spout.vanilla.protocol.handler.window;

import java.util.logging.Level;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.component.inventory.WindowHolder;
import org.spout.vanilla.inventory.window.ClickArguments;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.protocol.msg.window.WindowClickMessage;
import org.spout.vanilla.protocol.msg.window.WindowTransactionMessage;

public final class WindowClickHandler extends MessageHandler<WindowClickMessage> {
	@Override
	public void handleServer(Session session, WindowClickMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		Player player = session.getPlayer();
		Window window = player.get(WindowHolder.class).getActiveWindow();
		boolean result = false;
		int slot = message.getSlot();
		debug("Window: " + window.getClass().getCanonicalName());
		debug("Window clicked at slot " + slot);
		if (slot == 64537) {
			debug("Outside onClick");
			result = window.onOutsideClick();
		} else {
			debug("Getting args");
			ClickArguments args = window.getClickArguments(slot, message.isRightClick(), message.isShift());
			if (args != null) {
				debug("Clicking");
				result = window.onClick(args);
			}
		}
		session.send(false, new WindowTransactionMessage(window, message.getTransaction(), result));
	}

	private void debug(String msg) {
		if (Spout.debugMode()) {
			Spout.getLogger().log(Level.INFO, msg);
		}
	}
}
