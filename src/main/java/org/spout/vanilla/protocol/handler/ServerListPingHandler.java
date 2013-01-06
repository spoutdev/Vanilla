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
package org.spout.vanilla.protocol.handler;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.chat.VanillaStyleHandler;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.event.game.ServerListPingEvent;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerKickMessage;

public class ServerListPingHandler extends MessageHandler<ServerListPingMessage> {
	@Override
	public void handleServer(Session session, ServerListPingMessage message) {
		Server server = (Server) Spout.getEngine();
		String protocol = VanillaPlugin.getInstance().getDescription().getData("protocol");
		String mcV = VanillaPlugin.getInstance().getDescription().getVersion().trim().split(" ")[0];
		ChatArguments chatArgs = VanillaStyleHandler.INSTANCE.extractArguments(VanillaConfiguration.MOTD.getString());
		ServerListPingEvent event = Spout.getEventManager().callEvent(new ServerListPingEvent(session.getAddress().getAddress(), chatArgs.asString(VanillaStyleHandler.ID), server.getOnlinePlayers().length, server.getMaxPlayers()));
		session.send(false, true, new PlayerKickMessage('\u00A7' + "1" + '\u0000' + protocol + '\u0000' + mcV + '\u0000' + event.getMessage()));
	}
}
