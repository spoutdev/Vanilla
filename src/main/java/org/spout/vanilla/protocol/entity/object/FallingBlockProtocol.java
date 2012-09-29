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
package org.spout.vanilla.protocol.entity.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.substance.MovingBlock;
import org.spout.vanilla.protocol.entity.BasicVehicleEntityProtocol;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityVehicleMessage;

public class FallingBlockProtocol extends BasicVehicleEntityProtocol {
	public FallingBlockProtocol() {
		super(21);
	}

	@Override
	public List<Message> getSpawnMessages(Entity entity) {
		MovingBlock moving = entity.add(MovingBlock.class);
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
		//70 means FallingBlock ID type
		return Arrays.<Message>asList(new EntityVehicleMessage(entity.getId(), 70, entity.getTransform().getPosition(), (short) moving.getMaterial().getMinecraftId(), new Vector3(0.3, 0.01, 0)), new EntityMetadataMessage(entity.getId(), parameters));
	}
}
