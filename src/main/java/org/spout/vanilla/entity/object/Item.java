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
package org.spout.vanilla.entity.object;

import java.util.Random;
import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.EntityProtocolStore;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.entity.Entity;
import org.spout.vanilla.entity.ObjectEntity;
import org.spout.vanilla.protocol.msg.CollectItemMessage;

public class Item extends ObjectEntity {
	
	private static final EntityProtocolStore entityProtocolStore = new EntityProtocolStore(); //TODO this is an annoying fix, someone with knowlege in entities get rid of this?
	private ItemStack is;
	private int roll, unpickable;
	private final Random rand = new Random();	
	
	@Override
	public EntityProtocol getEntityProtocol(int protocolId) {
		return entityProtocolStore.getEntityProtocol(protocolId);
	}

	public static void setEntityProtocol(int protocolId, EntityProtocol protocol) {
		entityProtocolStore.setEntityProtocol(protocolId, protocol);
	}

	public Item(ItemStack is, Vector3 initial) {
		this.is = is;
		this.roll = 1;
		unpickable = 10;
		velocity.add(initial);
	}

	@Override
	public void onAttached() {
		parent.setData(Entity.KEY, Entity.DroppedItem.id);
	}

	@Override
	public void onTick(float dt) {
		if (parent.isDead()) {
			return;
		}
		
		if (unpickable > 0) {
			unpickable--;
			super.onTick(dt);
			return;
		}
		
		float x = (rand.nextBoolean() ? 1 : -1) * rand.nextFloat();
		float y = rand.nextFloat();
		float z = (rand.nextBoolean() ? 1 : -1) * rand.nextFloat();
		this.velocity.add(x, y, z);
		super.onTick(dt);
		World world = parent.getWorld();
		//TODO replace with getClosestPlayer when my Spout PR gets pulled!
		Set<Player> players = world.getPlayers();
		double minDistance = -1;
		Player closestPlayer = null;
		for (Player plr : players) {
			double distance = plr.getEntity().getPosition().distance(parent.getPosition());
			if (distance < minDistance || minDistance == -1) {
				closestPlayer = plr;
				minDistance = distance;
			}
		}
		
		if (closestPlayer == null) {
			return;
		}
		
		if (minDistance > VanillaConfiguration.ITEM_PICKUP_RANGE.getDouble()) {
			return;
		}
		
		int collected = parent.getId();
		int collector = closestPlayer.getEntity().getId();
		CollectItemMessage message = new CollectItemMessage(collected, collector);
		for (Player plr : players) {
			plr.getSession().send(message);
		}
		
		closestPlayer.getEntity().getInventory().addItem(is);
		parent.kill();
	}

	public Material getMaterial() {
		return is.getMaterial();
	}

	public int getAmount() {
		return is.getAmount();
	}

	public short getDamage() {
		return is.getDamage();
	}

	public int getRoll() {
		return roll;
	}
}
