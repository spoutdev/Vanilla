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
package org.spout.vanilla.event.world;

import org.spout.api.event.HandlerList;
import org.spout.api.event.ProtocolEvent;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.data.effect.GeneralEffect;

public class PlayParticleEffectEvent extends ProtocolEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Point position;
	private final GeneralEffect effect;
	private final int data;

	public PlayParticleEffectEvent(Point position, GeneralEffect effect, int data) {
		this.position = position;
		this.effect = effect;
		this.data = data;
	}

	/**
	 * Gets the Position where the Sound should be played
	 * @return position of the Sound
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * Gets the Effect to play
	 * @return the Effect
	 */
	public GeneralEffect getEffect() {
		return this.effect;
	}

	/**
	 * Gets the data to use for the Effect
	 * @return Effect data
	 */
	public int getData() {
		return this.data;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
