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
package org.spout.vanilla.component.world.sky;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.model.Model;

import org.spout.vanilla.component.entity.misc.Sleep;
import org.spout.vanilla.component.world.VanillaWorldComponent;
import org.spout.vanilla.data.Time;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.Weather;
import org.spout.vanilla.render.VanillaEffects;
import org.spout.vanilla.world.WeatherSimulator;

/**
 * Represents a sky in Vanilla
 */
public abstract class Sky extends VanillaWorldComponent {
	public static final byte MIN_SKY_LIGHT = 4;
	public static final byte MAX_SKY_LIGHT = 15;
	public static final byte SKY_LIGHT_RANGE = MAX_SKY_LIGHT - MIN_SKY_LIGHT;
	private static final long REFRESH_RATE = 600;
	private final AtomicLong countdown = new AtomicLong(REFRESH_RATE);
	private WeatherSimulator weather;
	private String model;

	@Override
	public void onAttached() {
		if (this.model != null && getOwner().getEngine().getPlatform() == Platform.CLIENT) {
			// Load the model
			Spout.getLogger().info("Loading Skydome for " + getClass().getSimpleName());
			Model m = (Model) getOwner().getEngine().getFilesystem().getResource(this.model);
			m.getRenderMaterial().addRenderEffect(VanillaEffects.SKY);
			Spout.getLogger().info("Loaded Skydome");
			// Apply model
			getOwner().getData().put("skydome", m);
		}
	}

	/**
	 * Sets the model of this sky
	 * @param model to set to
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the model of this sky
	 * @return model
	 */
	public String getModel() {
		return this.model;
	}

	@Override
	public void onTick(float dt) {
		final long maxTime = getMaxTime();
		float time = getData().get(VanillaData.WORLD_TIME).floatValue();
		time += getRate() * (dt / 50F);
		while (time >= maxTime) {
			time -= maxTime;
		}
		if (countdown.getAndDecrement() <= 0) {
			countdown.set(0);
			updateTime((long) time);
		}

		final List<Player> playerList = getOwner().getPlayers();

		// Sleeping players
		boolean skipNight = false;
		for (Player player : playerList) {
			Sleep c = player.get(Sleep.class);
			if (c == null || c.canSkipNight()) {
				skipNight = true;
			} else {
				skipNight = false;
				break;
			}
		}

		if (skipNight) {
			time = Time.DAWN.getTime();
			for (Player player : playerList) {
				Sleep c = player.get(Sleep.class);
				if (c != null && player.isOnline()) {
					c.wake();
				}
			}
		}
		getData().put(VanillaData.WORLD_TIME, time);

		synchronized (this) {
			if (this.weather != null) {
				this.weather.onTick(dt);
			}
		}
	}

	/**
	 * Sets the time of the sky.
	 * @param time
	 */
	public void setTime(long time) {
		getData().put(VanillaData.WORLD_TIME, time);
		countdown.set(0);
	}

	/**
	 * Gets the time of the sky
	 * @return time
	 */
	public long getTime() {
		return getData().get(VanillaData.WORLD_TIME).longValue();
	}

	/**
	 * Gets the max time of the sky. When the time reached the maxTime, the time
	 * will be set to 0.
	 * @return
	 */
	public long getMaxTime() {
		return getData().get(VanillaData.MAX_TIME);
	}

	/**
	 * Sets the max time of the sky. When the time reaches the maxTime, the time
	 * will be set to 0.
	 * @param maxTime
	 */
	public void setMaxTime(long maxTime) {
		getData().put(VanillaData.MAX_TIME, maxTime);
		countdown.set(0);
	}

	/**
	 * Gets the rate of how many ticks the time is incremented each time update.
	 * @return
	 */
	public long getRate() {
		return getData().get(VanillaData.TIME_RATE);
	}

	/**
	 * Sets the rate of how many ticks the time is incremented by each time
	 * update.
	 * @param rate
	 */
	public void setRate(long rate) {
		getData().put(VanillaData.TIME_RATE, rate);
		countdown.set(0);
	}

	/**
	 * Gets the Weather Simulator
	 * @return the weather simulator, or null if no weather is enabled
	 */
	public synchronized WeatherSimulator getWeatherSimulator() {
		return this.weather;
	}

	/**
	 * Whether or not the sky can produce weather
	 * @return true if sky has weather.
	 */
	public synchronized boolean hasWeather() {
		return this.weather != null;
	}

	/**
	 * Sets whether or not the sky can produce weather.
	 * @param hasWeather
	 */
	public synchronized void setHasWeather(boolean hasWeather) {
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
	public synchronized Weather getWeather() {
		return this.weather == null ? Weather.CLEAR : this.weather.getCurrent();
	}

	/**
	 * Sets the forecast for the next weather change.
	 * @param forecast
	 */
	public synchronized void setWeather(Weather forecast) {
		if (this.weather != null) {
			this.weather.setForecast(forecast);
			this.weather.forceUpdate();
		}
	}

	/**
	 * Gets the forecast for the next weather change.
	 * @return forecast
	 */
	public synchronized Weather getForecast() {
		return this.weather == null ? Weather.CLEAR : this.weather.getForecast();
	}

	public abstract void updateTime(long time);

	public abstract void updatePlayer(Player player);

	public abstract void updateWeather(Weather oldWeather, Weather newWeather);
}
