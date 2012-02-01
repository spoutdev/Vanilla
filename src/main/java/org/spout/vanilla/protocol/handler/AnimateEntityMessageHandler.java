/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is licensed under the SpoutDev License Version 1.  
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the 
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.  
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license, 
 * including the MIT license.
 */
package org.spout.vanilla.protocol.handler;

import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.protocol.msg.AnimateEntityMessage;

/**
 * A {@link MessageHandler} which handles {@link org.spout.vanilla.entity.MinecraftEntity}
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