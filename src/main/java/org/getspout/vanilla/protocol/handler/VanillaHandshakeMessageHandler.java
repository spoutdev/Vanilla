package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.VanillaHandshakeMessage;

public class VanillaHandshakeMessageHandler extends MessageHandler<VanillaHandshakeMessage> {
	@Override
	public void handle(Session session, Player player, VanillaHandshakeMessage message) {
		throw new IllegalStateException("Handshake messages received after protocol set");
	}
}