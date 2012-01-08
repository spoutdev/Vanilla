/*
 * This file is part of Vanilla (http://www.spout.org/).
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
import org.getspout.vanilla.protocol.msg.AnimateEntityMessage;

/**
 * A {@link MessageHandler} which handles {@link org.bukkit.entity.Entity}
 * animation messages.
 */
public final class AnimateEntityMessageHandler extends MessageHandler<AnimateEntityMessage> {
	@Override
	public void handle(Session session, Player player, AnimateEntityMessage message) {
		/*Block block = player.getTargetBlock(null, 6);
		if (block == null || block.getTypeId() == BlockID.AIR) {
			if (EventFactory.onPlayerInteract(player, Action.LEFT_CLICK_AIR).isCancelled()) {
				return; // TODO: Item interactions
			}
		}
		if (EventFactory.onPlayerAnimate(player).isCancelled()) {
			return;
		}
		switch (message.getAnimation()) {
			case AnimateEntityMessage.ANIMATION_SWING_ARM:
				AnimateEntityMessage toSend = new AnimateEntityMessage(player.getEntityId(), AnimateEntityMessage.ANIMATION_SWING_ARM);
				for (SpoutPlayer observer : player.getWorld().getRawPlayers()) {
					if (observer != player && observer.canSee(player)) {
						observer.getSession().send(toSend);
					}
				}
				break;
			default:
				// TODO: other things?
				return;
		}
		*/
	}
}