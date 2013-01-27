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

import org.spout.vanilla.api.component.substance.material.BrewingStandComponent;
import org.spout.vanilla.api.event.inventory.BrewingStandCloseEvent;
import org.spout.vanilla.api.event.inventory.BrewingStandOpenEvent;
import org.spout.vanilla.api.inventory.window.prop.BrewingStandProperty;
import org.spout.vanilla.api.material.PotionReagent;

import org.spout.vanilla.plugin.component.inventory.WindowHolder;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.inventory.block.BrewingStandInventory;
import org.spout.vanilla.plugin.inventory.window.block.BrewingStandWindow;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.item.potion.Potion;

/**
 * Component that represents a Brewing Stand in the world.
 */
public class BrewingStand extends BrewingStandComponent {
	private final float BREW_TIME_INCREMENT = 20f;

	private ItemStack input;

	@Override
	public BrewingStandInventory getInventory() {
		return getData().get(VanillaData.BREWING_STAND_INVENTORY);
	}

	@Override
	public void onTick(float dt) {
		BrewingStandInventory inventory = getInventory();

		// Update brewing stand display with number of output slots filled
		int filledSlots = 0;
		if (inventory.hasOutput()) {
			for (int i = 0; i < 3; i++) {
				if (inventory.getOutput(i) != null) {
					filledSlots++;
				}
			}
		}
		VanillaMaterials.BREWING_STAND_BLOCK.setFilledPotionSlots(getBlock(), filledSlots);

		if (getBrewTime() <= 0) {
			// Try to start brewing
			if (inventory.hasInput() && inventory.hasOutput()) {
				if (inventory.hasOutput()) {
					// Ensure the input is able to brew the three output items
					for (int i = 0; i < 3; i++) {
						ItemStack output = inventory.getOutput(i);
						if (output == null) {
							continue;
						}

						if (((PotionReagent) inventory.getInput().getMaterial()).getResult((Potion) output.getMaterial()) == null) {
							return;
						}
					}
					input = inventory.getInput(); // Store input just in case it is later removed during the brewing process
					setBrewTime(1);
					inventory.addAmount(BrewingStandInventory.INPUT_SLOT, -1);
				}
			}
		} else {
			// Continue brewing
			float newBrewTime = getBrewTime() + dt;
			if (newBrewTime > BREW_TIME_INCREMENT) {
				// Brewing has finished
				newBrewTime = -1;

				// Set output
				if (inventory.hasOutput()) {
					for (int i = 0; i < 3; i++) {
						ItemStack output = inventory.getOutput(i);
						if (output == null) {
							continue;
						}

						inventory.set(i, new ItemStack(((PotionReagent) input.getMaterial()).getResult((Potion) output.getMaterial()), 1));
					}
				}
			}
			setBrewTime(newBrewTime);
		}
	}

	@Override
	public boolean open(Player player) {
		BrewingStandOpenEvent event = Spout.getEventManager().callEvent(new BrewingStandOpenEvent(this, player));
		if (!event.isCancelled()) {
			player.get(WindowHolder.class).openWindow(new BrewingStandWindow(player, this, getInventory()));
			return true;
		}
		return false;
	}

	@Override
	public boolean close(Player player) {
		BrewingStandCloseEvent event = Spout.getEventManager().callEvent(new BrewingStandCloseEvent(this, player));
		if (!event.isCancelled()) {
			return super.close(player);
		}
		return false;
	}

	public float getBrewTime() {
		return getData().get(VanillaData.BREW_TIME);
	}

	public void setBrewTime(float brewTime) {
		getData().put(VanillaData.BREW_TIME, brewTime);
		for (Player player : viewers) {
			updateProgressArrow(player);
		}
	}

	private void updateProgressArrow(Player player) {
		float increment = 0;
		if (getBrewTime() >= 0) {
			increment = (BREW_TIME_INCREMENT - getBrewTime()) * 20;
		}
		player.get(WindowHolder.class).getActiveWindow().setProperty(BrewingStandProperty.PROGRESS_ARROW, (int) increment);
	}
}
