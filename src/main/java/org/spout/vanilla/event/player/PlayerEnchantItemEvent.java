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
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.player.Player;

public class PlayerEnchantItemEvent extends PlayerEvent implements Cancellable {
	
	private static HandlerList handlers = new HandlerList();
	private Enchantment enchantment = null;
	private ItemStack item = null;
	private Block table = null;
	private int expLevelCost;

	public PlayerEnchantItemEvent(Player p, ItemStack item, Enchantment enchantment, Block table, int expLevelCost) {
		super(p);
		this.enchantment = enchantment;
		this.item = item;
		this.table = table;
		this.expLevelCost = expLevelCost;
	}
	
	/**
	 * Gets the enchantment of the item.
	 * @return Enchantment (see enum below)
	 */
	public Enchantment getEnchantment() {
		return enchantment;
	}
	
	/**
	 * Gets the item that was enchanted.
	 * @return ItemStack (item that was enchanted)
	 */
	public ItemStack getItem() {
		return item;
	}
	
	/**
	 * Gets the Block that the player used to enchant
	 * @return Block (the enchantment table)
	 */
	public Block getEnchantmentTable() {
		return table;
	}
	
	/**
	 * Gets the amount of levels required to enchant the item
	 * @return int (amount of levels)
	 */
	public int getExpLevelCost() {
		return expLevelCost;
	}
	
	/**
	 * Sets the enchantment type of the item
	 * @param Enchantment (from enum below)
	 */
	public void setEnchantment(Enchantment enchantment) {
		this.enchantment = enchantment;
	}
	
	/**
	 * Sets the item that was enchanted.
	 * @param ItemStack (item to be enchanted)
	 */
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	/**
	 * Sets the amount of levels required to enchant
	 * @param int (amount of levels)
	 */
	public void setExpLevelCost(int expLevelCost) {
		this.expLevelCost = expLevelCost;
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
	
	public enum Enchantment {
		/**
		 * This is the list of all possible enchantments.
		 */
		PROTECTION,
		FIRE_PROTECTION,
		FEATHER_FALLING,
		BLAST_PROTECTION,
		PROJECTILE_PROTECTION,
		RESPIRATION,
		AQUA_AFFINITY,
		SHARPNESS,
		SMITE,
		BANE_OF_ARTHROPODS,
		KNOCKBACK,
		FIRE_ASPECT,
		LOOTING,
		EFFICIENCY,
		SILK_TOUCH,
		UNBREAKING,
		FORTUNE,
		POWER,
		PUNCH,
		FLAME,
		INFINITY;		
	}
}

