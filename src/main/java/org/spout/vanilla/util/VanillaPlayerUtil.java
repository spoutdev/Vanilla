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
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.components.VanillaPlayerController;
import org.spout.vanilla.components.component.HeadOwner;
import org.spout.vanilla.inventory.player.PlayerInventory;

public class VanillaPlayerUtil {
	/**
	 * Checks if the source is an entity with a vanilla player entity in survival mode
	 * @param source to check
	 * @return True if vanilla survival player entity
	 */
	public static boolean isSurvival(Source source) {
		if (!(source instanceof Entity)) {
			return false;
		}

		Entity entity = (Entity) source;
		return entity.getController() instanceof VanillaPlayerController && ((VanillaPlayerController) entity.getController()).isSurvival();
	}

	/**
	 * Checks if the source is an entity with a vanilla player entity in creative mode
	 * @param source to check
	 * @return True if vanilla creative player entity
	 */
	public static boolean isCreative(Source source) {
		if (!(source instanceof Entity)) {
			return false;
		}

		Entity entity = (Entity) source;
		return entity.getController() instanceof VanillaPlayerController && !((VanillaPlayerController) entity.getController()).isSurvival();
	}

	/**
	 * Gets the required facing for a Block to look at a possible Entity in the Source
	 * @param block to get the facing for
	 * @return The block facing
	 */
	public static BlockFace getBlockFacing(Block block) {
		if (block.getSource() instanceof Entity) {
			return getBlockFacing(block, (Entity) block.getSource());
		} else {
			return BlockFace.TOP;
		}
	}

	/**
	 * Gets the required facing for a Block to look at an Entity
	 * @param block to get the facing for
	 * @param entity to look at
	 * @return The block facing
	 */
	public static BlockFace getBlockFacing(Block block, Entity entity) {
		Controller controller = entity.getController();
		Point position;
		if (controller instanceof HeadOwner) {
			position = ((HeadOwner) controller).getHead().getPosition();
		} else {
			position = entity.getPosition();
		}
		Vector3 diff = position.subtract(block.getX(), block.getY(), block.getZ());
		if (Math.abs(diff.getX()) < 2.0f && Math.abs(diff.getZ()) < 2.0f) {
			if (diff.getY() > 1.8f) {
				return BlockFace.TOP;
			} else if (diff.getY() < -0.2f) {
				return BlockFace.BOTTOM;
			}
		}
		return getFacing(entity).getOpposite();
	}

	/**
	 * Tries to find the facing direction by inspecting the source<br>
	 * If no facing can be found, NORTH is returned
	 * @param source to get it of
	 * @return the face
	 */
	public static BlockFace getFacing(Source source) {
		if (source instanceof Entity) {
			Entity e = (Entity) source;
			float yaw;
			if (e.getController() instanceof HeadOwner) {
				yaw = ((HeadOwner) e.getController()).getHead().getYaw();
			} else {
				yaw = e.getYaw();
			}
			return BlockFace.fromYaw(yaw);
		}
		return BlockFace.NORTH;
	}

	/**
	 * Tries to find the player inventory of the source
	 * @param source
	 * @return inventory
	 */
	public static PlayerInventory getInventory(Source source) {
		if (source instanceof Entity) {
			Entity e = (Entity) source;
			if (e.getController() instanceof VanillaPlayerController) {
				return ((VanillaPlayerController) e.getController()).getInventory();
			}
		}
		return null;
	}

	/**
	 * Tries to find the player selected inventory slot of the source
	 * @param source
	 * @return inventory slot that is selected
	 */
	public static InventorySlot getCurrentSlot(Source source) {
		PlayerInventory inv = getInventory(source);
		return inv == null ? null : inv.getQuickbar().getCurrentSlotInventory();
	}

	/**
	 * Tries to find the currently active item (held item) from a player entity source
	 * @param source
	 * @return the held item, or null if not found
	 */
	public static ItemStack getCurrentItem(Source source) {
		InventorySlot inv = getCurrentSlot(source);
		return inv == null ? null : inv.getItem();
	}
}
