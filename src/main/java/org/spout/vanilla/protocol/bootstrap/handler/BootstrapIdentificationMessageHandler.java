/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.bootstrap.handler;

import java.util.Arrays;
import java.util.Iterator;

import org.spout.api.Server;
import org.spout.api.event.Event;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.protocol.msg.IdentificationMessage;

public class BootstrapIdentificationMessageHandler extends MessageHandler<IdentificationMessage> {
	@Override
	public void handle(Session session, Player player, IdentificationMessage message) {
		
		//We check if the player is banned
		String playerName = message.getName();
		
		Server s = (Server) session.getGame();	
		
		Iterator<Player> bannedPlayerList = s.getBannedPlayers().iterator();
		
		while(bannedPlayerList.hasNext())
		{
			if (bannedPlayerList.next().getName().equalsIgnoreCase(playerName)) {
				session.disconnect("You are banned from this server!");
				session.dispose(false);
				return;
			}
		}
		if (s.isWhitelist())
		{
			//We check if the player is in the whitelist
			boolean isWhiteListed = false;
			Iterator<String> whiteList = Arrays.asList(s.getWhitelistedPlayers()).iterator();
			while(whiteList.hasNext()) {
				if (whiteList.next().equalsIgnoreCase(playerName)) {
					isWhiteListed = true;
				}
			}
			if (!isWhiteListed) {
				session.disconnect("You are not whitelisted!");
				session.dispose(false);
				return;
			}
			
		}
		
		Event event = new PlayerConnectEvent(session, message.getName());
		session.getGame().getEventManager().callEvent(event);
	}
}