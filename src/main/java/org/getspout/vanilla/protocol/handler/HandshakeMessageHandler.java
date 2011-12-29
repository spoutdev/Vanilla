package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.HandshakeMessage;

public final class HandshakeMessageHandler extends MessageHandler<HandshakeMessage> {
	@Override
	public void handle(Session session, Player player, HandshakeMessage message) {
		/*SpoutSession.State state = session.getState();
		if (state == SpoutSession.State.EXCHANGE_HANDSHAKE) {
			session.setState(State.EXCHANGE_IDENTIFICATION);
			if (session.getServer().getOnlineMode()) {
				session.send(new HandshakeMessage(session.getSessionId()));
			} else {
				session.send(new HandshakeMessage("-"));
			}
		} else {
			session.disconnect("Handshake already exchanged.");
		}*/
	}
}
