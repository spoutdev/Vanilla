/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity.protocols.living;

import java.util.Collections;
import java.util.List;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;
import org.spout.vanilla.entity.protocols.BasicEntityProtocol;
import org.spout.vanilla.protocol.msg.SpawnMobMessage;

public class BasicMobEntityProtocol extends BasicEntityProtocol implements EntityProtocol {
	
	/**
	 * Gets a list of parameters used for the creation of a mob spawn message
	 * @param controller - The entity controller to obtain the parameters from
	 * @return a list of parameters
	 */
	@SuppressWarnings("unchecked")
	public List<Parameter<?>> getSpawnParameters(Controller controller) {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public Message getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c != null) {
			int id = entity.getId();
			int x = (int) (entity.getX() * 32);
			int y = (int) (entity.getY() * 32);
			int z = (int) (entity.getZ() * 32);
			int r = (int) (entity.getYaw() * 32);
			int p = (int) (entity.getPitch() * 32);
			int headyaw = 0; //TODO: obtain headyaw from entity
			int type = entity.getData(org.spout.vanilla.entity.Entity.KEY).asInt();
			List<Parameter<?>> parameters = this.getSpawnParameters(c);
			return new SpawnMobMessage(id, type, x, y, z, r, p, headyaw, parameters);
		} else {
			return null;
		}
	}

}
