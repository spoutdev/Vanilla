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
package org.spout.vanilla.protocol.handler.player;

import org.spout.api.entity.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.ServerSession;

import org.spout.vanilla.protocol.msg.player.PlayerChatMessage;

public final class PlayerChatHandler extends MessageHandler<PlayerChatMessage> {
	@Override
	public void handleClient(ClientSession session, PlayerChatMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		session.getPlayer().sendMessage(message.getMessage());
	}

	@Override
	public void handleServer(ServerSession session, PlayerChatMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();
		String text = message.getMessage().trim();
		if (text.length() > 100) {
			text = text.substring(0, 99);
		}

		String command;
		String[] args;

		if (text.startsWith("/")) {
			command = text.split(" ")[0].replaceFirst("/", "");
			int argsIndex = text.indexOf(" ") + 1;
			args = argsIndex > 0 ? text.substring(argsIndex).split(" ") : new String[0];
		} else {
			command = "say";
			args = text.split(" ");
		}

		player.processCommand(command, args);
	}
}
