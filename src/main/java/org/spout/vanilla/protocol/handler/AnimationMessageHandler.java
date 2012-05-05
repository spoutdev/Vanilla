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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.handler;

import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.living.Living;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.protocol.msg.AnimationMessage;
import org.spout.vanilla.protocol.msg.EntityActionMessage;

public final class AnimationMessageHandler extends MessageHandler<AnimationMessage> {
	@Override
	public void handleServer(Session session, Player player, AnimationMessage message) {
		if (player.getEntity() == null) {
			return;
		}
		if (!(player.getEntity().getController() instanceof Living)) {
			return;
		}

		switch (message.getAnimation()) {
			case AnimationMessage.ANIMATION_CROUCH:
				session.send(new EntityActionMessage(player.getEntity().getId(), EntityActionMessage.ACTION_CROUCH));
				//TODO Set this in VanillaActionController as apparently any entity can crouch?
				break;
			case AnimationMessage.ANIMATION_UNCROUCH:
				session.send(new EntityActionMessage(player.getEntity().getId(), EntityActionMessage.ACTION_UNCROUCH));
				//TODO Set this in VanillaActionController as apparently any entity can crouch?
				break;
			case AnimationMessage.ANIMATION_SWING_ARM:
				session.send(new EntityActionMessage(player.getEntity().getId(), AnimationMessage.ANIMATION_SWING_ARM));
				break;
			case AnimationMessage.ANIMATION_EAT_FOOD:
				session.send(new EntityActionMessage(player.getEntity().getId(), AnimationMessage.ANIMATION_EAT_FOOD));
				break;
			case AnimationMessage.ANIMATION_HURT:
				session.send(new AnimationMessage(player.getEntity().getId(), AnimationMessage.ANIMATION_HURT));
				break;
			case AnimationMessage.ANIMATION_LEAVE_BED:
				session.send(new AnimationMessage(player.getEntity().getId(), AnimationMessage.ANIMATION_LEAVE_BED));
				break;
			case AnimationMessage.ANIMATION_NONE:
				session.send(new AnimationMessage(player.getEntity().getId(), AnimationMessage.ANIMATION_NONE));
				break;
			case AnimationMessage.ANIMATION_UNKNOWN:
				session.send(new AnimationMessage(player.getEntity().getId(), AnimationMessage.ANIMATION_UNKNOWN));
			default:
				break;
		}
	}
}
