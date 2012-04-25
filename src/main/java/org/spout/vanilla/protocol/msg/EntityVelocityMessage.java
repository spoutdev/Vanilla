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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.msg;

import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;

public final class EntityVelocityMessage extends Message {
	private final int id, velocityX, velocityY, velocityZ;

	public EntityVelocityMessage(int id, Vector3 velocity) {
		this(id, (int) velocity.getX(), (int) velocity.getY(), (int) velocity.getZ());
	}

	public EntityVelocityMessage(int id, int velocityX, int velocityY, int velocityZ) {
		/*try {
			throw new Exception("Created EntityVelocityMessage");
		} catch (Exception ex) {
			Logger.getLogger(EntityVelocityMessage.class.getName()).log(Level.SEVERE, null, ex);
		}
		* This is for testing the random teleport behaviour of some mobs, uncomment if it happens!
		*/
		this.id = id;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
	}

	public int getId() {
		return id;
	}

	public int getVelocityX() {
		return velocityX;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public int getVelocityZ() {
		return velocityZ;
	}

	@Override
	public String toString() {
		return "EntityVelocityMessage{id=" + id + ",velocityX=" + velocityX + ",velocityY=" + velocityY + ",velocityZ=" + velocityZ + "}";
	}
}
