package org.getspout.vanilla.protocol.handler;

import org.getspout.api.entity.Entity;
import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.EntityActionMessage;

/**
 * A {@link MessageHandler} which handles {@link Entity} action messages.
 */
public final class EntityActionMessageHandler extends MessageHandler<EntityActionMessage> {
	@Override
	public void handle(Session session, Player player, EntityActionMessage message) {
		/*switch (message.getAction()) {
			case EntityActionMessage.ACTION_SNEAKING:
				player.setSneaking(true);
				break;
			case EntityActionMessage.ACTION_STOP_SNEAKING:
				player.setSneaking(false);
				break;
			default:
				// TODO: bed support
				return;
		}*/
	}
}
