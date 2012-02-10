/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity.sky;

import java.util.Random;

import org.spout.api.entity.Controller;
import org.spout.vanilla.world.Weather;

public class NormalSky extends Controller {
	final String weatherKey = "weather";
	final String timeKey = "worldTime";

	final float maxTime = 12000;
	final float gameSecondsPerSecond = 1.f / 0.013f; // ~0.013 MCseconds per RL second.
	float time = 0;

	Weather currentWeather;
	Weather forecast;
	float timeUntilWeatherChange = 0.0f;

	Random rng = new Random();

	@Override
	public void onAttached() {
		currentWeather = Weather.CLEAR;
		forecast = Weather.CLEAR;
		timeUntilWeatherChange = rng.nextFloat() * 5 * 60; //Max 5min till pattern change
	}

	@Override
	public void onTick(float dt) {
		time = (time + gameSecondsPerSecond * dt) % maxTime;

		timeUntilWeatherChange -= dt;
		if (timeUntilWeatherChange <= 0.0f) {
			changeWeatherPattern(forecast);
			switch (rng.nextInt(3)) {
				case 0:
					forecast = Weather.CLEAR;
					break;
				case 1:
					forecast = Weather.RAIN;
					break;
				case 2:
					forecast = Weather.THUNDERSTORM;
					break;
			}
		}

		//TODO: parent is null here... what to do?
		//parent.setMetadata(timeKey, time);
		//parent.setMetadata(weatherKey, currentWeather.getId());
	}

	public void changeWeatherPattern(Weather pattern) {
		currentWeather = pattern;
		//Throw the event here
	}

	@Override
	public void preSnapshot() {
	}
}
