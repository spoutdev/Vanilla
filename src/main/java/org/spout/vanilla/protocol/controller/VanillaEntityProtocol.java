/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
package org.spout.vanilla.protocol.controller;

import org.spout.api.entity.Entity;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;

import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.protocol.msg.DestroyEntityMessage;
import org.spout.vanilla.protocol.msg.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.EntityVelocityMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionRotationMessage;

public abstract class VanillaEntityProtocol implements EntityProtocol {
	@Override
	public Message[] getDestroyMessage(Entity entity) {
		return new Message[]{new DestroyEntityMessage(entity.getId())};
	}

	@Override
	public Message[] getUpdateMessage(Entity entity) {
		if (entity.getController() != null && entity.getController() instanceof VanillaActionController) {
			VanillaActionController controller = (VanillaActionController) entity.getController();

			int id = entity.getId();

			//Reducing variable counts would be great...but I see no alternative :(
			int lastX = controller.getClientPosX();
			int lastY = controller.getClientPosY();
			int lastZ = controller.getClientPosZ();
			int lastYaw = controller.getClientYaw();
			int lastPitch = controller.getClientPitch();
			controller.updateClientPosition();

			int newX = controller.getClientPosX();
			int newY = controller.getClientPosY();
			int newZ = controller.getClientPosZ();
			int newYaw = controller.getClientYaw();
			int newPitch = controller.getClientPitch();

			int deltaX = newX - lastX;
			int deltaY = newY - lastY;
			int deltaZ = newZ - lastZ;
			int deltaYaw = newYaw - lastYaw;
			int deltaPitch = newPitch - lastPitch;

			Message posmsg = null;

			if (controller.needsPositionUpdate() || deltaX > 128 || deltaX < -128 || deltaY > 128 || deltaY < -128 || deltaZ > 128 || deltaZ < -128) {
				posmsg = new EntityTeleportMessage(id, newX, newY, newZ, newYaw, newPitch);
			} else {
				boolean moved = deltaX > 4 || deltaX < -4 || deltaY > 4 || deltaY < -4 || deltaZ > 4 || deltaZ < -4;
				boolean looked = deltaYaw > 4 || deltaYaw < -4 || deltaPitch > 4 || deltaPitch < -4;
				if (moved) {
					if (looked) {
						posmsg = new RelativeEntityPositionRotationMessage(id, deltaX, deltaY, deltaZ, controller.getClientYaw(), controller.getClientPitch());
					} else {
						posmsg = new RelativeEntityPositionMessage(id, deltaX, deltaY, deltaZ);
						controller.setClientPosition(newX, newY, newZ, lastYaw, lastPitch);
					}
				} else if (looked) {
					posmsg = new EntityRotationMessage(id, controller.getClientYaw(), controller.getClientPitch());
					controller.setClientPosition(lastX, lastY, lastZ, newYaw, newPitch);
				}
			}

			Message velmsg = null;
			if (controller.needsVelocityUpdate()) {
				velmsg = new EntityVelocityMessage(id, controller.getVelocity());
			}

			if (velmsg == null) {
				if (posmsg == null) {
					return null;
				} else {
					return new Message[]{posmsg};
				}
			} else if (posmsg == null) {
				return new Message[]{velmsg};
			} else {
				return new Message[]{posmsg, velmsg};
			}
		} else {
			return null;
		}
	}
}
