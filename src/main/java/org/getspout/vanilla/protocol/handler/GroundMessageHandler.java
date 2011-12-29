package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.GroundMessage;

public class GroundMessageHandler extends MessageHandler<GroundMessage> {
	@Override
	public void handle(Session session, Player player, GroundMessage message) {
		/*if (player != null) {
			player.setOnGround(message.isOnGround());
		}*/
	}
}
