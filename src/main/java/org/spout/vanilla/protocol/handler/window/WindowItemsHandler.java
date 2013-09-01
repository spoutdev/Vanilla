/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.handler.window;

import org.spout.api.entity.Player;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.MessageHandler;

import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.protocol.msg.window.WindowItemsMessage;

public class WindowItemsHandler extends MessageHandler<WindowItemsMessage> {
	@Override
	public void handleClient(ClientSession session, WindowItemsMessage msg) {
		Player player = session.getPlayer();
		WindowHolder holder = player.get(WindowHolder.class);
		if (holder == null) {
			return;
		}
		Window window = holder.getActiveWindow();
		ItemStack[] slots = msg.getItems();
		for (int i = 0; i < slots.length; i++) {
			Slot entry = window.getSlot(i);
			if (entry == null) {
				return;
			}
			entry.getInventory().set(entry.getIndex(), slots[i]);
		}
	}
}
