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
package org.spout.vanilla.protocol.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;

import org.spout.vanilla.protocol.msg.DestroyEntitiesMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityTeleportMessage;

import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyPosition;
import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyRotation;

public abstract class VanillaEntityProtocol implements EntityProtocol {
	@Override
	public List<Message> getDestroyMessages(Entity entity) {
		return Arrays.<Message>asList(new DestroyEntitiesMessage(new int[]{entity.getId()}));
	}

	@Override
	public List<Message> getUpdateMessages(Entity entity) {
		if (!entity.getTransform().isDirty()) {
			return Collections.emptyList();
		}
		// You have to use the last known CLIENT transform
		// The last tick transform delta is not enough to properly send update messages
		// For most entities, an update takes more than one tick before an update message is ready
		// Do NOT use entity.getLastTransform() because it is not a delta since last tick!
		Transform prevTransform = entity.getTransform().getTransform();
		Transform newTransform = entity.getTransform().getTransformLive();

		int lastX = protocolifyPosition(prevTransform.getPosition().getX());
		int lastY = protocolifyPosition(prevTransform.getPosition().getY());
		int lastZ = protocolifyPosition(prevTransform.getPosition().getZ());

		int newX = protocolifyPosition(newTransform.getPosition().getX());
		int newY = protocolifyPosition(newTransform.getPosition().getY());
		int newZ = protocolifyPosition(newTransform.getPosition().getZ());
		int newYaw = protocolifyRotation(newTransform.getRotation().getYaw());
		int newPitch = protocolifyRotation(newTransform.getRotation().getPitch());

		int deltaX = newX - lastX;
		int deltaY = newY - lastY;
		int deltaZ = newZ - lastZ;

		List<Message> messages = new ArrayList<Message>(3);
		boolean looked = !prevTransform.getRotation().equals(newTransform.getRotation());
		if (deltaX > 4 || deltaX < -4 || deltaY > 4 || deltaY < -4 || deltaZ > 4 || deltaZ < -4) {
			messages.add(new EntityTeleportMessage(entity.getId(), newX, newY, newZ, newYaw, newPitch));
		} else {
			if (looked) {
				messages.add(new EntityRelativePositionRotationMessage(entity.getId(), deltaX, deltaY, deltaZ, newYaw, newPitch));
			} else {
				messages.add(new EntityRelativePositionMessage(entity.getId(), deltaX, deltaY, deltaZ));
			}
		}
		if (looked) {
			messages.add(new EntityRotationMessage(entity.getId(), newYaw, newPitch));
		}
		//TODO Velocity component

		//TODO HeadYaw component
		return messages;
	}
}
