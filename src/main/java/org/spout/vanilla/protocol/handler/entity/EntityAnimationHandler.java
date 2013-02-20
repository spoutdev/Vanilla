/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.protocol.handler.entity;

import org.spout.api.component.impl.AnimationComponent;
import org.spout.api.component.impl.ModelHolderComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.model.Model;
import org.spout.api.model.animation.Animation;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;

public final class EntityAnimationHandler extends MessageHandler<EntityAnimationMessage> {
	@Override
	public void handleServer(Session session, EntityAnimationMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();

		switch (message.getAnimation()) {
			case SWING_ARM:
				player.getNetwork().callProtocolEvent(new EntityAnimationEvent(player, org.spout.vanilla.data.Animation.SWING_ARM), true);
				break;
			default:
		}
	}
	
	@Override
	public void handleClient(Session session, EntityAnimationMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();

		Entity entity = player.getWorld().getEntity(message.getEntityId());
		
		ModelHolderComponent models = entity.get(ModelHolderComponent.class);
		if(models == null)
			return;
		
		AnimationComponent animations = entity.get(AnimationComponent.class);
		if(animations == null)
			return;
		
		//This code launch the first animation finded on the first model
		//TODO : play with animation API
		
		switch (message.getAnimation()) {
			case SWING_ARM:
				Model model = models.getModels().get(0);//get first model
				
				if(model == null)
					return;
				
				if(model.getAnimations() == null || model.getAnimations().isEmpty())
					return;
			
				Animation animation = model.getAnimations().values().iterator().next();
				
				animations.playAnimation(model, animation);
				
				break;
			default:
		}
	}
}
