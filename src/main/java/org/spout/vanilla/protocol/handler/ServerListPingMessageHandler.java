/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.protocol.handler;

import org.spout.api.Spout;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.event.game.ServerListPingEvent;
import org.spout.vanilla.protocol.msg.KickMessage;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;

/**
 * Format: (MOTD)/u00A7(# online)/u00A7(Max Players) /u00A7(Protocol Version
 * (This is added in case someone finds it useful, since it's not used by the
 * vanilla client.))
 */
public class ServerListPingMessageHandler extends MessageHandler<ServerListPingMessage> {
	@Override
	public void handle(Session session, Player player, ServerListPingMessage message) {
		//TODO this is not called - why?
		System.out.println("Server list ping event");
		ServerListPingEvent event = new ServerListPingEvent(session.getAddress().getAddress(), "Default MOTD", Spout.getGame().getOnlinePlayers().length, Spout.getGame().getMaxPlayers());
		Spout.getGame().getEventManager().callEvent(event);
		session.send(new KickMessage(event.getMessage()));
	}
}