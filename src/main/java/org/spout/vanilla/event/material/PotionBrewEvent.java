/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.event.material;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.block.BlockEvent;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.component.block.material.BrewingStand;

public class PotionBrewEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private BrewingStand brewingStand;
	private ItemStack ingredient;
	private ItemStack original;
	private ItemStack result;

	public PotionBrewEvent(BrewingStand brewingStand, Cause<?> reason, ItemStack ingredient, ItemStack original, ItemStack result) {
		super(brewingStand.getBlock(), reason);
		this.brewingStand = brewingStand;
		this.ingredient = ingredient;
		this.original = original;
		this.result = result;
	}

	/**
	 * Get the brewing stand in which the potion was brewed
	 *
	 * @return brewingStand
	 */
	public BrewingStand getBrewingStand() {
		return brewingStand;
	}

	/**
	 * Get the ingredient used in the brewing process
	 *
	 * @return ingredient
	 */
	public ItemStack getIngredient() {
		return ingredient;
	}

	/**
	 * Get the original potion
	 *
	 * @return original
	 */
	public ItemStack getOriginal() {
		return original;
	}

	/**
	 * Get the result of the brewing process
	 *
	 * @return result
	 */
	public ItemStack getResult() {
		return result;
	}

	/**
	 * Set the result ItemStack
	 */
	public void setResult(ItemStack result) {
		this.result = result;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
