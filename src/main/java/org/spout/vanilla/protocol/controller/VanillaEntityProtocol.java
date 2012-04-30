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

import java.util.HashSet;
import java.util.Set;

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
	private static final int MOVE = 1;
	private static final int LOOK = 2;

	@Override
	public Message[] getDestroyMessage(Entity entity) {
		return new Message[]{new DestroyEntityMessage(entity.getId())};
	}

	@Override
	public Message[] getUpdateMessage(Entity entity) {
		Set<Message> messages = new HashSet<Message>();

		if (entity.getController() != null && (entity.getController() instanceof VanillaActionController)) {
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

			if (controller.needsPositionUpdate() || hasMagnitudeOver(128, deltaX, deltaY, deltaZ)) {
				messages.add(new EntityTeleportMessage(id, newX, newY, newZ, newYaw, newPitch));
			} else {
				int pid = hasMagnitudeOver(4, deltaX, deltaY, deltaZ) ? MOVE:0;
				pid += hasMagnitudeOver(4, deltaYaw, deltaPitch) ? LOOK:0;

				switch (pid) {
					case MOVE:
						messages.add(new RelativeEntityPositionMessage(id, deltaX, deltaY, deltaZ));
						controller.setClientPosition(newX, newY, newZ, lastYaw, lastPitch);
						break;
					case LOOK:
						messages.add(new EntityRotationMessage(id, newYaw, newPitch));
						controller.setClientPosition(lastX, lastY, lastZ, newYaw, newPitch);
						break;
					case MOVE+LOOK:
						messages.add(new RelativeEntityPositionRotationMessage(id, deltaX, deltaY, deltaZ, newYaw, newPitch));
						break;
				}
			}

			if (controller.needsVelocityUpdate()) {
				messages.add(new EntityVelocityMessage(id, controller.getVelocity()));
			}
		}
		return messages.toArray(new Message[messages.size()]);
	}

	private boolean hasMagnitudeOver(int threshold, int... testVars) {
		for (int t : testVars) {
			if (Math.abs(t) > threshold) {
				return true;
			}
		}
		return false;
	}
}
