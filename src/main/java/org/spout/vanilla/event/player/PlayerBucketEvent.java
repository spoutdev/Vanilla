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
package org.spout.vanilla.event.player;

import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;

/**
 * Event which is called when a player fills or empties a bucket
 */
public class PlayerBucketEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private ItemStack bucket;
	private Block blockClicked;
	private BlockFace blockFace;
	private boolean filled = false;

	public PlayerBucketEvent(Player p, ItemStack bucket, Block blockClicked, BlockFace blockFace) {
		super(p);
		this.bucket = bucket;
		this.blockClicked = blockClicked;
		this.blockFace = blockFace;
	}

	/**
	 * Get the resulting bucket in hand after the bucket event.
	 * @return EmptyBucket held in hand after the event.
	 */
	public ItemStack getBucket() {
		return bucket;
	}

	/**
	 * Set the item in hand after the event.
	 * @param bucket the new bucket after the bucket event.
	 */
	public void setBucket(ItemStack bucket) {
		this.bucket = bucket;
	}

	/**
	 * Return the block clicked.
	 * @return the clicked block.
	 */
	public Block getBlockClicked() {
		return blockClicked;
	}

	/**
	 * Sets the block that is clicked.
	 * @param blockClicked the new block that is clicked.
	 */
	public void setBlockClicked(Block blockClicked) {
		this.blockClicked = blockClicked;
	}

	/**
	 * Get the face on the clicked block
	 * @return the clicked face
	 */
	public BlockFace getBlockFace() {
		return blockFace;
	}

	/**
	 * Sets the face of the clicked block
	 * @param blockFace The new blockface that was clicked.
	 */
	public void setBlockFace(BlockFace blockFace) {
		this.blockFace = blockFace;
	}

	/**
	 * Returns true if the bucket was filled.
	 * @return True if the bucket was filled.
	 */
	public boolean isFilled() {
		return filled;
	}

	/**
	 * Returns true if the bucket was emptied.
	 * @return True if the bucket was emptied.
	 */
	public boolean isEmptied() {
		return !filled;
	}

	/**
	 * Sets the status of whether the bucket is filled or not.
	 * @param filled True means the bucket is filled, false means the bucket is empty.
	 */
	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
