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
package org.spout.vanilla.plugin.world;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.spout.api.Spout;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.math.MathHelper;
import org.spout.api.tickable.BasicTickable;

import org.spout.vanilla.api.data.Climate;
import org.spout.vanilla.api.data.Weather;
import org.spout.vanilla.api.world.generator.biome.VanillaBiome;

import org.spout.vanilla.plugin.component.world.VanillaSky;
import org.spout.vanilla.plugin.data.VanillaData;

public class WeatherSimulator extends BasicTickable {
	private final VanillaSky sky;
	private final Random random = MathHelper.getRandom();
	private final AtomicBoolean forceWeatherUpdate = new AtomicBoolean(false);
	private LightningSimulator lightning;
	private final SnowSimulator snowfall;

	public WeatherSimulator(VanillaSky sky) {
		this.sky = sky;
		this.lightning = new LightningSimulator(this);
		this.snowfall = new SnowSimulator(this);
	}

	public VanillaSky getSky() {
		return this.sky;
	}

	public World getWorld() {
		return this.sky.getWorld();
	}

	public Weather getCurrent() {
		return sky.getData().get(VanillaData.WORLD_WEATHER);
	}

	public Weather getForecast() {
		return sky.getData().get(VanillaData.WORLD_FORECAST);
	}

	public void setForecast(Weather weather) {
		sky.getData().put(VanillaData.WORLD_FORECAST, weather);
	}

	public void forceUpdate() {
		this.forceWeatherUpdate.lazySet(true);
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

	public boolean isSnowingAt(int x, int y, int z) {
		Biome biome = getWorld().getBiome(x, y, z);
		if (biome instanceof VanillaBiome) {
			VanillaBiome vb = (VanillaBiome) biome;
			if (vb.getClimate() != Climate.COLD) {
				return false;
			}
		}
		return getCurrent().isRaining() && y > getWorld().getSurfaceHeight(x, z);
	}

	public boolean isRainingAt(int x, int y, int z, boolean includeSnow) {
		if (!includeSnow) {
			Biome biome = getWorld().getBiome(x, y, z);
			if (biome instanceof VanillaBiome) {
				VanillaBiome vb = (VanillaBiome) biome;
				if (vb.getClimate() == Climate.COLD) {
					return false;
				}
			}
		}
		return getCurrent().isRaining() && y > getWorld().getSurfaceHeight(x, z);
	}

	/**
	 * Gets the strength of the rain, which is affected by the duration
	 * @param factor to apply to the changing states
	 * @return the strength
	 */
	public float getRainStrength(float factor) {
		final float prevRainStr = sky.getData().get(VanillaData.PREVIOUS_RAIN_STRENGTH);
		return (prevRainStr + factor * (sky.getData().get(VanillaData.CURRENT_RAIN_STRENGTH) - prevRainStr));
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
		float secondsUntilWeatherChange = sky.getData().get(VanillaData.WEATHER_CHANGE_TIME);
		secondsUntilWeatherChange -= dt;
		if (forceWeatherUpdate.compareAndSet(true, false) || secondsUntilWeatherChange <= 0) {
			this.sky.updateWeather(getCurrent(), getForecast());
			sky.getData().put(VanillaData.WORLD_WEATHER, getForecast());
			final Weather current = getCurrent();
			Weather forecast = current;
			while (forecast == current) {
				// Loop until we pick a weather different from current
				forecast = Weather.get(random.nextInt(3));
			}
			setForecast(forecast);
			secondsUntilWeatherChange = current.getBaseWeatherTime() + random.nextInt(current.getRandomWeatherTime());
			Spout.getLogger().info("Weather changed to: " + current + ", next change in " + secondsUntilWeatherChange / 1000F + " s");
		}
		float currentRainStrength = sky.getData().get(VanillaData.CURRENT_RAIN_STRENGTH);
		sky.getData().put(VanillaData.PREVIOUS_RAIN_STRENGTH, currentRainStrength);
		if (this.isRaining()) {
			currentRainStrength = Math.min(1.0f, currentRainStrength + 0.01f);
		} else {
			currentRainStrength = Math.max(0.0f, currentRainStrength - 0.01f);
		}
		sky.getData().put(VanillaData.CURRENT_RAIN_STRENGTH, currentRainStrength);
		if (hasLightning()) {
			lightning.onTick(dt);
		}
		if (getCurrent().isRaining()) {
			snowfall.onTick(dt);
		}
		sky.getData().put(VanillaData.WEATHER_CHANGE_TIME, secondsUntilWeatherChange);
	}

	@Override
	public boolean canTick() {
		return true;
	}
}
