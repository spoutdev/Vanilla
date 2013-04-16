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
package org.spout.vanilla.data.effect;

import org.spout.api.tickable.BasicTickable;

/**
 * Represents a status effect on an entity.
 */
public class EntityEffect extends BasicTickable {
	private final EntityEffectType type;
	private final int amp;
	private float duration; // no setter because change in duration isn't actually staged on the client
	private int ticks;

	public EntityEffect(EntityEffectType type, int amp, float duration) {
		this.type = type;
		this.amp = amp;
		this.duration = duration;
	}

	public EntityEffect(EntityEffectType type, float duration) {
		this(type, 0, duration);
	}

	/**
	 * Returns the {@link EntityEffectType} of the effect.
	 *
	 * @return type of effect
	 */
	public EntityEffectType getType() {
		return type;
	}

	/**
	 * Returns the amplifier for the effect.
	 *
	 * @return amplifier
	 */
	public int getAmplifier() {
		return amp;
	}

	/**
	 * Returns the duration left in this effect.
	 *
	 * @return duration left
	 */
	public float getDuration() {
		return duration;
	}

	/**
	 * Returns the amount of ticks this effect has been active since
	 * {@link #resetTicks()} was called.
	 *
	 * @return amount of ticks that have passed
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Resets the tick counter to zero.
	 */
	public void resetTicks() {
		ticks = 0;
	}

	@Override
	public void onTick(float dt) {
		ticks++;
		duration -= dt;
	}

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof EntityEffect && ((EntityEffect) obj).type == type;
	}
}
