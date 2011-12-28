package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Protocol;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.VanillaIdentificationMessage;

public class VanillaIdentificationMessageHandler extends MessageHandler<VanillaIdentificationMessage> {
		@Override
		public void handle(Session session, Player player, VanillaIdentificationMessage message) {
			session.setProtocol(Protocol.getProtocol(message.getSeed()));
		}
}
