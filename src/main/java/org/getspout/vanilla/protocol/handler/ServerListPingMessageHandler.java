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
package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.ServerListPingMessage;

/**
 * Format: (MOTD)/u00A7(# online)/u00A7(Max Players) /u00A7(Protocol Version
 * (This is added in case someone finds it useful, since it's not used by the
 * vanilla client.))
 */
public class ServerListPingMessageHandler extends MessageHandler<ServerListPingMessage> {
	@Override
	public void handle(Session session, Player player, ServerListPingMessage message) {
		//ServerListPingEvent event = EventFactory.onServerListPing(session.getAddress().getAddress(), session.getServer().getMotd(), session.getServer().getOnlinePlayers().length, session.getServer().getMaxPlayers());
		//String text = event.getMotd() + "\u00A7" + event.getNumPlayers();
		//text += "\u00A7" + event.getMaxPlayers() + "\u00A7" + SpoutServer.PROTOCOL_VERSION;
		//session.send(new KickMessage(text));
	}
}