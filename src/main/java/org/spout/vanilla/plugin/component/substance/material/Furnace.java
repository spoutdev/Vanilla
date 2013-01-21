/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.plugin.component.substance.material;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.api.inventory.Container;
import org.spout.vanilla.api.material.Fuel;
import org.spout.vanilla.api.material.TimedCraftable;

import org.spout.vanilla.plugin.component.inventory.WindowHolder;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.event.inventory.FurnaceCloseEvent;
import org.spout.vanilla.plugin.event.inventory.FurnaceOpenEvent;
import org.spout.vanilla.plugin.inventory.block.FurnaceInventory;
import org.spout.vanilla.plugin.inventory.window.block.FurnaceWindow;
import org.spout.vanilla.plugin.inventory.window.prop.FurnaceProperty;
import org.spout.vanilla.plugin.material.VanillaMaterials;

public class Furnace extends ViewedBlockComponent implements Container {
	public final float MAX_FUEL_INCREMENT = 12.5f;
	public final float MAX_SMELT_TIME_INCREMENT = 9f;

	public float getMaxSmeltTime() {
		return getData().get(VanillaData.MAX_SMELT_TIME);
	}

	public void setMaxSmeltTime(float maxSmeltTime) {
		getData().put(VanillaData.MAX_SMELT_TIME, maxSmeltTime);
	}

	public float getSmeltTime() {
		return getData().get(VanillaData.SMELT_TIME);
	}

	public void setSmeltTime(float smeltTime) {
		getData().put(VanillaData.SMELT_TIME, smeltTime);
		for (Player player : viewers) {
			updateProgressArrow(player);
		}
	}

	private void updateProgressArrow(Player player) {
		WindowHolder window = player.get(WindowHolder.class);
		if (window != null) {
			float increment = 0.0f;
			if (canSmelt()) {
				float maxSmeltTime = getMaxSmeltTime();
				increment = (MAX_SMELT_TIME_INCREMENT * 20) - ((MAX_SMELT_TIME_INCREMENT / maxSmeltTime) * (getSmeltTime() * 20));
			}
			window.getActiveWindow().setProperty(FurnaceProperty.PROGRESS_ARROW, (int) increment);
		}
	}

	public float getMaxFuel() {
		return getData().get(VanillaData.MAX_FURNACE_FUEL);
	}

	public void setMaxFuel(float maxFuel) {
		getData().put(VanillaData.MAX_FURNACE_FUEL, maxFuel);
	}

	public float getFuel() {
		return getData().get(VanillaData.FURNACE_FUEL);
	}

	public void setFuel(float fuel) {
		getData().put(VanillaData.FURNACE_FUEL, fuel);
		for (Player player : viewers) {
			updateFireIcon(player);
		}
	}

	private void updateFireIcon(Player player) {
		WindowHolder window = player.get(WindowHolder.class);
		if (window != null) {
			// (12.5 / maximum time from fuel source) * (fuel seconds left * 20) = increment to send to client
			float maxFuel = getMaxFuel();
			float increment = MAX_FUEL_INCREMENT / maxFuel * (getFuel() * 20);
			window.getActiveWindow().setProperty(FurnaceProperty.FIRE_ICON, (int) increment);
		}
	}

	public void pulseFuel(float dt) {
		setFuel(getFuel() - dt);
	}

	public void pulseSmeltTime(float dt) {
		setSmeltTime(getSmeltTime() - dt);
	}

	public boolean canSmelt() {
		FurnaceInventory inventory = getInventory();
		ItemStack output = inventory.getOutput();
		return inventory.hasIngredient() && (output == null || output.getMaterial() == ((TimedCraftable) inventory.getIngredient().getMaterial()).getResult().getMaterial());
	}

	public void smelt() {
		FurnaceInventory inventory = getInventory();
		if (!inventory.hasIngredient()) {
			return;
		}

		ItemStack ingredient = inventory.getIngredient();
		ItemStack output = inventory.getOutput();
		if (output == null) {
			inventory.setOutput(((TimedCraftable) ingredient.getMaterial()).getResult());
		} else {
			inventory.addAmount(FurnaceInventory.OUTPUT_SLOT, 1);
		}
		inventory.addAmount(FurnaceInventory.INGREDIENT_SLOT, -1);
		setMaxSmeltTime(-1);
		setSmeltTime(-1);
	}

	@Override
	public void onTick(float dt) {
		final float fuel = getFuel();
		final FurnaceInventory inventory = getInventory();

		// Not burning
		if (fuel <= 0) {
			// Reset any progress
			if (getSmeltTime() > 0) {
				setSmeltTime(-1);
				setMaxSmeltTime(-1);
			}

			// Try to light the furnace
			if (inventory.hasFuel() && inventory.hasIngredient()) {
				float newFuel = ((Fuel) inventory.getFuel().getMaterial()).getFuelTime();
				setMaxFuel(newFuel);
				setFuel(newFuel);
				inventory.addAmount(FurnaceInventory.FUEL_SLOT, -1);
				setBurning(true);
				return;
			}

			setBurning(false);
		}
		// Burning
		else if (fuel > 0) {
			setBurning(true);
			pulseFuel(dt);
			final float smeltTime = getSmeltTime();
			if (smeltTime == -1) {
				// Try to start smelting
				if (inventory.hasIngredient()) {
					if (!canSmelt()) {
						return;
					}
					float newSmeltTime = ((TimedCraftable) inventory.getIngredient().getMaterial()).getCraftTime();
					setMaxSmeltTime(newSmeltTime);
					setSmeltTime(newSmeltTime);
					return;
				}
			}

			if (smeltTime <= 0) {
				// Try to smelt the current ingredient
				if (inventory.hasIngredient()) {
					smelt();
					return;
				}
			}

			if (smeltTime > 0) {
				// Reset progress if ingredient is gone
				if (!inventory.hasIngredient()) {
					setSmeltTime(-1);

					return;
				}
				pulseSmeltTime(dt);
			}
		}
	}

	private void setBurning(boolean burning) {
		VanillaMaterials.FURNACE.setBurning(getBlock(), burning);
	}

	@Override
	public FurnaceInventory getInventory() {
		return getData().get(VanillaData.FURNACE_INVENTORY);
	}

	@Override
	public boolean open(Player player) {
		FurnaceOpenEvent event = Spout.getEventManager().callEvent(new FurnaceOpenEvent(this, player));
		if (!event.isCancelled()) {
			WindowHolder window = player.get(WindowHolder.class);
			if (window != null) {
				window.openWindow(new FurnaceWindow(player, this, getInventory()));
				updateProgressArrow(player);
				updateFireIcon(player);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean close(Player player) {
		FurnaceCloseEvent event = Spout.getEventManager().callEvent(new FurnaceCloseEvent(this, player));
		if (!event.isCancelled()) {
			return super.close(player);
		}
		return false;
	}
}
