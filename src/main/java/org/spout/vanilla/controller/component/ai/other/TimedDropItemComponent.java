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
package org.spout.vanilla.controller.component.ai.other;

import org.spout.api.entity.BasicComponent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.controller.object.moving.Item;

/**
 * Controller component that drops an {@link Item} every x ticks.
 */
public class TimedDropItemComponent extends BasicComponent<Item> {
	/**
	 * Creates the component for controllers that drop an {@link Item} every x ticks with normal priority.
	 * @param itemDropMinTime the minimum time between the drops
	 * @param itemDropMaxTime the maximum time between the drops
	 */
	public TimedDropItemComponent(int itemDropMinTime, int itemDropMaxTime) {
		this(TickPriority.NORMAL, itemDropMinTime, itemDropMaxTime);
	}

	/**
	 * Creates the component for controllers that drop an {@link Item} every x ticks.
	 * @param itemDropDelay the minimum time between the drops
	 * @param itemDropMaxDelay the maximum time between the drops
	 * @param priority the priority of this component
	 */
	public TimedDropItemComponent(TickPriority priority, float itemDropDelay, float itemDropMaxDelay) {
		super(priority, itemDropDelay, itemDropMaxDelay);
	}

	@Override
	public void onTick(float dt) {
		final Point position = getParent().getParent().getLastTransform().getPosition();
		position.getWorld().createAndSpawnEntity(position, getParent());
	}

	@Override
	public boolean canTick() {
		return true;
	}
}