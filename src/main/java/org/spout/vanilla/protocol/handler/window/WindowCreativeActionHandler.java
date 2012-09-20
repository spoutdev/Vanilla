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

import org.spout.api.entity.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.component.inventory.window.Window;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.inventory.window.ClickArguments;
import org.spout.vanilla.inventory.window.InventoryEntry;
import org.spout.vanilla.protocol.msg.window.WindowCreativeActionMessage;

public class WindowCreativeActionHandler extends MessageHandler<WindowCreativeActionMessage> {
	@Override
	public void handleServer(Session session, WindowCreativeActionMessage message) {
		if (!session.hasPlayer() || !session.getPlayer().has(Human.class)) {
			return;
		}
		Player holder = session.getPlayer();
		Human human = holder.get(Human.class);
		if (human.isSurvival()) {
			holder.kick("Attempted to use the creative inventory while on survival.");
			return;
		}

		Window window = holder.get(Window.class);
		if (message.get() == null) {
			//Taking item from existing slot
			window.setCursorItem(null);
			ClickArguments args = window.getClickArguments(message.getSlot(), false, false);
			if (args != null) {
				window.onClick(args);
			}
		} else if (message.getSlot() == -1) {
			window.setCursorItem(message.get());
			window.onOutsideClick();
		} else {
			InventoryEntry entry = window.getInventoryEntry(message.getSlot());
			if (entry != null) {
				window.onCreativeClick(entry.getInventory(), entry.getSlot(), message.get());
			}
		}
	}
}
