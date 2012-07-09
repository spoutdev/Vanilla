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
package org.spout.vanilla.util;

import org.spout.api.math.MathHelper;

public class VanillaMathHelper {

	/**
	 * Gets the celestial angle at a certain time of the day
	 * 
	 * @param timeMillis time
	 * @param timeMillisTune fine runing
	 * @return celestial angle
	 */
	public static float getCelestialAngle(long timeMillis, float timeMillisTune) {
		float timeFactor = ((float) (timeMillis % 24000) + timeMillisTune) / 24000f - 0.25f;
		if(timeFactor < 0) {
			timeFactor++;
		} else if(timeFactor > 1) {
			timeFactor--;
		}

		float value = ((float) MathHelper.cos(timeFactor * MathHelper.PI) + 1.0f) / 2.0f;
		timeFactor += (1.0f - value - timeFactor) / 3f;
		return timeFactor;
	}

	/**
	 * Gets the (real?) celestial angle at a certain time of the day<br>
	 * The use of this function is unknown...
	 * 
	 * @param timeMillis time
	 * @param timeMillisTune fine runing
	 * @return celestial angle, a value from 0 to 1
	 */
	public static float getRealCelestialAngle(long timeMillis, float timeMillisTune) {
		float celestial = VanillaMathHelper.getCelestialAngle(timeMillis, timeMillisTune);
		celestial *= MathHelper.TWO_PI;
		celestial = 1.0f - ((float) MathHelper.cos(celestial) * 2.0f + 0.5f);
		if(celestial < 0) {
			celestial = 0.0f;
		} else if(celestial > 1) {
			celestial = 1.0f;
		}
		return 1.0f - celestial;
	}
}
