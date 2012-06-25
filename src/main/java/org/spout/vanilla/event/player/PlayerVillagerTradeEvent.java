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

import org.spout.vanilla.controller.living.creature.passive.Villager;

/**
 * The Event called when a player trades with a villager. Not yet implemented.
 */
public class PlayerVillagerTradeEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final Player player;
	private final Villager villager;
	private final ItemStack offer, result;

	public PlayerVillagerTradeEvent(Player player, Villager villager, ItemStack offer, ItemStack result) {
		super(player);
		this.player = player;
		this.villager = villager;
		this.offer = offer;
		this.result = result;
	}

	/**
	 * Gets the player doing the trading.
	 *
	 * @return The Player doing the trading.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the villager doing the trading.
	 *
	 * @return The Villager doing the trading.
	 */
	public Villager getVillager() {
		return villager;
	}

	/**
	 * Gets the item offered in the trade.
	 *
	 * @return The ItemStack representing the offer in the trade.
	 */
	public ItemStack getOffer() {
		return offer;
	}

	/**
	 * Gets the item result of the trade.
	 *
	 * @return The ItemStack representing the result of the trade.
	 */
	public ItemStack getResult() {
		return result;
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
