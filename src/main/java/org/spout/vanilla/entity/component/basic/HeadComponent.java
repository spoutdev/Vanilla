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
package org.spout.vanilla.entity.component.basic;

import org.spout.api.entity.BasicComponent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Vector3;
import org.spout.api.util.BlockIterator;
import org.spout.vanilla.entity.component.HeadOwner;

public class HeadComponent extends BasicComponent<HeadOwner> {
	private Vector3 lookingAt = Vector3.ZERO;
	private int headYaw = 0;
	private int lastHeadYaw = 0;
	private int nextHeadYaw = 0;
	private boolean headYawChanged;
	private float headHeight = 1.0f;
	private int reach = 5; //TODO: Find a better location for this

	@Override
	public void onTick(float dt) {
		headYawChanged = false;
		if (nextHeadYaw == 0) {
			nextHeadYaw = (int) (getParent().getParent().getYaw());
		}
		headYaw = nextHeadYaw;
		nextHeadYaw = 0;
		if (lastHeadYaw != headYaw) {
			lastHeadYaw = headYaw;
			headYawChanged = true;
		}
	}

	@Override
	public boolean canTick() {
		return true;
	}

	/**
	 * Sets the maximum distance this Living Entity can interact at
	 * @param reach distance
	 */
	public void setReach(int reach) {
		this.reach = reach;
	}

	/**
	 * Gets the maximum distance this Living Entity can interact at
	 * @return reach distance
	 */
	public int getReach() {
		return this.reach;
	}

	/**
	 * Sets the yaw of a entity's head for the next tick.
	 * @param headYaw
	 */
	public void setYaw(int headYaw) {
		this.nextHeadYaw = headYaw;
	}

	/**
	 * Sets the position where the player should look.
	 * @param lookingAt {@link org.spout.api.math.Vector3} to look at
	 */
	public void setLooking(Vector3 lookingAt) {
		this.lookingAt = lookingAt;
	}

	/**
	 * Gets the {@link Vector3} the player is currently looking at.
	 * @return position the player is looking at
	 */
	public Vector3 getLookingAt() {
		return lookingAt;
	}

	/**
	 * Sets the current height of the head above the main position
	 * @param height
	 */
	public void setHeight(float height) {
		this.headHeight = height;
	}

	/**
	 * Gets the current height of the head above the main position
	 * @return the head height
	 */
	public float getHeight() {
		return this.headHeight;
	}

	/**
	 * Gets the position of the head of this living entity
	 * @return the head position
	 */
	public Point getPosition() {
		return this.getParent().getParent().getPosition().add(0.0f, this.getHeight(), 0.0f);
	}

	public Transform getTransform() {
		Transform trans = new Transform();
		trans.setPosition(this.getPosition());
		trans.setRotation(getParent().getParent().getRotation());
		return trans;
	}

	public BlockIterator getBlockView() {
		return getBlockView(this.getReach());
	}

	public BlockIterator getBlockView(int maxDistance) {
		return new BlockIterator(this.getParent().getParent().getWorld(), this.getTransform(), maxDistance);
	}

	public int getYaw() {
		return headYaw;
	}

	public boolean yawChanged() {
		return headYawChanged;
	}

	/**
	 * Performs a collision test
	 * 
	 * @return the first block this Living entity collides with
	 */
	public Block hitTest() {
		Block block;
		for (BlockIterator iter = this.getBlockView(); iter.hasNext(); ) {
			block = iter.next();
			//TODO: Hit box check
			if (!block.getMaterial().isTransparent()) {
				return block;
			}
		}
		return null;
	}
}
