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
package org.spout.vanilla.world;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.tickable.BasicTickable;
import org.spout.api.tickable.Tickable;

import org.spout.vanilla.controller.world.VanillaSky;
import org.spout.vanilla.data.Weather;

public class WeatherSimulator extends BasicTickable {
	private final VanillaSky sky;
	protected final Random random = new Random();
	protected Weather weather = Weather.CLEAR, forecast = Weather.CLEAR;
	protected float ticksUntilWeatherChange = random.nextFloat() * 5 * 60;
	protected boolean forceWeatherUpdate = false;
	protected LightningSimulator lightning;
	protected float previousRainStrength, currentRainStrength;

	public WeatherSimulator(VanillaSky sky) {
		this.sky = sky;
		this.lightning = new LightningSimulator(this);
	}

	public VanillaSky getSky() {
		return this.sky;
	}

	public World getWorld() {
		return this.sky.getWorld();
	}

	public Weather getCurrent() {
		return this.weather;
	}

	public Weather getForecast() {
		return this.forecast;
	}

	public void setForecast(Weather weather) {
		this.forecast = weather;
	}

	public void forceUpdate() {
		this.forceWeatherUpdate = true;
	}

	/**
	 * Gets if this Weather simulator supports Lightning storms
	 * @return True if it has lightning, False if not
	 */
	public boolean hasLightning() {
		return this.lightning != null;
	}

	/**
	 * Sets if this Weather simulator supports Lightning storms
	 * @param hasLightning state to set to
	 */
	public void setLightning(boolean hasLightning) {
		if (hasLightning && this.lightning == null) {
			this.lightning = new LightningSimulator(this);
		} else {
			this.lightning = null;
		}
	}

	public boolean isThundering() {
		return this.getCurrent() == Weather.THUNDERSTORM;
	}

	public boolean isRaining() {
		return this.getCurrent().isRaining();
	}

	/**
	 * Gets the strength of the rain, which is affected by the duration
	 * @param factor to apply to the changing states
	 * @return the strength
	 */
	public float getRainStrength(float factor) {
		return (this.previousRainStrength + factor * (this.currentRainStrength - this.previousRainStrength));
	}

	/**
	 * Gets the strength of the thunder storm, which is affected by the duration
	 * @param factor to apply to the changing states
	 * @return the strength
	 */
	public float getThunderStrength(float factor) {
		return this.lightning == null ? 0.0f : this.lightning.getThunderStrength(factor) * this.getRainStrength(factor);
	}

	@Override
	public void onTick(float dt) {
		ticksUntilWeatherChange -= dt;
		if (forceWeatherUpdate || ticksUntilWeatherChange <= 0) {
			this.sky.updateWeather(weather, forecast);
			weather = forecast;
			forecast = Weather.get(random.nextInt(3));
			ticksUntilWeatherChange = random.nextFloat() * 5 * 60;
			forceWeatherUpdate = false;
		}
		this.previousRainStrength = this.currentRainStrength;
		if (this.isRaining()) {
			this.currentRainStrength = Math.min(1.0f, this.currentRainStrength + 0.01f);
		} else {
			this.currentRainStrength = Math.max(0.0f, this.currentRainStrength - 0.01f);
		}

		if (this.hasLightning()) {
			this.lightning.onTick(dt);
		}
	}
}
