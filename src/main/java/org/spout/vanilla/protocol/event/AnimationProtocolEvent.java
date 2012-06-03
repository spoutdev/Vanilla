/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.protocol.event;

import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.vanilla.protocol.msg.AnimationMessage;

/**
 * @author zml2008
 */
public class AnimationProtocolEvent implements ProtocolEvent {
	public static enum Animation {
		NONE(AnimationMessage.ANIMATION_NONE),
		SWING_ARM(AnimationMessage.ANIMATION_SWING_ARM),
		HURT(AnimationMessage.ANIMATION_HURT),
		LEAVE_BED(AnimationMessage.ANIMATION_LEAVE_BED),
		EAT_FOOD(AnimationMessage.ANIMATION_EAT_FOOD),
		UNKNOWN(AnimationMessage.ANIMATION_UNKNOWN),
		CROUCH(AnimationMessage.ANIMATION_CROUCH),
		UNCROUCH(AnimationMessage.ANIMATION_UNCROUCH),
		;
		private final int id;

		private Animation(int id) {
			this.id = id;
		}

		public byte getId() {
			return (byte) id;
		}
	}

	private final int entityId;
	private final Animation animation;

	public AnimationProtocolEvent(int entityId, Animation animation) {
		this.entityId = entityId;
		this.animation = animation;
	}

	public int getEntityId() {
		return entityId;
	}

	public Animation getAnimation() {
		return animation;
	}
}
