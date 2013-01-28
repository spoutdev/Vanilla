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
package org.spout.vanilla.plugin.protocol.entity.creature;

import java.util.Arrays;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.Parameter;

import org.spout.vanilla.plugin.protocol.entity.BasicEntityProtocol;
import org.spout.vanilla.plugin.protocol.msg.entity.spawn.EntityMobMessage;

public class CreatureProtocol extends BasicEntityProtocol {
	public CreatureProtocol(CreatureType type) {
		super(type.getId());
	}

	@Override
	public List<Message> getSpawnMessages(Entity entity, RepositionManager rm) {
		int entityId = entity.getId();
		Vector3 position = entity.getScene().getPosition().multiply(32).floor();
		int yaw = (int) (entity.getScene().getRotation().getYaw() * 32);
		int pitch = (int) (entity.getScene().getRotation().getPitch() * 32);
		List<Parameter<?>> parameters = this.getSpawnParameters(entity);
		if (parameters.isEmpty()) {
			parameters.add(new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) 1)); //Official expects some metadata to spawn
		}
		//TODO Headyaw
		return Arrays.<Message>asList(new EntityMobMessage(entityId, this.typeId, position, yaw, pitch, 0, (short) 0, (short) 0, (short) 0, parameters, rm));
	}
}
