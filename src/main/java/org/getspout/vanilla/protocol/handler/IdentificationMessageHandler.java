/*
 * This file is part of Vanilla (http://www.getspout.org/).
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
import org.getspout.vanilla.protocol.msg.IdentificationMessage;

public final class IdentificationMessageHandler extends MessageHandler<IdentificationMessage> {
	@Override
	public void handle(Session session, Player player, IdentificationMessage message) {
		/*SpoutSession.State state = session.getState();

		// Are we at the proper stage?
		if (state == SpoutSession.State.EXCHANGE_IDENTIFICATION) {
			if (message.getId() < SpoutServer.PROTOCOL_VERSION) {
				session.disconnect("Outdated client!");
			} else if (message.getId() > SpoutServer.PROTOCOL_VERSION) {
				session.disconnect("Outdated server!");
			}
			boolean allow = true; // Default to okay

			// If we're in online mode, attempt to verify with mc.net
			if (session.getServer().getOnlineMode()) {
				allow = false;
				try {
					URL verify = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(message.getName(), "UTF-8") + "&serverId=" + URLEncoder.encode(session.getSessionId(), "UTF-8"));
					BufferedReader reader = new BufferedReader(new InputStreamReader(verify.openStream()));
					String result = reader.readLine();
					reader.close();
					allow = result.equals("YES"); // Get minecraft.net's result. If the result is YES, allow login to continue
				} catch (IOException ex) {
					// Something went wrong, disconnect the player
					session.getServer().getLogger().log(Level.WARNING, "Failed to authenticate {0} with minecraft.net: {1}", new Object[] {message.getName(), ex.getMessage()});
					session.disconnect("Player identification failed [" + ex.getMessage() + "]");
				}
			}

			// Was the player allowed?
			if (allow) {
				PlayerPreLoginEvent event = EventFactory.onPlayerPreLogin(message.getName(), session);
				if (event.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
					session.disconnect(event.getKickMessage());
				}
				SpoutPlayer newPlayer = new SpoutPlayer(session, event.getName()); // TODO case-correct the name
				session.setPlayer(newPlayer);
			} else {
				session.getServer().getLogger().log(Level.INFO, "Failed to authenticate {0} with minecraft.net.", message.getName());
				session.disconnect("Player identification failed!");
			}
			session.setState(State.GAME);
		} else {
			// Kick if they send ident at the wrong time
			boolean game = state == State.GAME;
			session.disconnect(game ? "Identification already exchanged." : "Handshake not yet exchanged.");
		}
		*/
	}
}