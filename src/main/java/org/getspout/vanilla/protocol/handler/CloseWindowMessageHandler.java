package org.getspout.vanilla.protocol.handler;

import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.protocol.msg.CloseWindowMessage;

public final class CloseWindowMessageHandler extends MessageHandler<CloseWindowMessage> {
	@Override
	public void handle(Session session, Player player, CloseWindowMessage message) {
		if (player == null) {
			return;
		}

		/*if (player.getItemOnCursor() != null) {
			// player.getWorld().dropItem(player.getEyeLocation(), player.getItemInHand());
			if (player.getGameMode() != GameMode.CREATIVE) {
				player.getInventory().addItem(player.getItemOnCursor());
			}
			player.setItemOnCursor(null);
		}

		player.onClosedWindow();
		*/
	}
}
