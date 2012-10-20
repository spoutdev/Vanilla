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
package org.spout.vanilla.component.substance.material;

import org.spout.api.entity.Player;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.component.inventory.window.Window;
import org.spout.vanilla.component.inventory.window.block.FurnaceWindow;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.Container;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.inventory.window.prop.FurnaceProperty;
import org.spout.vanilla.material.Fuel;

public class Furnace extends WindowBlockComponent implements Container {
	public final float MAX_FUEL_INCREMENT = 12.5f;

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
		// (12.5 / maximum time from fuel source) * (fuel seconds left * 20) = increment to send to client
		float maxFuel = getMaxFuel();
		float increment = MAX_FUEL_INCREMENT / maxFuel * (fuel * 20);
		for (Player player : viewers) {
			player.get(Window.class).setProperty(FurnaceProperty.FIRE_ICON, (int) increment);
		}
	}

	public void pulseFuel(float dt) {
		setFuel(getFuel() - dt);
	}

	@Override
	public void onTick(float dt) {
		float fuel = getFuel();
		if (fuel <= 0) {
			// Try to light the furnace
			FurnaceInventory inventory = getInventory();
			if (inventory.hasFuel()) {
				float newFuel = ((Fuel) inventory.getFuel().getMaterial()).getFuelTime();
				setMaxFuel(newFuel);
				setFuel(newFuel);
				inventory.addAmount(FurnaceInventory.FUEL_SLOT, -1);
				return;
			}
		}

		if (fuel >= 0) {
			pulseFuel(dt);
			return;
		}
	}

	@Override
	public FurnaceInventory getInventory() {
		return getData().get(VanillaData.FURNACE_INVENTORY);
	}

	@Override
	public void openWindow(Player player) {
		player.add(FurnaceWindow.class).init(getInventory()).open();
	}
}
