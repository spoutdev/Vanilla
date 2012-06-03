/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * vanilla is distributed in the hope that it will be useful,
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

import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.ProgressBarMessage;


public class Furnace extends VanillaBlockController {
	private final FurnaceInventory inventory;
	private float burnTime = 0, burnIncrement = 0, burnStartTime = 0;
	private float progress = 0, progressIncrement = 0, craftTime = 0;

	public Furnace() {
		super(VanillaControllerTypes.FURNACE, VanillaMaterials.FURNACE);
		inventory = new FurnaceInventory(this);
	}

	@Override
	public void onAttached() {
	}

	@Override
	public void onTick(float dt) {
		ItemStack input = inventory.getIngredient(), output = inventory.getOutput();
		if (burnTime <= 0) {
			// Start burning
			if (inventory.hasIngredient() && inventory.hasFuel()) {
				ItemStack fuelStack = inventory.getFuel();
				Fuel fuel = (Fuel) fuelStack.getMaterial();
				burnTime = fuel.getFuelTime();
				burnStartTime = burnTime;
				burnIncrement = 250;

				TimedCraftable ingredient = (TimedCraftable) input.getMaterial();
				progress = 0;
				progressIncrement = 0;
				craftTime = ingredient.getCraftTime();

				int amount = fuelStack.getAmount();
				inventory.setFuel(fuelStack.setAmount(amount - 1));
			}
		}

		if (burnTime > 0) {

			// Decrement the burn
			burnTime -= dt;
			burnIncrement -= 250 / (burnStartTime * 20);

			if (inventory.hasIngredient()) {
				// Check for new ingredients
				if (progress <= 0) {
					progress += dt;
					progressIncrement = 0;
					craftTime = ((TimedCraftable) inventory.getIngredient().getMaterial()).getCraftTime();
				} else {
					progress += dt;
					progressIncrement += 180 / (craftTime * 20);
				}
			} else {
				progress = 0;
				progressIncrement = 0;
				craftTime = 0;
			}

			// Reset the burn timer, this is necessary because of the floating points.
			if (burnTime <= 0) {
				burnIncrement = 0;
				burnStartTime = 0;
			}

			if (progress >= craftTime && inventory.hasIngredient()) {
				progress = 0;
				progressIncrement = 0;
				craftTime = 0;
				ItemStack result = ((TimedCraftable) input.getMaterial()).getResult();
				if (output == null) {
					output = result;
				} else {
					output.setAmount(output.getAmount() + result.getAmount());
				}

				int inputAmount = input.getAmount();
				int outputAmount = output.getAmount();
				inventory.setIngredient(input.setAmount(inputAmount - 1));
				inventory.setOutput(output.setAmount(outputAmount));
			}

			// Update viewers
			for (VanillaPlayer player : this.inventory.getViewingPlayers()) {
				int window = player.getActiveWindow().getInstanceId();
				player.getPlayer().getNetworkSynchronizer().callProtocolEvent(new ProgressBarMessage(window, org.spout.vanilla.material.block.controlled.Furnace.FIRE_ICON, (int) burnIncrement));
				player.getPlayer().getNetworkSynchronizer().callProtocolEvent(new ProgressBarMessage(window, org.spout.vanilla.material.block.controlled.Furnace.PROGRESS_ARROW, (int) progressIncrement));
			}
		}
	}

	public FurnaceInventory getInventory() {
		return inventory;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	public float getBurnTime() {
		return burnTime;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public float getProgress() {
		return progress;
	}

	public boolean isBurning() {
		return burnTime > 0;
	}
}
