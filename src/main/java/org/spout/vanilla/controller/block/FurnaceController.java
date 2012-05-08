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

import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.inventory.FurnaceInventory;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;

public class FurnaceController extends VanillaBlockController {
	private final FurnaceInventory inventory = new FurnaceInventory(this);
	private float progress = 0, burnTime = 0;

	public FurnaceController() {
		super(VanillaControllerTypes.FURNACE, VanillaMaterials.FURNACE);
	}

	@Override
	public void onTick(float dt) {
		// Start the burn timer
		if (inventory.hasFuel() && burnTime <= 0) {
			System.out.println("Has fuel");
			Fuel fuel = (Fuel) inventory.getFuel().getMaterial();
			burnTime = fuel.getFuelTime();
		}

		// Decrement the burn timer
		if (burnTime > 0) {
			burnTime -= dt;
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

	/**
	 * Returns the burn time in Minecraft Ticks
	 * @return
	 */
	public int getBurnTimeTicks() {
		return (int) (burnTime * 20);
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public float getProgress() {
		return progress;
	}

	/**
	 * Returns the progress in Minecraft Ticks
	 * @return
	 */
	public int getProgressTicks() {
		return (int) (progress * 20);
	}
}
