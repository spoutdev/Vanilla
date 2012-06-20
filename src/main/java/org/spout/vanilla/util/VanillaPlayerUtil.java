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
package org.spout.vanilla.util;

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.living.player.VanillaPlayer;

public class VanillaPlayerUtil {
	/**
	 * Checks if the source is an entity with a vanilla player controller in survival mode
	 * @param source to check
	 * @return True if vanilla survival player entity
	 */
	public static boolean isSurvival(Source source) {
		if (!(source instanceof Entity)) {
			return false;
		}

		Entity entity = (Entity) source;
		return entity.getController() instanceof VanillaPlayer && ((VanillaPlayer) entity.getController()).isSurvival();
	}

	/**
	 * Checks if the source is an entity with a vanilla player controller in creative mode
	 * @param source to check
	 * @return True if vanilla creative player entity
	 */
	public static boolean isCreative(Source source) {
		if (!(source instanceof Entity)) {
			return false;
		}

		Entity entity = (Entity) source;
		return entity.getController() instanceof VanillaPlayer && !((VanillaPlayer) entity.getController()).isSurvival();
	}

	/**
	 * Tries to find the facing direction by inspecting the source<br>
	 * If no facing can be found, NORTH is returned
	 * @param source to get it of
	 * @return the face
	 */
	public static BlockFace getFacing(Source source) {
		if (source instanceof Entity) {
			return BlockFace.fromYaw(((Entity) source).getYaw());
		}
		return BlockFace.NORTH;
	}

	/**
	 * Tries to find the player inventory of the source
	 * @param source
	 * @return inventory
	 */
	public static InventoryBase getInventory(Source source) {
		if (source instanceof Entity) {
			Entity e = (Entity) source;
			if (e.getController() instanceof VanillaPlayer) {
				return ((VanillaPlayer) e.getController()).getInventory();
			}
		}
		return null;
	}

	/**
	 * Tries to find the currently active item (held item) from a player entity source
	 * @param source
	 * @return the held item, or null if not found
	 */
	public static ItemStack getCurrentItem(Source source) {
		InventoryBase inv = getInventory(source);
		return inv == null ? null : inv.getCurrentItem();
	}
}
