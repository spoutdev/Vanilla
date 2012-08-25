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
import java.util.List;

import net.royawesome.jlibnoise.MathHelper;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.entity.component.HeadOwner;
import org.spout.vanilla.protocol.msg.DestroyEntitiesMessage;
import org.spout.vanilla.protocol.msg.entity.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnPlayerMessage;
import org.spout.vanilla.protocol.msg.entity.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.entity.EntityVelocityMessage;

import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyPosition;
import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyRotation;

public class VanillaPlayerProtocol implements EntityProtocol {
	private static final int MC_FULL_AIR = 300;

	@Override
	public Message[] getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null) {
			return null;
		}

		if (c instanceof VanillaPlayerController) {
			int id = entity.getId();
			int x = (int) (entity.getPosition().getX() * 32);
			int y = (int) (entity.getPosition().getY() * 32);
			int z = (int) (entity.getPosition().getZ() * 32);
			int r = (int) (-entity.getYaw() * 32); //cardinal directions differ
			int p = (int) (entity.getPitch() * 32);

			VanillaPlayerController playerController = (VanillaPlayerController) c;
			int item = 0;
			ItemStack hand = playerController.getRenderedItemInHand();
			if (hand != null) {
				item = hand.getMaterial().getId();
			}

			int percentAirLeft = 100;// - (playerController.getSuffocation().getAirTicks() * 100 / playerController.getSuffocation().getMaxAirTicks());
			int airLeft = MathHelper.clamp(percentAirLeft * MC_FULL_AIR, 0, MC_FULL_AIR);

			List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
			parameters.add(new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) airLeft));

			return new Message[]{new EntitySpawnPlayerMessage(id, playerController.getTitle(), x, y, z, r, p, item, parameters)};
		}

		return null;
	}

	@Override
	public Message[] getDestroyMessage(Entity entity) {
		return new Message[]{new DestroyEntitiesMessage(new int[]{entity.getId()})};
	}

	@Override
	public Message[] getUpdateMessage(Entity entity) {
		Controller controller = entity.getController();
		if (!(controller instanceof VanillaPlayerController)) {
			return new Message[0];
		}
		VanillaPlayerController playerController = (VanillaPlayerController) controller;

		// You have to use the last known CLIENT transform
		// The last tick transform delta is not enough to properly send update messages
		// For most entities, an update takes more than one tick before an update message is ready
		// Do NOT use entity.getLastTransform() because it is not a delta since last tick!
		Transform prevTransform = playerController.getLastClientTransform();
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

		if (playerController.needsPositionUpdate() || deltaX > 128 || deltaX < -128 || deltaY > 128 || deltaY < -128 || deltaZ > 128 || deltaZ < -128) {
			messages.add(new EntityTeleportMessage(entity.getId(), newX, newY, newZ, newYaw, newPitch));
			playerController.setLastClientTransform(newTransform);
		} else {
			boolean moved = !prevTransform.getPosition().equals(newTransform.getPosition());
			boolean looked = !prevTransform.getRotation().equals(newTransform.getRotation());
			if (moved) {
				if (looked) {
					messages.add(new EntityRelativePositionRotationMessage(entity.getId(), deltaX, deltaY, deltaZ, newYaw, newPitch));
					playerController.setLastClientTransform(newTransform);
				} else {
					messages.add(new EntityRelativePositionMessage(entity.getId(), deltaX, deltaY, deltaZ));
					playerController.getLastClientTransform().setPosition(newTransform.getPosition());
				}
			} else if (looked) {
				messages.add(new EntityRotationMessage(entity.getId(), newYaw, newPitch));
				playerController.getLastClientTransform().setRotation(newTransform.getRotation());
			}
		}
		if (playerController.needsVelocityUpdate()) {
			messages.add(new EntityVelocityMessage(entity.getId(), playerController.getVelocity()));
		}
		if (controller instanceof HeadOwner) {
			HeadOwner headOwner = (HeadOwner) controller;
			if (headOwner.getHead().yawChanged()) {
				messages.add(new EntityHeadYawMessage(entity.getId(), protocolifyRotation(headOwner.getHead().getYaw())));
			}
		}

		return messages.toArray(new Message[messages.size()]);
	}
}
