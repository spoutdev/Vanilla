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
package org.spout.vanilla.plugin.component.test;

import java.util.List;
import java.util.Random;

import org.spout.api.component.impl.NavigationComponent;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.MathHelper;

public class FollowComponent extends EntityComponent {
	private Entity toFollow;
	private NavigationComponent navigate;

	@Override
	public void onAttached() {
		navigate = getOwner().get(NavigationComponent.class);
		if (navigate == null) {
			throw new IllegalArgumentException("FollowComponent requires NavigateComponent");
		}
	}

	@Override
	public boolean canTick() {
		return toFollow != null;
	}

	@Override
	public void onTick(float dt) {
		if (navigate == null) {
			return;
		}

		//Within 2 blocks of the followed entity? No need to continue
		if (MathHelper.distance(getOwner().getTransform().getPosition(), toFollow.getTransform().getPosition()) <= 3) {
			navigate.stop();
			return;
		}
		//Already navigating and the Entity hasn't moved, no need to continue execution.
		if (navigate.isNavigating() && !toFollow.getTransform().isPositionDirty()) {
			return;
		}
		//We have established the follower is outside 2 block scope of the followed and the followed's position has moved and the follower isn't following...
		// Plot a course, make it so!
		navigate.setDestination(toFollow.getTransform().getPosition());
	}

	/**
	 * Instructs the holder to follow the entity specified. Passing a null will instruct the holder to cancel following.
	 * @param toFollow
	 */
	public void follow(Entity toFollow) {
		this.toFollow = toFollow;
	}

	/**
	 * Instructs the holder to find just a player or any entity and follow it.
	 * @param player true to only find players, false if it doesn't matter
	 * @param range range to scan for
	 * @return Entity the Entity found or null if none found
	 */
	public Entity findAndFollow(boolean player, int range) {
		final Point point = getOwner().getTransform().getPosition();
		List potentialToFollow;
		if (player) {
			potentialToFollow = point.getWorld().getNearbyPlayers(getOwner(), range);
		} else {
			potentialToFollow = point.getWorld().getNearbyEntities(getOwner(), range);
		}
		//None found :'(...return null.
		if (potentialToFollow.size() == 0) {
			return null;
		}
		final static Random random = MathHelper.getRandom();
		final int choice = random.nextInt(potentialToFollow.size());
		final Entity chosenEntity = (Entity) potentialToFollow.get(choice);
		follow(chosenEntity);
		return chosenEntity;
	}
}
