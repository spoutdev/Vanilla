package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.VanillaPingMessage;

public class VanillaPingMessageHandler extends MessageHandler<VanillaPingMessage> {

	@Override
	public void handle(Session session, Player player, VanillaPingMessage message) {
		System.out.println("Ping message received");
	}
	
}
