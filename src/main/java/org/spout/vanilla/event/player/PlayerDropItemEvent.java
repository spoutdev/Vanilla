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
package org.spout.vanilla.event.player;

import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.player.Player;

import org.spout.vanilla.controller.object.moving.Item;

/**
 * Event called when a player drops/throws an item.
 */
public class PlayerDropItemEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final Player player;
	private ItemStack itemInHand;
	private Item spawnItem;

	public PlayerDropItemEvent(Player player, ItemStack itemInHand, Item itemToBeSpawned) {
		super(player);
		this.player = player;
		this.itemInHand = itemInHand;
		this.spawnItem = itemToBeSpawned;
	}

	/**
	 * Gets the player dropping the item.
	 *
	 * @return The Player dropping the item.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the ItemStack the player is dropping from.
	 *
	 * @return The ItemStack the player is dropping from.
	 */
	public ItemStack getItemInHand() {
		return itemInHand;
	}

	/**
	 * Sets the ItemStack the player should drop from.
	 *
	 * @param itemInHand The ItemStack the player should drop from.
	 */
	public void getItemInHand(ItemStack itemInHand) {
		this.itemInHand = itemInHand;
	}

	/**
	 * Gets the Item controller object to be spawned in the world.
	 *
	 * @return The Item controller object to be spawned in the world.
	 */
	public Item getItemToBeSpawned() {
		return spawnItem;
	}

	/**
	 * Sets the Item controller object to be spawned in the world.
	 *
	 * @param itemToBeSpawned The Item controller object to be spawned in the world.
	 */
	public void getItemToBeSpawned(Item itemToBeSpawned) {
		this.spawnItem = itemToBeSpawned;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}