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
package org.spout.vanilla.entity.living.player;

import org.spout.api.entity.PlayerController;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.EntityProtocolStore;
import org.spout.vanilla.VanillaPlugin;

public abstract class MinecraftPlayer extends PlayerController {
	private static final EntityProtocolStore entityProtocolStore = new EntityProtocolStore();

	@Override
	public EntityProtocol getEntityProtocol(int protocolId) {
		return entityProtocolStore.getEntityProtocol(protocolId);
	}

	public static void setEntityProtocol(int protocolId, EntityProtocol protocol) {
		entityProtocolStore.setEntityProtocol(protocolId, protocol);
	}

	public MinecraftPlayer(Player p) {
		super(p);
		p.getEntity().setInventorySize(45);
		p.getEntity().getInventory().setCurrentSlot(36);
	}

	@Override
	public void onAttached() {
		Transform spawn = VanillaPlugin.spawnWorld.getSpawnPoint();
		Vector3 rotation = spawn.getRotation().getAxisAngles();
		parent.setPosition(spawn.getPosition(), rotation.getZ(), rotation.getY(), rotation.getX());
		parent.setScale(spawn.getScale());
	}

	@Override
	public void onTick(float dt) {
		// TODO need to send timeout packets
	}
}
