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
package org.spout.vanilla.task;

import org.spout.api.geo.World;

import org.spout.vanilla.data.Weather;

/**
 * Running task to keep weather updates synchronized.
 */
public class WeatherTask implements Runnable {
	private final World world;
	private Weather weather;
	private long maxTime;
	private long rate;
	private int update;

	public WeatherTask(World world, Weather weather, long maxTime, long rate) {
		this.world = world;
		this.weather = weather;
		this.maxTime = maxTime;
		this.rate = rate;
		update = -1;
	}

	@Override
	public void run() {
		if (update == -1) {
			update = weather.getId();
		} else {

		}
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public Weather getWeather() {
		return weather;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public long getRate() {
		return rate;
	}

	public void setRate(long rate) {
		this.rate = rate;
	}
}
