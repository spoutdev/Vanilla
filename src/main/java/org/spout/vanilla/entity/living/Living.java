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
package org.spout.vanilla.entity.living;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.tickable.LogicPriority;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.entity.VanillaControllerType;
import org.spout.vanilla.entity.VanillaEntityController;
import org.spout.vanilla.entity.component.basic.SuffocationComponent;

public abstract class Living extends VanillaEntityController {
	protected SuffocationComponent suffocationProcess;
	private Point headPos = null;
	private int headYaw = 0;
	private int lastHeadYaw = 0;
	private int nextHeadYaw = 0;
	private boolean headYawChanged;
	private float headHeight = 1.0f;
	private int reach = 5;
	protected boolean crouching;

	protected Living(VanillaControllerType type) {
		super(type);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		suffocationProcess = registerProcess(new SuffocationComponent(this, LogicPriority.HIGHEST));
	}

	/**
	 * Gets the suffocation component (air)
	 * @return entity suffocation process
	 */
	public SuffocationComponent getSuffocation() {
		return suffocationProcess;
	}

	@Override
	public void onTick(float dt) {
		headPos = null;
		super.onTick(dt);
		headYawChanged = false;
		headYaw = calculateHeadYaw();
		if (lastHeadYaw != headYaw) {
			lastHeadYaw = headYaw;
			headYawChanged = true;
		}
	}

	private int calculateHeadYaw() {
		if (nextHeadYaw == 0) {
			nextHeadYaw = (int) (getParent().getYaw());
		}
		int tmp = nextHeadYaw;
		nextHeadYaw = 0;
		return tmp;
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
	public void setHeadYaw(int headYaw) {
		this.nextHeadYaw = headYaw;
	}

	/**
	 * Sets the current height of the head above the main position
	 * @param height
	 */
	public void setHeadHeight(float height) {
		this.headHeight = height;
	}

	/**
	 * Gets the current height of the head above the main position
	 * @return the head height
	 */
	public float getHeadHeight() {
		return this.headHeight;
	}

	/**
	 * Gets the position of the head of this living entity
	 * @return the head position
	 */
	public Point getHeadPosition() {
		if (headPos == null) {
			headPos = this.getParent().getPosition().add(0.0f, this.getHeadHeight(), 0.0f);
		}
		return headPos;
	}

	public Transform getHeadTransform() {
		Transform trans = new Transform();
		trans.setPosition(this.getHeadPosition());
		trans.setRotation(this.getParent().getRotation());
		return trans;
	}

	public BlockIterator getHeadBlockView() {
		return getHeadBlockView(this.getReach());
	}

	public BlockIterator getHeadBlockView(int maxDistance) {
		return new BlockIterator(this.getParent().getWorld(), this.getHeadTransform(), maxDistance);
	}

	public int getHeadYaw() {
		return headYaw;
	}

	public boolean headYawChanged() {
		return headYawChanged;
	}

	public boolean isCrouching() {
		return crouching;
	}

	public void setCrouching(boolean crouching) {
		this.crouching = crouching;
	}

	//TODO Need to remove this or do this better...

	/**
	 * Performs a collision test
	 * @return the first block this Living entity collides with
	 */
	public Block hitTest() {
		Block block;
		for (BlockIterator iter = this.getHeadBlockView(); iter.hasNext(); ) {
			block = iter.next();
			//TODO: Hit box check
			if (!block.getMaterial().isTransparent()) {
				return block;
			}
		}
		return null;
	}
}
