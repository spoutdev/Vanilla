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
package org.spout.vanilla.protocol.controller;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.entity.component.Controller;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;

import org.spout.vanilla.entity.VanillaEntityController;
import org.spout.vanilla.entity.living.Living;
import org.spout.vanilla.protocol.msg.DestroyEntitiesMessage;
import org.spout.vanilla.protocol.msg.entity.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.entity.EntityVelocityMessage;

import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyPosition;
import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyRotation;

public abstract class VanillaEntityProtocol implements EntityProtocol {
	@Override
	public Message[] getDestroyMessage(Entity entity) {
		return new Message[]{new DestroyEntitiesMessage(new int[]{entity.getId()})};
	}

	public Message[] getUpdateMessage(Entity entity) {
		Controller controller = entity.getController();
		if (!(controller instanceof VanillaEntityController)) {
			return new Message[0];
		}
		VanillaEntityController vController = (VanillaEntityController) controller;

		// You have to use the last known CLIENT transform
		// The last tick transform delta is not enough to properly send update messages
		// For most entities, an update takes more than one tick before an update message is ready
		// Do NOT use entity.getLastTransform() because it is not a delta since last tick!
		Transform prevTransform = vController.getLastClientTransform();
		Transform newTransform = entity.getTransform();

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

		if (vController.needsPositionUpdate() || deltaX > 128 || deltaX < -128 || deltaY > 128 || deltaY < -128 || deltaZ > 128 || deltaZ < -128) {
			messages.add(new EntityTeleportMessage(entity.getId(), newX, newY, newZ, newYaw, newPitch));
			vController.setLastClientTransform(newTransform);
		} else {
			boolean moved = !prevTransform.getPosition().equals(newTransform.getPosition());
			boolean looked = !prevTransform.getRotation().equals(newTransform.getRotation());
			if (moved) {
				if (looked) {
					messages.add(new EntityRelativePositionRotationMessage(entity.getId(), deltaX, deltaY, deltaZ, newYaw, newPitch));
					vController.setLastClientTransform(newTransform);
				} else {
					messages.add(new EntityRelativePositionMessage(entity.getId(), deltaX, deltaY, deltaZ));
					vController.getLastClientTransform().setPosition(newTransform.getPosition());
				}
			} else if (looked) {
				messages.add(new EntityRotationMessage(entity.getId(), newYaw, newPitch));
				vController.getLastClientTransform().setRotation(newTransform.getRotation());
			}
		}

		if (controller instanceof VanillaEntityController) {
			VanillaEntityController vaController = (VanillaEntityController) controller;
			if (vaController.needsVelocityUpdate()) {
				messages.add(new EntityVelocityMessage(entity.getId(), vaController.getVelocity()));
			}

			if (controller instanceof Living) {
				Living living = (Living) controller;
				if (living.headYawChanged()) {
					messages.add(new EntityHeadYawMessage(entity.getId(), protocolifyRotation(living.getHeadYaw())));
				}
			}
		}

		return messages.toArray(new Message[messages.size()]);
	}
}
