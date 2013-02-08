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
package org.spout.vanilla.plugin.util;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.event.cause.EntityCause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.api.data.GameMode;
import org.spout.api.inventory.Slot;
import org.spout.vanilla.api.inventory.entity.QuickbarInventory;

import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.api.component.misc.HeadComponent;
import org.spout.vanilla.api.data.VanillaData;

public class PlayerUtil {
	/**
	 * Checks whether the inventory of an entity, if available, suppresses item removal costs
	 * @param entity to check it for
	 * @return True if the costs are suppressed, False if not
	 */
	public static boolean isCostSuppressed(Entity entity) {
		return isCreativePlayer(entity);
	}

	/**
	 * Checks whether an Entity is in creative mode
	 * @param entity to check
	 * @return True if the game mode is creative, False if not
	 */
	public static boolean isCreativePlayer(Entity entity) {
		return entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.CREATIVE);
	}

	/**
	 * Gets the quickbar of a player entity<br>
	 * If the entity is not a quickbar-containing entity, null is returned
	 * @param entity to get the quickbar of
	 * @return The quickbar, or null
	 */
	public static QuickbarInventory getQuickbar(Entity entity) {
		if (entity instanceof Player && entity.has(PlayerInventory.class)) {
			return entity.get(PlayerInventory.class).getQuickbar();
		} else {
			return null;
		}
	}

	/**
	 * Gets the currently selected Quickbar Slot of a player entity<br>
	 * Needs to be a valid player entity, otherwise null is returned
	 * @param entity to get the current slot of
	 * @return Current slot
	 */
	public static Slot getHeldSlot(Entity entity) {
		QuickbarInventory bar = getQuickbar(entity);
		return bar == null ? null : bar.getSelectedSlot();
	}

	/**
	 * Gets the required facing for a Block to look at a possible Entity in the cause
	 * @param block to get the facing for
	 * @return The block facing
	 */
	public static BlockFace getBlockFacing(Block block, Cause<?> cause) {
		if (cause instanceof EntityCause) {
			return getBlockFacing(block, ((EntityCause) cause).getSource());
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
		Point position;
		if (entity.has(HeadComponent.class)) {
			position = entity.get(HeadComponent.class).getPosition();
		} else {
			position = entity.getScene().getPosition();
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
	 * Obtains the yaw from a cause, if possible, otherwise 0 is returned
	 * @param cause to obtain the yaw of
	 * @return yaw
	 */
	public static float getYaw(Cause<?> cause) {
		if (cause instanceof EntityCause) {
			return getYaw(((EntityCause) cause).getSource());
		} else {
			return 0.0f;
		}
	}

	/**
	 * Obtains the yaw of an entity, if the entity has a head,
	 * the head yaw is returned instead
	 * @param entity to get the yaw of
	 * @return yaw
	 */
	public static float getYaw(Entity entity) {
		if (entity.has(HeadComponent.class)) {
			return entity.get(HeadComponent.class).getRotation().getYaw();
		} else {
			return entity.getScene().getRotation().getYaw();
		}
	}

	/**
	 * Calculates the facing direction of the entity based on it's
	 * head if it has one, or it's yaw if not.
	 * @param entity
	 * @return the face
	 */
	public static BlockFace getFacing(Entity entity) {
		return BlockFace.fromYaw(getYaw(entity));
	}

	/**
	 * Tries to calculate the facing direction from the cause
	 * if possible. Returns BlockFace.NORTH if not.
	 * @param cause
	 * @return the face
	 */
	public static BlockFace getFacing(Cause<?> cause) {
		return BlockFace.fromYaw(getYaw(cause));
	}
}
