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
package org.spout.vanilla.protocol.handler;

import java.util.Map.Entry;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.protocol.msg.window.WindowClickMessage;
import org.spout.vanilla.protocol.msg.window.WindowTransactionMessage;
import org.spout.vanilla.window.ClickArgs;
import org.spout.vanilla.window.Window;

public final class WindowClickMessageHandler extends MessageHandler<WindowClickMessage> {
	@Override
	public void handleServer(Session session, Player player, WindowClickMessage message) {
		Entity entity = player.getEntity();
		if (!(entity.getController() instanceof VanillaPlayer)) {
			return;
		}

		VanillaPlayer controller = (VanillaPlayer) entity.getController();
		Window window = controller.getActiveWindow();
		boolean result = false;
		try {
			if (message.getSlot() == 64537) {
				// outside the window
				result = window.onOutsideClick();
			} else {
				// inside the window
				Entry<InventoryBase, Integer> entry = window.getInventoryEntry(message.getSlot());
				if (entry != null) {
					result = window.onClick(entry.getKey(), entry.getValue(), new ClickArgs(message.isRightClick(), message.isShift()));
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		session.send(false, new WindowTransactionMessage(message.getWindowInstanceId(), message.getTransaction(), result));
	}
}
