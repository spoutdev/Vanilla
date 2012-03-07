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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity.object.sky;

import org.spout.vanilla.entity.object.Sky;
import java.util.Random;
import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.entity.Controller;
import org.spout.api.geo.World;
import org.spout.api.player.Player;
import org.spout.vanilla.event.world.WeatherChangeEvent;
import org.spout.vanilla.protocol.msg.StateChangeMessage;
import org.spout.vanilla.protocol.msg.TimeMessage;
import org.spout.vanilla.world.Weather;

public class NormalSky extends Controller implements Sky {
	private float time = 0;
	private float countdown = 20;
	private Weather currentWeather;
	private Weather forecast;
	private float timeUntilWeatherChange = 0.0f;
	private Random random = new Random();

	@Override
	public void onAttached() {
		currentWeather = Weather.CLEAR;
		forecast = Weather.CLEAR;
		timeUntilWeatherChange = random.nextFloat() * 5 * 60; //Max 5min till pattern change
		parent.setObserver(true);
	}

	@Override
	public void onTick(float dt) {
		countdown -= 1;
		if (countdown <= 0) {
			if (time >= 24000) {
				time = 0;
			} else {
				time += 20;
			}

			countdown = 20;
			Set<Player> players = parent.getWorld().getPlayers();
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

	@Override
	public void setTime(float time) {
		this.time = time;
	}

	@Override
	public float getTime() {
		return time;
	}

	@Override
	public Weather getWeather() {
		return currentWeather;
	}

	@Override
	public void setWeather(Weather pattern) {
		WeatherChangeEvent event = new WeatherChangeEvent(this, currentWeather, pattern);
		if (event.isCancelled()) {
			return;
		}

		Spout.getEventManager().callEvent(event);
		currentWeather = event.getNewWeather();
		boolean rain = (currentWeather != Weather.CLEAR);
		StateChangeMessage msg = new StateChangeMessage((byte) (rain ? 1 : 2), (byte) 0);
		for (Player player : parent.getWorld().getPlayers()) {
			player.getSession().send(msg);
		}
	}

	@Override
	public void preSnapshot() {

	}

	@Override
	public World getWorld() {
		return parent.getWorld();
	}
}
