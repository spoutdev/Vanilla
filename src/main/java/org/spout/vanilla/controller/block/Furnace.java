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
package org.spout.vanilla.controller.block;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.controller.TransactionWindowOwner;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.window.Window;
import org.spout.vanilla.window.block.FurnaceWindow;

public class Furnace extends VanillaWindowBlockController implements TransactionWindowOwner {
	private final FurnaceInventory inventory;
	private float burnTimeRemaining = 0, burnTimeTotal = 0;
	private float craftTimeRemaining = 0, craftTimeTotal = 0;
	//private float progress = 0, progressIncrement = 0, craftTime = 0;
	private boolean isBurningState = false;

	public Furnace() {
		super(VanillaControllerTypes.FURNACE, VanillaMaterials.FURNACE);
		inventory = new FurnaceInventory(this);
	}

	@Override
	public void onAttached() {
		this.isBurningState = getBlock().getMaterial() == VanillaMaterials.FURNACE_BURNING;
	}

	@Override
	public void onTick(float dt) {
		// the tick time is not accurate enough - results in 7/8 crafted when using coal as fuel
		// another solution is to give fuel/craft time bonuses to fix the accuracy
		// but for now, this will do just fine.
		dt = 0.05f;

		ItemStack input = inventory.getIngredient().getItem(), output = inventory.getOutput().getItem();

		if (craftTimeRemaining > 0f) {
			if (inventory.hasIngredient()) {
				// Decrement crafting time
				craftTimeRemaining -= dt;
				if (craftTimeRemaining <= 0f) {
					// finished crafting the item
					ItemStack result = ((TimedCraftable) input.getMaterial()).getResult();
					if (output == null) {
						output = result;
					} else {
						output.setAmount(output.getAmount() + result.getAmount());
					}

					inventory.getIngredient().addItemAmount(-1);
					inventory.getOutput().setItem(output);
					craftTimeRemaining = craftTimeTotal = 0.0f;
				}
			} else {
				craftTimeRemaining = craftTimeTotal = 0.0f;
			}
		}

		// Take new fuel if needed
		if (burnTimeRemaining <= 0) {
			if (inventory.hasFuel() && inventory.hasIngredient()) {
				// Start burning
				ItemStack fuelStack = inventory.getFuel().getItem();
				Fuel fuel = (Fuel) fuelStack.getMaterial();
				burnTimeRemaining = burnTimeTotal = fuel.getFuelTime();

				TimedCraftable ingredient = (TimedCraftable) input.getMaterial();
				craftTimeRemaining = craftTimeTotal = ingredient.getCraftTime();

				inventory.getFuel().addItemAmount(-1);
			} else {
				burnTimeRemaining = burnTimeTotal = 0.0f;
				craftTimeRemaining = craftTimeTotal = 0.0f;
			}
		} else if (burnTimeRemaining > 0) {
			// Decrement the burn
			burnTimeRemaining -= dt;
		}
	
		// Take new ingredient if needed
		if (craftTimeRemaining <= 0 && inventory.hasIngredient()) {
			TimedCraftable ingredient = (TimedCraftable) input.getMaterial();
			craftTimeRemaining = craftTimeTotal = ingredient.getCraftTime();
		}

		// Update burning state
		if (this.isBurning() != this.isBurningState) {
			this.isBurningState = this.isBurning();
			VanillaMaterials.FURNACE.setBurning(this.getBlock(), this.isBurningState);
		}

		// Update viewers
		for (VanillaPlayer player : this.getViewers()) {
			FurnaceWindow window = (FurnaceWindow) player.getActiveWindow();
			window.updateBurnTime((int) (250f * burnTimeRemaining / burnTimeTotal));
			window.updateProgress((int) (180f - 180f * craftTimeRemaining / craftTimeTotal));
		}
	}

	@Override
	public FurnaceInventory getInventory() {
		return inventory;
	}

	/**
	 * Sets the remaining burn time for the current Furnace recipe to complete
	 * @param burnTime in seconds
	 */
	public void setBurnTime(float burnTime) {
		this.burnTimeRemaining = burnTime;
	}

	/**
	 * Gets the remaining burn time for the current Furnace recipe to complete
	 * @return burnTime in seconds
	 */
	public float getBurnTime() {
		return burnTimeRemaining;
	}

	/**
	 * Sets the progress of burning
	 * @param progress in seconds to set to
	 */
	public void setProgress(float progress) {
		this.craftTimeRemaining = this.craftTimeTotal - progress;
	}

	/**
	 * Gets the progress of burning
	 * @return progress in seconds
	 */
	public float getProgress() {
		return this.craftTimeTotal - this.craftTimeRemaining;
	}

	/**
	 * Gets whether this Furnace is burning
	 * @return True if it is burning, False if not
	 */
	public boolean isBurning() {
		return burnTimeRemaining > 0;
	}

	@Override
	public Window createWindow(VanillaPlayer player) {
		return new FurnaceWindow(player, this);
	}
}
