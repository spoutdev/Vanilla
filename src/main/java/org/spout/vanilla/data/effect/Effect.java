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
package org.spout.vanilla.data.effect;

import org.spout.api.protocol.Message;
import org.spout.api.tickable.TimedLogicRunnable;

import org.spout.vanilla.controller.living.player.VanillaPlayer;

/**
 * Represents an entity effect that is applied to an entity.
 */
public abstract class Effect extends TimedLogicRunnable<VanillaPlayer> {
	protected int strength, id;

	public Effect(VanillaPlayer effected, int id, float duration, int strength) {
		super(effected, duration);
		this.id = id;
		this.strength = strength;
	}

	/**
	 * Whether or not to send a message to the client when the effect starts.
	 * @return true if has appliance message
	 */
	public abstract boolean hasApplianceMessage();

	/**
	 * Whether or not the send a message to the client when the effects ends.
	 * @return true if has removal message
	 */
	public abstract boolean hasRemovalMessage();

	/**
	 * Gets the message sent to the client when the effect activates.
	 * @return message to send
	 */
	public abstract Message getApplianceMessage();

	/**
	 * Gets the message sent to the client when the effect is removed.
	 * @return message to send
	 */
	public abstract Message getRemovalMessage();

	/**
	 * Gets the id of the effect.
	 * @return id of effect
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the strength of the effect.
	 * @return strength of effect.
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * Sets the strength of the effect.
	 * @param strength of effect
	 */
	public void setStrength(int strength) {
		this.strength = strength;
	}

	@Override
	public void onRegistration() {
		getParent().getPlayer().getSession().send(false, getApplianceMessage());
	}

	@Override
	public boolean shouldRun(float dt) {
		return super.shouldRun(dt);
	}

	@Override
	public void run() {
		getParent().getPlayer().getSession().send(false, getRemovalMessage());
	}
}
