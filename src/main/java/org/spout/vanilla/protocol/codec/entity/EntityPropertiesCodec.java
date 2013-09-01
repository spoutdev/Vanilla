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
package org.spout.vanilla.protocol.codec.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.util.ByteBufUtils;

import org.spout.vanilla.protocol.msg.entity.EntityPropertiesMessage;

public class EntityPropertiesCodec extends MessageCodec<EntityPropertiesMessage> {
	public EntityPropertiesCodec() {
		super(EntityPropertiesMessage.class, 0x2C);
	}

	@Override
	public EntityPropertiesMessage decode(ByteBuf buffer) throws IOException {
		int entityID = buffer.readInt();
		int amount = buffer.readInt();
		System.out.println(amount);
		EntityPropertiesMessage msg = new EntityPropertiesMessage(entityID);
		Map<EntityPropertiesCodec, Double> map = new HashMap<EntityPropertiesCodec, Double>();
		for (int i = 1; i <= amount; i++) {
			msg.addProperty(EntityPropertiesMessage.EntityProperties.getByName(ByteBufUtils.readString(buffer)), buffer.readDouble());
		}
		return msg;
	}

	@Override
	public ByteBuf encode(EntityPropertiesMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeInt(message.getEntityId());
		Map<EntityPropertiesMessage.EntityProperties, Double> map = message.getProperties();
		buffer.writeInt(map.size());
		for (Map.Entry<EntityPropertiesMessage.EntityProperties, Double> value : map.entrySet()) {
			ByteBufUtils.writeString(buffer, value.getKey().toString());
			buffer.writeDouble(value.getValue());
		}
		return buffer;
	}
}
