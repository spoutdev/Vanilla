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
import java.util.Set;

import org.spout.api.entity.Controller;
import org.spout.api.geo.World;
import org.spout.api.player.Player;
import org.spout.vanilla.protocol.msg.TimeMessage;
import org.spout.vanilla.world.Weather;

public class NormalSky extends Controller {
	
	private final World world;
	private float time = 0;
	private float tickcount = 0;
	private float countdown = 20;
	private Weather currentWeather;
	private Weather forecast;
	private float timeUntilWeatherChange = 0.0f;
	private Random random = new Random();
	
	public NormalSky(World world) {
		this.world = world;
	}

	@Override
	public void onAttached() {
		currentWeather = Weather.CLEAR;
		forecast = Weather.CLEAR;
		timeUntilWeatherChange = random.nextFloat() * 5 * 60; //Max 5min till pattern change
	}

	@Override
	public void onTick(float dt) {
		countdown -= 1;
		tickcount += 1;
		if (countdown <= 0) {
			if (tickcount >= 24000) {
				time = 0;
			} else {
				time += 20;
			}
						
			countdown = 20;
			tickcount = 0;
			Set<Player> players = world.getPlayers();
			for (Player player : players) {
				player.getSession().send(new TimeMessage((long) time));
			}
		}
		
		timeUntilWeatherChange -= dt;
		if (timeUntilWeatherChange <= 0.0f) {
			setWeather(forecast);
			switch (random.nextInt(3)) {
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
	}
	
	public Weather getWeather() {
		return currentWeather;
	}

	public void setWeather(Weather pattern) {
		currentWeather = pattern;
		//Throw the event here
	}

	@Override
	public void preSnapshot() {
		
	}
}
