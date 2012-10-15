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
package org.spout.vanilla.component.world;

import java.util.HashMap;

import org.spout.api.component.Component;
import org.spout.api.component.components.WorldComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;

import org.spout.vanilla.component.misc.SleepComponent;
import org.spout.vanilla.data.Animation;
import org.spout.vanilla.data.Time;
import org.spout.vanilla.data.Weather;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.world.WeatherSimulator;

/**
 * Represents a sky in Vanilla
 */
public abstract class VanillaSky extends WorldComponent {
	public static final byte MIN_SKY_LIGHT = 4;
	public static final byte MAX_SKY_LIGHT = 15;
	public static final byte SKY_LIGHT_RANGE = MAX_SKY_LIGHT - MIN_SKY_LIGHT;
	protected long maxTime, time = 0, countdown = 20, rate;
	private Long setTime;
	private WeatherSimulator weather;
	private static final HashMap<World, VanillaSky> skies = new HashMap<World, VanillaSky>();

	public VanillaSky() {
		maxTime = 24000;
		rate = 20;
	}

	@Override
	public void onAttached() {
		setSky(getWorld(), this);
	}

	@Override
	public void onDetached() {
		setSky(getWorld(), null);
	}

	@Override
	public void onTick(float dt) {
		// Keep time
		if (setTime != null) {
			this.time = setTime;
			setTime = null;
		}
		countdown--;
		if (countdown <= 0) {
			if (time >= maxTime) {
				time = 0;
			} else {
				time += rate;
			}

			countdown = 20;
			updateTime(time);
		}

		// Sleeping players
		boolean skipNight = false;
		for (Player player : getWorld().getPlayers()) {
			if (player.add(SleepComponent.class).canSkipNight()) {
				skipNight = true;
			} else {
				skipNight = false;
				break;
			}
		}

		if (skipNight) {
			setTime = Time.DAWN.getTime();
			for (Player player : getWorld().getPlayers()) {
				player.add(SleepComponent.class).wake();
			}
		}

//		// Weather TODO this thing is possessed and needs to be fixed.
//		if (this.hasWeather()) {
//			this.weather.onTick(dt);
//		}
	}

	/**
	 * Sets the time of the sky.
	 * @param time
	 */
	public void setTime(long time) {
		this.setTime = time;
	}

	/**
	 * Gets the time of the sky
	 * @return time
	 */
	public long getTime() {
		if (setTime != null) {
			return setTime;
		} else {
			return time;
		}
	}

	/**
	 * Gets the max time of the sky. When the time reached the maxTime, the time
	 * will be set to 0.
	 * @return
	 */
	public long getMaxTime() {
		return time;
	}

	/**
	 * Sets the max time of the sky. When the time reaches the maxTime, the time
	 * will be set to 0.
	 * @param maxTime
	 */
	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * Gets the rate of how many ticks the time is incremented each time update.
	 * @return
	 */
	public long getRate() {
		return rate;
	}

	/**
	 * Sets the rate of how many ticks the time is incremented by each time
	 * update.
	 * @param rate
	 */
	public void setRate(long rate) {
		this.rate = rate;
	}

	/**
	 * Gets the Weather Simulator
	 * @return the weather simulator, or null if no weather is enabled
	 */
	public WeatherSimulator getWeatherSimulator() {
		return this.weather;
	}

	/**
	 * Whether or not the sky can produce weather
	 * @return true if sky has weather.
	 */
	public boolean hasWeather() {
		return this.weather != null;
	}

	/**
	 * Sets whether or not the sky can produce weather.
	 * @param hasWeather
	 */
	public void setHasWeather(boolean hasWeather) {
		if (hasWeather && this.weather == null) {
			this.weather = new WeatherSimulator(this);
		} else {
			this.weather = null;
		}
	}

	/**
	 * Gets the weather of the sky.
	 * @return weather
	 */
	public Weather getWeather() {
		return this.weather == null ? Weather.CLEAR : this.weather.getCurrent();
	}

	/**
	 * Sets the forecast for the next weather change.
	 * @param forecast
	 */
	public void setWeather(Weather forecast) {
		if (this.weather != null) {
			this.weather.setForecast(forecast);
			this.weather.forceUpdate();
		}
	}

	/**
	 * Gets the forecast for the next weather change.
	 * @return forecast
	 */
	public Weather getForecast() {
		return this.weather == null ? Weather.CLEAR : this.weather.getForecast();
	}

	/**
	 * Gets the world in which the sky is attached.
	 * @return world
	 */
	public World getWorld() {
		return getOwner().getWorld();
	}

	public static void setSky(World world, VanillaSky sky) {
		synchronized (skies) {
			if (sky == null) {
				skies.remove(world);
			} else {
				skies.put(world, sky);
			}
		}
	}

	public static VanillaSky getSky(World world) {
		synchronized (skies) {
			return skies.get(world);
		}
	}

	protected abstract void updateTime(long time);

	public abstract void updateWeather(Weather oldWeather, Weather newWeather);
}
