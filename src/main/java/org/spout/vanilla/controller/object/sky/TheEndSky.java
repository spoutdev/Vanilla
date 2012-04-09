/**
 * The Permissions project.
 * Copyright (C) 2012 Walker Crouse
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.spout.vanilla.controller.object.sky;

import org.spout.api.entity.type.ControllerType;
import org.spout.api.entity.type.EmptyConstructorControllerType;
import org.spout.vanilla.controller.object.VanillaSky;
import org.spout.vanilla.world.Weather;

public class TheEndSky extends VanillaSky {
	private static final ControllerType TYPE = new EmptyConstructorControllerType(TheEndSky.class, "The End Sky");
	
	public TheEndSky() {
		super(TYPE, false);
	}

	@Override
	public void onAttached() {
	}

	@Override
	public void updateTime(long time) {
	}

	@Override
	public void updateWeather(Weather oldWeather, Weather newWeather) {
	}
}
