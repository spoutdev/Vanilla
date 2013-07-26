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

import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.ProtocolEvent;
import org.spout.api.geo.World;

import org.spout.vanilla.data.Weather;

public class WeatherChangeEvent extends ProtocolEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Weather current;
	private Weather weather;
	private final World world;

	public WeatherChangeEvent(World world, Weather current, Weather weather) {
		this.world = world;
		this.current = current;
		this.weather = weather;
	}

	/**
	 * Gets the weather at the time the event is called.
	 * @return the current weather.
	 */
	public Weather getCurrentWeather() {
		return current;
	}

	/**
	 * Gets the new weather set after the event.
	 * @return the new weather.
	 */
	public Weather getNewWeather() {
		return weather;
	}

	/**
	 * Sets the outcome of the event.
	 * @param weather
	 */
	public void setNewWeather(Weather weather) {
		this.weather = weather;
	}

	/**
	 * Gets the world associated with this event.
	 *
	 * @return The world associated with the event.
	 */
	public World getWorld() {
		return world;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
