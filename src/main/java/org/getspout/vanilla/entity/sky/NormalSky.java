/*
 * This file is part of Vanilla (http://www.getspout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.vanilla.entity.sky;

import java.util.Random;

import org.getspout.api.entity.Controller;
import org.getspout.vanilla.world.Weather;

public class NormalSky extends Controller {
	final String weatherKey = "weather";
	final String timeKey = "worldTime";

	final float maxTime = 12000;
	final float gameSecondsPerSecond = 1.f/0.013f; // ~0.013 MCseconds per RL second.
	float time = 0;

	Weather currentWeather;
	Weather forecast;
	float timeUntilWeatherChange = 0.0f;

	Random rng = new Random();

	@Override
	public void onAttached() {
		currentWeather = Weather.CLEAR;
		forecast = Weather.CLEAR;
		timeUntilWeatherChange = rng.nextFloat() * 300; //Max 5min till pattern change
	}

	@Override
	public void onTick(long dt) {
		time = (time + gameSecondsPerSecond * dt) % maxTime;

		timeUntilWeatherChange -= dt;
		if(timeUntilWeatherChange <= 0.0f){
			changeWeatherPattern(forecast);
			switch(rng.nextInt(3)){
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

		parent.setMetadata(timeKey, time);
		parent.setMetadata(weatherKey, currentWeather.getId());
	}

	public void changeWeatherPattern(Weather pattern){
		currentWeather = pattern;
	}
}