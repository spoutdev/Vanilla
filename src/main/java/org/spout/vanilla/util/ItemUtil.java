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
package org.spout.vanilla.util;

import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Vector3;
import org.spout.vanilla.controller.object.moving.Item;

public class ItemUtil {

	/**
	 * Drops an item at the position with the item stack specified with a natural random velocity
	 * @param position to spawn the item
	 * @param itemStack to set to the item
	 * @return the Item controller
	 */
	public static Item dropItemNaturally(Point position, ItemStack itemStack) {
		Vector3 velocity = new Vector3(Math.random() * 0.2 - 0.1, 0.2, Math.random() * 0.2 - 0.1);
		return dropItem(position, itemStack, velocity);
	}

	/**
	 * Drops an item at the position with the item stack specified
	 * @param position to spawn the item
	 * @param itemStack to set to the item
	 * @param velocity to drop at
	 * @return the Item controller
	 */
	public static Item dropItem(Point position, ItemStack itemStack, Vector3 velocity) {
		Item item = new Item(itemStack, velocity);
		position.getWorld().createAndSpawnEntity(position, item);
		return item;
	}
	
}
