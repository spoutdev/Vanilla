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

import org.spout.api.entity.Entity;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.entity.type.EmptyConstructorControllerType;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.object.Substance;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.CollectItemMessage;

import static org.spout.vanilla.protocol.VanillaNetworkSynchronizer.sendPacketsToNearbyPlayers;

/**
 * Controller that serves as the base for all items that are not in an inventory (dispersed in the world).
 */
public class Item extends Substance {
	public static final ControllerType TYPE = new EmptyConstructorControllerType(Item.class, "Item");
	private final int distance = (int) VanillaConfiguration.ITEM_PICKUP_RANGE.getDouble();
	private final ItemStack is;
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
			is.setNBTData(item.getNBTData());
			is.getAuxData().putAll(item.getAuxData());
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
		Block block = getParent().getRegion().getBlock(getParent().getPosition().subtract(0, 1, 0));
		if (!block.getMaterial().isPlacementObstacle()) {
			Vector3 next = block.getPosition();
			getParent().translate(block.getPosition().multiply(dt));
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
		sendPacketsToNearbyPlayers(entity.getPosition(), entity.getViewDistance(), new CollectItemMessage(collector, collected));
		((VanillaPlayer) entity.getController()).getInventory().getBase().addItem(is, true, true);
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

	@Override
	public void onSave() {
		super.onSave();
		data().put("Itemstack", is);
		data().put("unpickable", unpickable);
	}
}
