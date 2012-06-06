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

import org.spout.api.entity.component.controller.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;

import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.living.Living;
import org.spout.vanilla.protocol.msg.DestroyEntityMessage;
import org.spout.vanilla.protocol.msg.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.EntityVelocityMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionRotationMessage;

import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyPosition;
import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyRotation;

public abstract class VanillaEntityProtocol implements EntityProtocol {
	@Override
	public Message[] getDestroyMessage(Entity entity) {
		return new Message[]{new DestroyEntityMessage(entity.getId())};
	}

	public Message[] getUpdateMessage(Entity entity) {
		Controller controller = entity.getController();
		Transform previousPosition = entity.getLastTransform();
		Transform newPosition = entity.getTransform();

		int lastX = protocolifyPosition(previousPosition.getPosition().getX());
		int lastY = protocolifyPosition(previousPosition.getPosition().getY());
		int lastZ = protocolifyPosition(previousPosition.getPosition().getZ());

		int newX = protocolifyPosition(newPosition.getPosition().getX());
		int newY = protocolifyPosition(newPosition.getPosition().getY());
		int newZ = protocolifyPosition(newPosition.getPosition().getZ());
		int newYaw = protocolifyRotation(newPosition.getRotation().getYaw());
		int newPitch = protocolifyRotation(newPosition.getRotation().getPitch());

		int deltaX = newX - lastX;
		int deltaY = newY - lastY;
		int deltaZ = newZ - lastZ;

		List<Message> messages = new ArrayList<Message>(3);

		if ((controller instanceof VanillaActionController && ((VanillaActionController) controller).needsPositionUpdate()) || deltaX > 128 || deltaX < -128 || deltaY > 128 || deltaY < -128 || deltaZ > 128 || deltaZ < -128) {
			messages.add(new EntityTeleportMessage(entity.getId(), newX, newY, newZ, newYaw, newPitch));
		} else {
			boolean moved = !previousPosition.getPosition().equals(newPosition.getPosition());
			boolean looked = !previousPosition.getRotation().equals(newPosition.getRotation());
			if (moved) {
				if (looked) {
					messages.add(new RelativeEntityPositionRotationMessage(entity.getId(), deltaX, deltaY, deltaZ, newYaw, newPitch));
				} else {
					messages.add(new RelativeEntityPositionMessage(entity.getId(), deltaX, deltaY, deltaZ));
				}
			} else if (looked) {
				messages.add(new EntityRotationMessage(entity.getId(), newYaw, newPitch));
			}
		}

		if (controller instanceof VanillaActionController) {
			VanillaActionController vaController = (VanillaActionController) controller;
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
