package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.KickMessage;

public final class KickMessageHandler extends MessageHandler<KickMessage> {
	@Override
	public void handle(Session session, Player player, KickMessage message) {
		//session.disconnect("Goodbye!", true);
	}
}
