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

import org.spout.api.entity.Controller;
import org.spout.api.geo.World;
import org.spout.vanilla.entity.object.Sky;
import org.spout.vanilla.world.Weather;

public class NetherSky extends Controller implements Sky {
	@Override
	public void onAttached() {
		getParent().setObserver(true);
	}

	@Override
	public void onTick(float dt) {
	}

	@Override
	public void setWeather(Weather weather) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Weather getWeather() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setTime(float time) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getTime() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public World getWorld() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
