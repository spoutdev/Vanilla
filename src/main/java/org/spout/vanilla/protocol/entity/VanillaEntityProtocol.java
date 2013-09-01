/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.Parameter;

import org.spout.math.vector.Vector3;
import org.spout.vanilla.component.entity.misc.EntityHead;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.protocol.EntityProtocol;
import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.msg.entity.EntityDestroyMessage;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityRelativePositionMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityRelativePositionYawMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityYawMessage;

import static org.spout.vanilla.protocol.VanillaByteBufUtils.protocolifyPitch;
import static org.spout.vanilla.protocol.VanillaByteBufUtils.protocolifyPosition;
import static org.spout.vanilla.protocol.VanillaByteBufUtils.protocolifyYaw;

public abstract class VanillaEntityProtocol implements EntityProtocol {
	/**
	 * Gets all the Metadata Parameters associated with this Entity when spawning
	 *
	 * @param entity to get it for
	 * @return List of metadata Parameters
	 */
	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		MetadataComponent metadataComponent = entity.get(MetadataComponent.class);
		if (metadataComponent == null) {
			return Collections.emptyList();
		} else {
			return metadataComponent.getSpawnParameters();
		}
	}

	/**
	 * Gets all the Metadata Parameters required for updating this Entity.
	 *
	 * @param entity to get it for
	 * @return List of metadata Parameters to update
	 */
	public List<Parameter<?>> getUpdateParameters(Entity entity) {
		MetadataComponent metadataComponent = entity.get(MetadataComponent.class);
		if (metadataComponent == null) {
			return Collections.emptyList();
		} else {
			return metadataComponent.getUpdateParameters();
		}
	}

	@Override
	public final List<Message> getDestroyMessages(Entity entity) {
		return Arrays.<Message>asList(new EntityDestroyMessage(new int[]{entity.getId()}));
	}

	@Override
	public List<Message> getUpdateMessages(Entity entity, Transform liveTransform, RepositionManager rm, boolean force) {
		// Movement
		final Transform prevTransform = rm.convert(entity.getPhysics().getTransform());
		final Transform newTransform = rm.convert(liveTransform);

		final boolean looked = entity.getPhysics().isRotationDirty();

		final int lastX = protocolifyPosition(prevTransform.getPosition().getX());
		final int lastY = protocolifyPosition(prevTransform.getPosition().getY());
		final int lastZ = protocolifyPosition(prevTransform.getPosition().getZ());
		final Vector3 lastAxesAngles = prevTransform.getRotation().getAxesAngleDeg();
		final int lastYaw = protocolifyYaw(lastAxesAngles.getY());
		final int lastPitch = protocolifyPitch(lastAxesAngles.getX());

		final int newX = protocolifyPosition(newTransform.getPosition().getX());
		final int newY = protocolifyPosition(newTransform.getPosition().getY());
		final int newZ = protocolifyPosition(newTransform.getPosition().getZ());
		final Vector3 newAxesAngles = newTransform.getRotation().getAxesAngleDeg();
		final int newYaw = protocolifyYaw(newAxesAngles.getY());
		final int newPitch = protocolifyPitch(newAxesAngles.getX());

		final int deltaX = newX - lastX;
		final int deltaY = newY - lastY;
		final int deltaZ = newZ - lastZ;
		final int deltaYaw = newYaw - lastYaw;
		final int deltaPitch = newPitch - lastPitch;

		final List<Message> messages = new ArrayList<Message>();

		/*
		 * Two scenarios:
		 * - The entity moves more than 4 blocks and maybe changes rotation.
		 * - The entity moves less than 4 blocks and maybe changes rotation.
		 */
		if (force || deltaX > 128 || deltaX < -128 || deltaY > 128 || deltaY < -128 || deltaZ > 128 || deltaZ < -128) {
			messages.add(new EntityTeleportMessage(entity.getId(), newX, newY, newZ, newYaw, newPitch));
			if (force || looked) {
				messages.add(new EntityYawMessage(entity.getId(), newYaw, newPitch));
			}
		} else if (deltaX != 0 || deltaY != 0 || deltaZ != 0 || deltaYaw != 0 || deltaPitch != 0) {
			if (looked) {
				messages.add(new EntityRelativePositionYawMessage(entity.getId(), deltaX, deltaY, deltaZ, newYaw, newPitch));
			} else if (!prevTransform.getPosition().equals(newTransform.getPosition())) {
				messages.add(new EntityRelativePositionMessage(entity.getId(), deltaX, deltaY, deltaZ));
			}
		}

		// Head movement
		EntityHead head = entity.get(EntityHead.class);
		if (head != null && head.isDirty()) {
			final int headYawProt = VanillaByteBufUtils.protocolifyYaw(head.getOrientation().getAxesAngleDeg().getY());
			messages.add(new EntityHeadYawMessage(entity.getId(), headYawProt));
		}

		// Physics
		//TODO: Actually not used?
		/*if (physics != null && physics.isLinearVelocityDirty()) {
			messages.add(new EntityVelocityMessage(entity.getId(), new Vector3(0, 0, 0)));
		}*/

		// Refresh metadata
		List<Parameter<?>> params = getUpdateParameters(entity);
		if (!params.isEmpty()) {
			messages.add(new EntityMetadataMessage(entity.getId(), params));
		}
		return messages;
	}

	public static Vector3 getProtocolVelocity(Vector3 velocity) {
		final float x = velocity.getX() * 32000;
		final float y = velocity.getY() * 32000;
		final float z = velocity.getZ() * 32000;
		return new Vector3(x, y, z);
	}
}
