/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * vanilla is distributed in the hope that it will be useful,
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
package org.spout.vanilla.controller.world.sky;

import org.spout.api.geo.World;

import org.spout.vanilla.controller.EntityController;
import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.world.Weather;

import java.util.Random;

/**
 * Represents a sky in Vanilla
 */
public abstract class VanillaSky extends EntityController {
	protected long maxTime;
	protected long time = 0;
	protected long countdown = 20;
	protected long rate;
	protected boolean hasWeather;
	protected boolean forceWeatherUpdate = false;
	protected Weather weather = Weather.CLEAR;
	protected Weather forecast = Weather.CLEAR;
	protected final Random random = new Random();
	protected float ticksUntilWeatherChange = random.nextFloat() * 5 * 60;

	public VanillaSky(VanillaControllerType type, boolean hasWeather, long maxTime, long rate) {
		super(type);
		this.maxTime = maxTime;
		this.hasWeather = hasWeather;
		this.rate = rate;
	}

	public VanillaSky(VanillaControllerType type, boolean hasWeather, long maxTime) {
		this(type, hasWeather, maxTime, 20);
	}

	public VanillaSky(VanillaControllerType type, boolean hasWeather) {
		this(type, hasWeather, 24000, 20);
	}

	public VanillaSky(VanillaControllerType type) {
		this(type, false, 24000, 20);
	}

	@Override
	public void onTick(float dt) {

		// Keep time
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

		// Keep weather
		if (hasWeather) {
			ticksUntilWeatherChange -= dt;
			if (forceWeatherUpdate || ticksUntilWeatherChange <= 0) {
				updateWeather(weather, forecast);
				weather = forecast;
				forecast = Weather.getById(random.nextInt(3));
				ticksUntilWeatherChange = random.nextFloat() * 5 * 60;
				forceWeatherUpdate = false;
			}
		}
	}

	/**
	 * Sets the time of the sky.
	 *
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Gets the time of the sky
	 *
	 * @return time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Gets the max time of the sky. When the time reached the maxTime, the time will be set to 0.
	 *
	 * @return
	 */
	public long getMaxTime() {
		return time;
	}

	/**
	 * Sets the max time of the sky. When the time reaches the maxTime, the time will be set to 0.
	 *
	 * @param maxTime
	 */
	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * Gets the rate of how many ticks the time is incremented each time update.
	 *
	 * @return
	 */
	public long getRate() {
		return rate;
	}

	/**
	 * Sets the rate of how many ticks the time is incremented by each time update.
	 *
	 * @param rate
	 */
	public void setRate(long rate) {
		this.rate = rate;
	}

	/**
	 * Whether or not the sky can produce weather
	 *
	 * @return true if sky has weather.
	 */
	public boolean hasWeather() {
		return hasWeather;
	}

	/**
	 * Sets whether or not the sky can produce weather.
	 *
	 * @param hasWeather
	 */
	public void setHasWeather(boolean hasWeather) {
		this.hasWeather = hasWeather;
	}

	/**
	 * Gets the weather of the sky.
	 *
	 * @return weather
	 */
	public Weather getWeather() {
		return weather;
	}

	/**
	 * Sets the forecast for the next weather change.
	 *
	 * @param forecast
	 */
	public void setWeather(Weather forecast) {
		this.forecast = forecast;
		this.forceWeatherUpdate = true;
	}

	/**
	 * Gets the forecast for the next weather change.
	 *
	 * @return forecast
	 */
	public Weather getForecast() {
		return forecast;
	}

	/**
	 * Gets the world in which the sky is attached.
	 *
	 * @return world
	 */
	public World getWorld() {
		return getParent().getWorld();
	}

	protected abstract void updateTime(long time);

	protected abstract void updateWeather(Weather oldWeather, Weather newWeather);
}
