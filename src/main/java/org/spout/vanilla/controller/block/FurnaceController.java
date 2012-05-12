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
package org.spout.vanilla.controller.block;

import org.spout.api.entity.Controller;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.FurnaceInventory;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.solid.Furnace;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.ProgressBarMessage;

import static org.spout.vanilla.protocol.VanillaNetworkSynchronizer.sendPacket;

public class FurnaceController extends VanillaBlockController {
	private final FurnaceInventory inventory = new FurnaceInventory(this);
	private float burnTime = 0, burnIncrement = 0, burnStartTime = 0;
	private float progress = 0, progressIncrement = 0, craftTime = 0;
	private boolean burning;

	public FurnaceController() {
		super(VanillaControllerTypes.FURNACE, VanillaMaterials.FURNACE);
	}

	@Override
	public void onTick(float dt) {
		if (inventory.hasIngredient()) {
			// Increment progress if something is cooking
			if (burning) {
				progress += dt;
				progressIncrement += 180 / (craftTime * 20);
			}

			// Start the burner.
			if (inventory.hasFuel()) {
				if (burnTime <= 0) {
					ItemStack fuelStack = inventory.getFuel();
					Fuel fuel = (Fuel) fuelStack.getMaterial();
					burnTime = fuel.getFuelTime();
					burnStartTime = burnTime;
					burnIncrement = 250;
					burning = true;

					ItemStack ingredientStack = inventory.getIngredient();
					TimedCraftable ingredient = (TimedCraftable) ingredientStack.getMaterial();
					progress = 0;
					progressIncrement = 0;
					craftTime = ingredient.getCraftTime();

					int amount = fuelStack.getAmount();
					inventory.setFuel(fuelStack.setAmount(amount - 1));
				} else {
					// Decrement burn time
					burnTime -= dt;
					burnIncrement -= 250 / (burnStartTime * 20);
				}
			}
		}

		// Update viewers
		for (InventoryViewer viewer : inventory.getViewers()) {
			if (viewer instanceof VanillaNetworkSynchronizer) {
				VanillaNetworkSynchronizer network = (VanillaNetworkSynchronizer) viewer;
				Controller c = network.getEntity().getController();
				if (c instanceof VanillaPlayer && burnTime > 0) {
					VanillaPlayer controller = (VanillaPlayer) c;
					int window = controller.getWindowId();
					sendPacket(controller.getPlayer(), new ProgressBarMessage(window, Furnace.FIRE_ICON, (int) burnIncrement), new ProgressBarMessage(window, Furnace.PROGRESS_ARROW, (int) progressIncrement));
				}
			}
		}

		// Make sure everything gets reset properly, because of the floating points, not everything is always 100% accurate.
		if (burnTime <= 0) {
			burnIncrement = 0;
			burnStartTime = 0;
			burning = false;
		}

		if (progress >= craftTime) {
			progress = 0;
			progressIncrement = 0;
			craftTime = 0;
			ItemStack ingredientStack = inventory.getIngredient();
			if (ingredientStack == null) {
				return;
			}

			int amount = ingredientStack.getAmount();
			inventory.setIngredient(ingredientStack.setAmount(amount - 1));
			ItemStack output = null;
			if (ingredientStack.getMaterial() instanceof TimedCraftable) {
				output = ((TimedCraftable) ingredientStack.getMaterial()).getResult();
			}

			inventory.setOutput(output);
		}
	}

	@Override
	public void onAttached() {
		System.out.println("Furnace entity spawned and controller attached to: " + getParent().getPosition().toString());
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
		return burning;
	}
}
