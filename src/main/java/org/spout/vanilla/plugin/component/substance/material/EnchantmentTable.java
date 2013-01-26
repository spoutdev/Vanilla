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
import org.spout.api.geo.cuboid.Block;

import org.spout.vanilla.api.component.substance.material.EnchantmentTableComponent;
import org.spout.vanilla.api.enchantment.Enchantment;
import org.spout.vanilla.api.event.inventory.EnchantmentTableCloseEvent;
import org.spout.vanilla.api.event.inventory.EnchantmentTableOpenEvent;

import org.spout.vanilla.plugin.component.inventory.WindowHolder;
import org.spout.vanilla.plugin.inventory.block.EnchantmentTableInventory;
import org.spout.vanilla.plugin.inventory.window.block.EnchantmentTableWindow;
import org.spout.vanilla.plugin.material.VanillaMaterials;

/**
 * Component that represents a enchantment table in a world.
 */
public class EnchantmentTable extends EnchantmentTableComponent {
	private final EnchantmentTableInventory inventory = new EnchantmentTableInventory();

	@Override
	public EnchantmentTableInventory getInventory() {
		return inventory;
	}

	@Override
	public void onTick(float dt) {
		EnchantmentTableInventory inventory = getInventory();
		// Only display enchantment levels if there is an item and it is not enchanted
		if (inventory.has()) {
			for (Player player : viewers) {
				for (int i = 0; i < 3; i++) {
					player.get(WindowHolder.class).getActiveWindow().setProperty(i, !Enchantment.isEnchanted(inventory.get()) ? inventory.getEnchantmentLevel(i) : 0);
				}
			}
		}
	}

	@Override
	public boolean open(Player player) {
		EnchantmentTableOpenEvent event = Spout.getEventManager().callEvent(new EnchantmentTableOpenEvent(this, player));
		if (!event.isCancelled()) {
			player.get(WindowHolder.class).openWindow(new EnchantmentTableWindow(player, this, inventory));
			return true;
		}
		return false;
	}

	@Override
	public boolean close(Player player) {
		EnchantmentTableCloseEvent event = Spout.getEventManager().callEvent(new EnchantmentTableCloseEvent(this, player));
		if (!event.isCancelled()) {
			return super.close(player);
		}
		return false;
	}

	/**
	 * Returns the amount of bookshelves within a 2 block radius for the X/Z coordinates and 1 block above this enchantment table for the Y coordinate
	 * @return Amount of bookshelves near this enchantment table
	 */
	public int getNearbyBookshelves() {
		Block block = getBlock();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();

		// Check for obstructions - if there are any blocks right next to the enchantment table, all nearby bookshelves are nullified
		for (int xx = x - 1; xx <= x + 1; xx++) {
			for (int yy = y; yy <= y + 1; yy++) {
				for (int zz = z - 1; zz <= z + 1; zz++) {
					if (xx == x && zz == z) {
						continue; // Ignore the enchantment table itself
					}

					if (!VanillaMaterials.AIR.equals(block.getWorld().getBlock(xx, yy, zz))) {
						return 0;
					}
				}
			}
		}

		// Find bookshelves within the radius
		int bookshelves = 0;
		mainLoop: for (int xx = x - 2; xx <= x + 2; xx++) {
			for (int yy = y; yy <= y + 1; yy++) {
				for (int zz = z - 2; zz <= z + 2; zz++) {
					if (VanillaMaterials.BOOKSHELF.equals(block.getWorld().getBlock(xx, yy, zz).getMaterial())) {
						if (++bookshelves >= 15) {
							break mainLoop;
						}
					}
				}
			}
		}

		return bookshelves;
	}
}
