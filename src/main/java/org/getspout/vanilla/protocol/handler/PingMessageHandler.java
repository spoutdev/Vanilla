package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.PingMessage;

public class PingMessageHandler extends MessageHandler<PingMessage> {
	@Override
	public void handle(Session session, Player player, PingMessage message) {
		//if (session.getPingMessageId() == message.getPingId()) {
		//	session.pong();
		//}
	}
}
