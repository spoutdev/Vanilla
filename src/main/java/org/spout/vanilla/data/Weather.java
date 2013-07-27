/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.data;

/**
 * Enum of the vanilla weather states.
 */
public enum Weather {
	/**
	 * Minecraft has a period of 10 minutes (base, guaranteed) + a random amount out of 140 minutes which there will be no weather.
	 * Rain/Snow has a period of 3 minutes (base, guaranteed) + a random amount out of 10 minutes which there will be rain/snow.
	 * Thunder has a period of 10 minutes (base, guaranteed) + a random amount out of 10 minutes which there will be a thunderstorm.
	 */
	CLEAR(false, 600000, 8400000),
	RAIN(true, 180000, 600000),
	THUNDERSTORM(true, 600000, 600000);
	final boolean raining;
	final int baseWeatherTime;
	final int randomWeatherTime;

	private Weather(boolean rain, int baseWeatherTime, int randomWeatherTime) {
		this.raining = rain;
		this.baseWeatherTime = baseWeatherTime;
		this.randomWeatherTime = randomWeatherTime;
	}

	public int getId() {
		return ordinal();
	}

	public int getBaseWeatherTime() {
		return baseWeatherTime;
	}

	public int getRandomWeatherTime() {
		return randomWeatherTime;
	}

	/**
	 * Gets if this Weather state has rain
	 * @return True if it has rain, false if not
	 */
	public boolean isRaining() {
		return this.raining;
	}

	public static Weather get(int id) {
		return values()[id];
	}

	public static Weather get(String name) {
		return valueOf(name.toUpperCase());
	}
}
