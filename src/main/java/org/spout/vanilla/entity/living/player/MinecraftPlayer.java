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

import java.util.Random;
import org.spout.api.entity.PlayerController;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.inventory.Inventory;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.EntityProtocolStore;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.protocol.msg.PingMessage;
import org.spout.vanilla.protocol.msg.SetWindowSlotsMessage;
import org.spout.vanilla.util.configuration.VanillaConfiguration;

public abstract class MinecraftPlayer extends PlayerController {

	private static final EntityProtocolStore entityProtocolStore = new EntityProtocolStore();
	private static Random random = new Random();
	private int unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInteger();

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
		Inventory inv = p.getEntity().getInventory();
		for(int i=0;i<=8;i++) 
			inv.setHiddenSlot(i, true);
	}

	@Override
	public void onAttached() {
		Transform spawn = VanillaPlugin.spawnWorld.getSpawnPoint();
		Vector3 rotation = spawn.getRotation().getAxisAngles();
		parent.setPosition(spawn.getPosition(), rotation.getZ(), rotation.getY(), rotation.getX());
		parent.setScale(spawn.getScale());
		parent.setObserver(true);
	}

	@Override
	public void onTick(float dt) {
		Player player = getPlayer();
		PingMessage p = new PingMessage(random.nextInt());
		player.getSession().send(p);
		unresponsiveTicks--;
		if(unresponsiveTicks==0) {
			player.getSession().disconnect("Connection timeout!");
		}
		if (player.getEntity().getInventory().isDirty()) {
			player.getEntity().getInventory().setDirty(false);
			SetWindowSlotsMessage message = new SetWindowSlotsMessage((byte)0, player.getEntity().getInventory().getContents());
			player.getSession().send(message);
		} 
	}
	
	public void resetTimeoutTicks() {
		unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInteger();
	}
}
