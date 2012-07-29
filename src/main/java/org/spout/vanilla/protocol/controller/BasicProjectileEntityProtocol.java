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

import org.spout.api.entity.Entity;
import org.spout.api.entity.component.Controller;
import org.spout.api.protocol.Message;

import org.spout.vanilla.controller.object.Projectile;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnVehicleMessage;

public class BasicProjectileEntityProtocol extends BasicEntityProtocol {
	public BasicProjectileEntityProtocol(int projectileSpawnID) {
		super(projectileSpawnID);
	}

	@Override
	public Message[] getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null || !(c instanceof Projectile)) {
			return null;
		}

		Projectile pro = (Projectile) c;

		int id = entity.getId();

		Entity shooter = pro.getShooter();
		int shooterid = shooter == null ? 0 : shooter.getId();

		return new Message[]{new EntitySpawnVehicleMessage(id, this.getSpawnID(), entity.getPosition(), shooterid, pro.getVelocity())};
	}
}
