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
package org.spout.vanilla.component.world.sky;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.component.world.SkydomeComponent;
import org.spout.api.entity.Player;
import org.spout.api.model.Model;
import org.spout.api.protocol.NetworkSynchronizer;

import org.spout.vanilla.data.Weather;
import org.spout.vanilla.event.world.TimeUpdateEvent;
import org.spout.vanilla.event.world.WeatherChangeEvent;
import org.spout.vanilla.render.VanillaEffects;
import org.spout.vanilla.util.MathHelper;
import org.spout.vanilla.world.WeatherSimulator;

public class NormalSky extends Sky {
	public NormalSky() {
		super();
		setHasWeather(true);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		if (getOwner().getEngine() instanceof Client) {
			final SkydomeComponent skydome = getOwner().add(SkydomeComponent.class);
			final Model model = Spout.getFileSystem().getResource("model://Vanilla/materials/sky/skydome.spm");
			skydome.setModel(model);
			model.getRenderMaterial().addRenderEffect(VanillaEffects.SKY);
		}
		getOwner().setSkyLight(MAX_SKY_LIGHT);
	}

	@Override
	public void updateTime(long time) {
		this.updateCelestialTime(time, 1.0f);
	}

	@Override
	public void updateWeather(Weather oldWeather, Weather newWeather) {
		WeatherChangeEvent event = getOwner().getEngine().getEventManager().callEvent(new WeatherChangeEvent(getOwner(), oldWeather, newWeather));
		if (event.isCancelled()) {
			return;
		}
		if (oldWeather != newWeather) {
			for (Player player : getOwner().getPlayers()) {
				final NetworkSynchronizer networkSynchronizer = player.getNetworkSynchronizer();
				if (networkSynchronizer != null) {
					networkSynchronizer.callProtocolEvent(event);
				}
			}
		}
	}

	/**
	 * Updates the time as a celestial sky system
	 * @param time to use
	 * @param timeFactor unknown factor, use 1.0f
	 */
	public void updateCelestialTime(long time, float timeFactor) {
		float celestial = MathHelper.getRealCelestialAngle(time, timeFactor);
		WeatherSimulator weather = this.getWeatherSimulator();
		if (weather != null) {
			celestial = (float) ((double) celestial * (1.0d - (double) (weather.getRainStrength(timeFactor) * 5f) / 16d));
			celestial = (float) ((double) celestial * (1.0d - (double) (weather.getThunderStrength(timeFactor) * 5f) / 16d));
		}
		getOwner().setSkyLight((byte) (celestial * (float) SKY_LIGHT_RANGE + MIN_SKY_LIGHT));

		TimeUpdateEvent event = new TimeUpdateEvent(getOwner(), time);
		for (Player player : getOwner().getPlayers()) {
			final NetworkSynchronizer networkSynchronizer = player.getNetworkSynchronizer();
			if (networkSynchronizer != null) {
				networkSynchronizer.callProtocolEvent(event);
			}
		}
	}

	@Override
	public void updatePlayer(Player player) {
		TimeUpdateEvent event = new TimeUpdateEvent(getOwner(), getTime());
		player.getNetworkSynchronizer().callProtocolEvent(event);
		if (Weather.CLEAR != getWeather()) {
			WeatherChangeEvent weatherEvent = new WeatherChangeEvent(getOwner(), Weather.CLEAR, getWeather());
			player.getNetworkSynchronizer().callProtocolEvent(weatherEvent);
		}
	}
}
