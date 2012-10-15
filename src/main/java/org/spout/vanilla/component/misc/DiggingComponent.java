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
package org.spout.vanilla.component.misc;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.MathHelper;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.data.Animation;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;

public class DiggingComponent extends EntityComponent {
	private boolean isDigging;
	protected Point diggingPosition;
	protected long diggingStartTime;
	protected int miningDamagePosition = 0;
	protected long previousDiggingTime = 0;
	protected int miningDamageAllowance = VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_ALLOWANCE.getInt(), miningDamagePeriod = VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_PERIOD.getInt();
	protected int[] miningDamage;

	/**
	 * Returns the digging state of the entity
	 * @return true if player is digging
	 */
	public boolean isDigging() {
		return isDigging;
	}

	/**
	 * Sets isDigging true and records start time, unless already digging
	 * @return true if successful
	 */
	public boolean startDigging(Point position) {
		if (getOwner().getTransform().getPosition().getDistance(position) > 6) { // TODO: Actually get block reach from somewhere instead of just using 6
			return false;
		}
		isDigging = true;
		diggingPosition = position;
		diggingStartTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * Sets isDigging false and records total time, unless the dig was invalid/never started.
	 * @return true if successful
	 */
	public boolean stopDigging(Point position) {
		if (!isDigging) {
			return false;
		}
		previousDiggingTime = getDiggingTime();
		isDigging = false;
		getOwner().getNetwork().callProtocolEvent(new EntityAnimationEvent(getOwner(), Animation.NONE));
		if (!position.equals(diggingPosition)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets time spent digging
	 * @return time spent digging
	 */
	public long getDiggingTime() {
		if (!isDigging) {
			return previousDiggingTime;
		}

		// Is this correct?
		return System.currentTimeMillis() - diggingStartTime;
	}

	/**
	 * Gets last time spent digging in real(client) ticks
	 * @return ticks spent digging
	 */
	public long getDiggingTicks() {
		return getDiggingTime() / 50;
	}

	/**
	 * Adds and checks mining speed for cheating.
	 * @param damageRemaining Remaining damage on block
	 * @return false if player is cheating
	 */
	public boolean addAndCheckMiningSpeed(int damageRemaining) {
		if (!VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_ENABLED.getBoolean()) {
			return true;
		}

		miningDamage[miningDamagePosition++] = damageRemaining;

		if (miningDamagePosition >= miningDamagePeriod) {
			miningDamagePosition = 0;
		}

		return checkMiningSpeed();
	}

	/**
	 * Checks mining speed for cheating.
	 * @return false if player is cheating
	 */
	public boolean checkMiningSpeed() {
		if (MathHelper.mean(miningDamage) > miningDamageAllowance) { // TODO: Make this configurable?
			return false;
		}
		return true;
	}
}