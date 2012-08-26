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
package org.spout.vanilla.entity.component.network;

import org.spout.api.entity.BasicComponent;
import org.spout.api.entity.Controller;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.tickable.TickPriority;

public class PositionUpdateComponent <T extends Controller> extends BasicComponent<T> {
	private int positionTicks = 0;
	private int velocityTicks = 0;
	private Transform lastClientTransform = new Transform();

	public PositionUpdateComponent(TickPriority priority) {
		super(priority);
	}

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		positionTicks++;
		velocityTicks++;
	}

	/**
	 * Tests if a velocity update is needed for this entity<br>
	 * This is called by the entity protocol
	 * @return True if a velocity update is needed
	 */
	public boolean needsVelocityUpdate() {
		return velocityTicks % 5 == 0;
	}

	/**
	 * Tests if a position update is needed for this entity<br>
	 * This is called by the entity protocol
	 * @return True if a position update is needed
	 */
	public boolean needsPositionUpdate() {
		return positionTicks % 30 == 0;
	}

	/**
	 * Sets the last known transformation known by the clients<br>
	 * This should only be called by the protocol classes
	 * @param transform to set to
	 */
	public void setLastClientTransform(Transform transform) {
		this.lastClientTransform = transform.copy();
	}

	/**
	 * Gets the last known transformation updated to the clients
	 * @return the last known transform by the clients
	 */
	public Transform getLastClientTransform() {
		return this.lastClientTransform;
	}
}
