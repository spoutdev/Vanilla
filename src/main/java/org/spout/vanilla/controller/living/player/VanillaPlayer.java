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
package org.spout.vanilla.controller.living.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.api.entity.PlayerController;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.EntityProtocolStore;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.living.Human;
import org.spout.vanilla.protocol.msg.PingMessage;
import org.spout.vanilla.protocol.msg.UserListItemMessage;

public abstract class VanillaPlayer extends Human implements PlayerController {
	private static final EntityProtocolStore entityProtocolStore = new EntityProtocolStore();
	private final Player owner;
	private int unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInteger();
	private short count = 0;
	private short ping;
	private boolean sneaking, running, onGround;
	private final Vector3 moveSpeed = new Vector3(10, 0, 0);
	private final Vector3 horizSpeed = new Vector3(0, 0, -10);

	public VanillaPlayer(Player p) {
		owner = p;
		p.getEntity().setInventorySize(45);
		Inventory inv = p.getEntity().getInventory();
		for (int i = 37; i <= inv.getSize(); i++) {
			inv.setHiddenSlot(i, true);
		}

		p.getEntity().getInventory().setCurrentSlot(0);
	}

	@Override
	public void onAttached() {
		Transform spawn = getParent().getWorld().getSpawnPoint();
		Quaternion rotation = spawn.getRotation();
		getParent().setPosition(spawn.getPosition());
		getParent().setRotation(rotation);
		getParent().setScale(spawn.getScale());
	}

	@Override
	public void onTick(float dt) {
		Player player = getPlayer();
		if (player == null || player.getSession() == null) {
			return;
		}
		/* COMMENTED OUT PENDING TESTING
		if(player.input().getForward() > 0){
			getParent().translate(moveSpeed.transform(getParent().getRotation()));
		}
		if(player.input().getForward() < 0){
			getParent().translate(moveSpeed.transform(getParent().getRotation()).multiply(-1));
		}
		if(player.input().getHorizantal() < 0){
			getParent().translate(horizSpeed.transform(getParent().getRotation()).multiply(-1));
		}
		if(player.input().getHorizantal() > 0){
			getParent().translate(horizSpeed.transform(getParent().getRotation()));
		}*/

		sendMessage(player, new PingMessage(getRandom().nextInt()));
		count++;
		unresponsiveTicks--;
		if (unresponsiveTicks == 0) {
			player.getSession().disconnect("Connection timeout!");
		}

		sendMessage(player, new UserListItemMessage(player.getName(), true, ping));
	}

	@Override
	public EntityProtocol getEntityProtocol(int protocolId) {
		return entityProtocolStore.getEntityProtocol(protocolId);
	}

	public static void setEntityProtocol(int protocolId, EntityProtocol protocol) {
		entityProtocolStore.setEntityProtocol(protocolId, protocol);
	}

	public short getPing() {
		return ping;
	}

	@Override
	public Player getPlayer() {
		return owner;
	}

	@Override
	public PlayerInventory createInventory(int size) {
		return new PlayerInventory(size);
	}

	public void resetTimeoutTicks() {
		ping = count;
		count = 0;
		unresponsiveTicks = VanillaConfiguration.PLAYER_TIMEOUT_TICKS.getInteger();
	}

	@Override
	public Set<ItemStack> getDeathDrops() {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		ItemStack[] contents = getParent().getInventory().getContents();
		drops.addAll(Arrays.asList(contents));
		return drops;
	}

	public void setSneaking(boolean b) {
		sneaking = b;
	}

	public boolean isSneaking() {
		return sneaking;
	}

	public void setRunning(boolean b) {
		running = b;
	}

	public boolean isRunning() {
		return running;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public boolean isOnGround() {
		return onGround;
	}
}
