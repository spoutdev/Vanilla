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
package org.spout.vanilla.material.block;

import org.spout.api.collision.BoundingBox;
import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.util.MinecartTrackLogic;
import org.spout.vanilla.util.RailsState;

public abstract class RailsBase extends GroundAttachable {
	public RailsBase(String name, int id) {
		super(name, id);
		this.setCollision(CollisionStrategy.NOCOLLIDE);
		//TODO: Fix this up so we can set this area ourselves in the volume!
		BoundingBox bb = (BoundingBox) this.getBoundingArea();
		bb.set(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
	}

	@Override
	public void loadProperties() {
		super.loadProperties();
		this.setHardness(0.7F).setResistance(1.2F);
	}

	/**
	 * Gets the directions the minecart track is connected to
	 * @param block to get the directions of
	 * @return an array of BlockFace directions that the minecart track connects
	 */
	public BlockFace[] getDirections(Block block) {
		return getState(block).getDirections();
	}

	/**
	 * Sets the direction of a minecart track to face into a non-curved direction
	 * @param block to set it of
	 * @param direction to set to
	 */
	public void setDirection(Block block, BlockFace direction) {
		this.setDirection(block, direction, false);
	}

	/**
	 * Sets the direction of a minecart track to face into a non-curved direction, possibly sloped.
	 * @param block to set it of
	 * @param direction to set to
	 * @param sloped whether the track slopes up the direction given
	 */
	public void setDirection(Block block, BlockFace direction, boolean sloped) {
		this.setState(block, RailsState.get(direction, sloped));
	}

	/**
	 * Sets the direction of a minecart track to face into a curved direction<br>
	 * The from and to are combined into a curved rails state if applicable.
	 * @param block to set it of
	 * @param from direction
	 * @param to direction
	 */
	public void setDirection(Block block, BlockFace from, BlockFace to) {
		this.setState(block, RailsState.get(from, to));
	}

	/**
	 * Gets if this minecart track is connected to the direction specified.
	 * @param block to get it of
	 * @param direction to check
	 * @return True if the direction is connected, False if not
	 */
	public boolean isConnected(Block block, BlockFace direction) {
		return getState(block).isConnected(direction);
	}

	/**
	 * Gets if the minecart track is curved
	 * @param block to get it of
	 * @return True if curved, False if not
	 */
	public boolean isCurved(Block block) {
		return this.getState(block).isCurved();
	}

	/**
	 * Gets if the minecart track is sloped
	 * @param block to get it of
	 * @return True if sloped, False if not
	 */
	public boolean isSloped(Block block) {
		return this.getState(block).isSloped();
	}

	/**
	 * Gets whether this type of rails can curve
	 * @return True if it can curve
	 */
	public abstract boolean canCurve();

	/**
	 * Sets the rails state of this minecart track material
	 * @param block to set it of
	 * @param state to set to
	 */
	public abstract void setState(Block block, RailsState state);

	/**
	 * Gets the rails state of this minecart track material
	 * @param block to get it of
	 * @return the rails state
	 */
	public abstract RailsState getState(Block block);

	public void doTrackLogic(Block block) {
		MinecartTrackLogic logic = MinecartTrackLogic.create(block);
		if (logic != null) {
			logic.refresh();
		}
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			this.doTrackLogic(block);
			return true;
		} else {
			return false;
		}
	}
}
