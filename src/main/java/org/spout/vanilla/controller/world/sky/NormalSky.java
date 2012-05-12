/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.controller.world.sky;

import org.spout.api.Spout;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.protocol.msg.ChangeGameStateMessage;
import org.spout.vanilla.protocol.msg.TimeUpdateMessage;
import org.spout.vanilla.world.Weather;

import static org.spout.vanilla.protocol.VanillaNetworkSynchronizer.broadcastPacket;

public class NormalSky extends VanillaSky {
	public NormalSky() {
		super(VanillaControllerTypes.NORMAL_SKY, true);
	}

	@Override
	public void updateTime(long time) {
		broadcastPacket(new TimeUpdateMessage(time));
	}

	@Override
	public void updateWeather(Weather oldWeather, Weather newWeather) {
		org.spout.vanilla.event.world.WeatherChangeEvent event = Spout.getEventManager().callEvent(new org.spout.vanilla.event.world.WeatherChangeEvent(this, oldWeather, newWeather));
		if (event.isCancelled()) {
			return;
		}

		byte reason = newWeather.equals(Weather.RAIN) || newWeather.equals(Weather.THUNDERSTORM) ? ChangeGameStateMessage.BEGIN_RAINING : ChangeGameStateMessage.END_RAINING;
		broadcastPacket(new ChangeGameStateMessage(reason));
	}
}
