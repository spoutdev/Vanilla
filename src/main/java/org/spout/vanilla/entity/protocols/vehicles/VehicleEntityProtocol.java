/*
 * This file is part of Vanilla.
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
package org.spout.vanilla.entity.protocols.vehicles;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.vanilla.entity.protocols.BasicEntityProtocol;
import org.spout.vanilla.entity.vehicle.Boat;
import org.spout.vanilla.entity.vehicle.PoweredMinecart;
import org.spout.vanilla.entity.vehicle.StorageMinecart;
import org.spout.vanilla.entity.vehicle.TransportMinecart;
import org.spout.vanilla.protocol.msg.SpawnVehicleMessage;

public class VehicleEntityProtocol extends BasicEntityProtocol implements EntityProtocol {

	@Override
	public Message getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null) {
			return null;
		}
		int id = entity.getId();
		int x = (int) (entity.getX() * 32);
		int y = (int) (entity.getY() * 32);
		int z = (int) (entity.getZ() * 32);
		int type;
		
	    if (c instanceof Boat) {
	    	type = 1;
	    } else if (c instanceof TransportMinecart) {
			type = 10;
		} else if (c instanceof PoweredMinecart) {
			type = 11;
		} else if (c instanceof StorageMinecart) {
			type = 12;
		} else {
			return null;
		}
	    
	    return new SpawnVehicleMessage(id, type, x, y, z);
	}

}
