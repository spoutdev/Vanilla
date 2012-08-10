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
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.entity.component.Controller;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.entity.living.Creature;
import org.spout.vanilla.entity.living.Living;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnMobMessage;

public class BasicMobEntityProtocol extends BasicEntityProtocol {
	public BasicMobEntityProtocol(int mobSpawnID) {
		super(mobSpawnID);
	}

	/**
	 * Gets a list of parameters used for the creation of a mob spawn message
	 * @param controller - The entity entity to obtain the parameters from
	 * @return a list of parameters
	 */
	public List<Parameter<?>> getSpawnParameters(Controller controller) {
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>(1);
		if (controller instanceof Creature) {
			Creature creature = (Creature) controller;
			parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, 12, (int) creature.getGrowing().getTimeUntilAdult() * -1));
		}

		return parameters;
	}

	@Override
	public Message[] getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null) {
			return null;
		}

		int id = entity.getId();
		Vector3 pos = entity.getPosition().multiply(32).floor();
		int r = (int) (entity.getYaw() * 32);
		int p = (int) (entity.getPitch() * 32);
		int headyaw = 0;
		if (c instanceof Living) {
			headyaw = ((Living) c).getHeadYaw();
		}
		List<Parameter<?>> parameters = this.getSpawnParameters(c);
		//TODO: Is there a Velocity in a entity class?
		return new Message[]{new EntitySpawnMobMessage(id, this.getSpawnID(), pos, r, p, headyaw, (short) 0, (short) 0, (short) 0, parameters)};
	}

	@Override
	public Message[] getUpdateMessage(Entity entity) {
		List<Parameter<?>> params = this.getSpawnParameters(entity.getController());
		if (params == null || params.size() <= 0) {
			return super.getUpdateMessage(entity);
		}

		Message[] toSend = super.getUpdateMessage(entity);
		List<Message> msgs = new ArrayList<Message>(Arrays.asList(toSend != null ? toSend : new Message[0]));
		msgs.add(new EntityMetadataMessage(entity.getId(), params));
		return msgs.toArray(new Message[msgs.size()]);
	}
}
