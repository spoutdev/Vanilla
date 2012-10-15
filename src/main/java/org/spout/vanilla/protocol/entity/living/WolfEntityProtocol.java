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
package org.spout.vanilla.protocol.entity.living;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.living.neutral.Wolf;
import org.spout.vanilla.component.misc.HealthComponent;
import org.spout.vanilla.protocol.entity.CreatureProtocol;

public class WolfEntityProtocol extends CreatureProtocol {
	public WolfEntityProtocol() {
		super(95);
	}

	@Override
	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		List<Parameter<?>> parameters = super.getSpawnParameters(entity);
		Wolf wolf = entity.add(Wolf.class);
		byte data = 0;
		data |= (wolf.isSitting() ? 1 : 0) & 0x01;
		data |= (wolf.haveRedEyes() ? 1 : 0) & 0x02;
		data |= (wolf.isTamed() ? 1 : 0) & 0x04;
		parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, data));
		parameters.add(new Parameter<String>(Parameter.TYPE_STRING, 17, wolf.getOwnerName()));
		parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, 18, wolf.getOwner().get(HealthComponent.class).getHealth()));
		return parameters;
	}
}
