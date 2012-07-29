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
package org.spout.vanilla.controller.object.moving;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.data.Data;
import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.object.Substance;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.entity.EntityCollectItemMessage;

import static org.spout.vanilla.util.VanillaNetworkUtil.sendPacketsToNearbyPlayers;

/**
 * Controller that serves as the base for all items that are not in an inventory (dispersed in the world).
 */
public class Item extends Substance {
	private final int distance = VanillaConfiguration.ITEM_PICKUP_RANGE.getInt();
	private ItemStack is;
	private int uncollectableTicks;

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
		this.uncollectableTicks = 30;
		setVelocity(initial);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getParent().getCollision().setStrategy(CollisionStrategy.SOFT);
		if (data().containsKey(Data.HELD_ITEM)) {
			is = data().get(Data.HELD_ITEM).clone();
		}

		uncollectableTicks = data().get(VanillaData.UNCOLLECTABLE_TICKS, uncollectableTicks);
	}

	@Override
	public void onSave() {
		super.onSave();
		data().put(Data.HELD_ITEM, is);
		data().put(VanillaData.UNCOLLECTABLE_TICKS, uncollectableTicks);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		// gravity
		this.setVelocity(this.getVelocity().subtract(0, 0.04, 0));
		this.move();
		// slow-down
		this.setVelocity(this.getVelocity().multiply(0.98));

		//TODO: proper entity on ground function
		Block below = getParent().getWorld().getBlock(getParent().getPosition().subtract(0.0, 0.2, 0.0), getParent());
		if (below.getMaterial().isSolid()) {
			this.setVelocity(this.getVelocity().multiply(0.7, 0.0, 0.7).add(0.0, 0.06, 0.0));
		}

		//TODO: Block friction / burn damage / etc.

		if (uncollectableTicks > 0) {
			uncollectableTicks--;
			return;
		}

		Player closestPlayer = getParent().getWorld().getNearestPlayer(getParent(), distance);
		if (closestPlayer == null) {
			return;
		}

		Entity entity = closestPlayer.getEntity();
		if (!(entity.getController() instanceof VanillaPlayer)) {
			return;
		}

		int collected = getParent().getId();
		int collector = entity.getId();
		sendPacketsToNearbyPlayers(entity.getPosition(), entity.getViewDistance(), new EntityCollectItemMessage(collected, collector));
		((VanillaPlayer) entity.getController()).getInventory().getMain().addItem(is, true, true);
		getParent().kill();
	}

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
}
