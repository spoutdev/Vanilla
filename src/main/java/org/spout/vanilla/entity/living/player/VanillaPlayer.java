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
package org.spout.vanilla.entity.living.player;

import java.util.Random;

import org.spout.api.entity.PlayerController;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.EntityProtocolStore;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.entity.living.HumanEntity;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.msg.PingMessage;
import org.spout.vanilla.protocol.msg.UserListItemMessage;

public abstract class VanillaPlayer extends HumanEntity implements PlayerController {
	
	private static final EntityProtocolStore entityProtocolStore = new EntityProtocolStore();
	private static Random random = new Random();
	private final Player owner;
	private int unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInteger();
	private short count = 0;
	private short ping;

	@Override
	public EntityProtocol getEntityProtocol(int protocolId) {
		return entityProtocolStore.getEntityProtocol(protocolId);
	}

	public static void setEntityProtocol(int protocolId, EntityProtocol protocol) {
		entityProtocolStore.setEntityProtocol(protocolId, protocol);
	}

	public VanillaPlayer(Player p) {
		owner = p;
		p.getEntity().setInventorySize(45);
		Inventory inv = p.getEntity().getInventory();
		for (int i = 37; i <= inv.getSize(); i++) {
			inv.setHiddenSlot(i, true);
		}
		
		p.getEntity().getInventory().setCurrentSlot(0);

	}
	
	public short getPing() {
		return ping;
	}
	
	@Override
	public Player getPlayer() {
		return owner;
	}
	
	@Override
	public Inventory createInventory(int size) {
		return new PlayerInventory(size);
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
		Player player = getPlayer();
		if (player == null || player.getSession() == null) {
			return;
		}
		
		PingMessage p = new PingMessage(random.nextInt());
		player.getSession().send(p);
		count++;
		unresponsiveTicks--;
		if (unresponsiveTicks == 0) {
			player.getSession().disconnect("Connection timeout!");
		}
		
		player.getSession().send(new UserListItemMessage(player.getName(), true, ping));
	}

	public void resetTimeoutTicks() {
		ping = count;
		count = 0;
		unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInteger();
	}
}
