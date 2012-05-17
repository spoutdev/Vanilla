/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.controller.object.moving;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.entity.type.EmptyConstructorControllerType;
import org.spout.api.geo.World;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.object.Substance;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.CollectItemMessage;

import static org.spout.vanilla.protocol.VanillaNetworkSynchronizer.sendPacket;

/**
 * Controller that serves as the base for all items that are not in an inventory (dispersed in the world).
 */
public class Item extends Substance {
	public static final ControllerType TYPE = new EmptyConstructorControllerType(Item.class, "Item");
	private final ItemStack is;
	private final int roll;
	private int unpickable;
	
	/**
	 * Creates an item controller. Intended for deserialization only.
	 */
	protected Item() {
		this(new ItemStack(VanillaMaterials.AIR, 1), Vector3.ZERO);
	}

	/**
	 * Creates an item controller
	 * @param itemstack this item controller represents
	 * @param initial velocity that this item has
	 */
	public Item(ItemStack itemstack, Vector3 initial) {
		super(VanillaControllerTypes.DROPPED_ITEM);
		this.is = itemstack;
		this.roll = 1;
		unpickable = 10;
		setVelocity(initial);
	}
	
	@Override
	public void onAttached() {
		super.onAttached();
		if (data().containsKey("Itemstack")) {
			ItemStack item = (ItemStack) data().get("Itemstack");
			is.setMaterial(item.getMaterial(), item.getData());
			is.setAmount(item.getAmount());
			is.setAuxData(item.getAuxData());
		}

		unpickable = (Integer) data().get("unpickable", unpickable);
	}

	@Override
	public void onTick(float dt) {
		if (unpickable > 0) {
			unpickable--;
			super.onTick(dt);
			return;
		}

		super.onTick(dt);

		//move();

		World world = getParent().getWorld();
		if (world == null) {
			return;
		}

		List<Player> players = Arrays.asList(Spout.getEngine().getOnlinePlayers());
		Iterable<Player> onlinePlayers = Iterables.filter(players, isValidPlayer);

		double minDistance = -1;
		Entity closestPlayer = null;
		for (Player plr : onlinePlayers) {
			Entity entity = plr.getEntity();
			if (entity.getWorld().equals(world)) {
				double distance = entity.getPosition().getSquaredDistance(getParent().getPosition());
				if (distance < minDistance || minDistance == -1) {
					closestPlayer = entity;
					minDistance = distance;
				}
			}
		}

		double maxDistance = VanillaConfiguration.ITEM_PICKUP_RANGE.getDouble();
		if (closestPlayer != null && minDistance <= maxDistance * maxDistance) {
			int collected = getParent().getId();
			int collector = closestPlayer.getId();
			for (Player plr : onlinePlayers) {
				if (plr.getEntity().getWorld().equals(world)) {
					sendPacket(plr, new CollectItemMessage(collected, collector));
				}
			}

			closestPlayer.getInventory().addItem(is, false);
			getParent().kill();
		}
	}

	private final Predicate<Player> isValidPlayer = new Predicate<Player>() {
		@Override
		public boolean apply(Player p) {
			return p != null && p.getEntity() != null && p.getEntity().getWorld() != null;
		}
	};

	/**
	 * Gets what block the item is.
	 * @return block of item.
	 */
	public Material getMaterial() {
		return is.getMaterial();
	}

	/**
	 * Gets the amount of the block there is in the ItemStack.
	 * @return amount of items
	 */
	public int getAmount() {
		return is.getAmount();
	}

	/**
	 * Gets the data of the item
	 * @return item data
	 */
	public short getData() {
		return is.getData();
	}

	/**
	 * Gets the roll of the item
	 * @return roll of item.
	 */
	public int getRoll() {
		return roll;
	}
	
	@Override
	public void onSave() {
		data().put("Itemstack", is);
		data().put("unpickable", unpickable);
		super.onSave();
	}
}
