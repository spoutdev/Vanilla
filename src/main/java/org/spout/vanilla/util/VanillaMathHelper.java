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

import java.util.Random;

import org.spout.api.math.MathHelper;
import org.spout.api.math.SinusHelper;
import org.spout.api.math.Vector3;

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
		if (timeFactor < 0) {
			timeFactor++;
		} else if (timeFactor > 1) {
			timeFactor--;
		}

		float value = ((float) MathHelper.cos(timeFactor * MathHelper.PI) + 1.0f) / 2.0f;
		timeFactor += (1.0f - value - timeFactor) / 3f;
		return timeFactor;
	}

	/**
	 * Gets the (real?) celestial angle at a certain time of the day<br> The use
	 * of this function is unknown...
	 *
	 * @param timeMillis time
	 * @param timeMillisTune fine runing
	 * @return celestial angle, a value from 0 to 1
	 */
	public static float getRealCelestialAngle(long timeMillis, float timeMillisTune) {
		float celestial = VanillaMathHelper.getCelestialAngle(timeMillis, timeMillisTune);
		celestial *= MathHelper.TWO_PI;
		celestial = 1.0f - ((float) MathHelper.cos(celestial) * 2.0f + 0.5f);
		if (celestial < 0) {
			celestial = 0.0f;
		} else if (celestial > 1) {
			celestial = 1.0f;
		}
		return 1.0f - celestial;
	}

	/**
	 * Calculates a new random direction
	 *
	 * @param maxXZForce of the direction
	 * @param maxYForce of the direction
	 * @return a random Vector3 direction
	 */
	public static Vector3 getRandomDirection(float maxXZForce, float maxYForce) {
		Random rand = new Random();
		float xzLength = maxXZForce * rand.nextFloat();
		float yLength = maxYForce * (rand.nextFloat() - rand.nextFloat());
		return SinusHelper.getRandom2DAxis(rand).multiply(xzLength).toVector3(yLength);
	}

	// TODO: Get these two functions working in the API!
	public static float getLookAtYaw(Vector3 offset) {
		float yaw = 0;
		// Set yaw
		if (offset.getX() != 0) {
			// Set yaw start value based on dx
			if (offset.getX() < 0) {
				yaw = 270;
			} else {
				yaw = 90;
			}
			yaw -= Math.toDegrees(Math.atan(offset.getZ() / offset.getX()));
		} else if (offset.getZ() < 0) {
			yaw = 180;
		}
		return yaw;
	}

	public static float getLookAtPitch(Vector3 offset) {
		return (float) -Math.toDegrees(Math.atan(offset.getY() / MathHelper.length(offset.getX(), offset.getZ())));
	}

	/**
	 * Calculates the value at x using linear interpolation
	 *
	 * @param x the X coord of the value to interpolate
	 * @param x1 the X coord of q0
	 * @param x2 the X coord of q1
	 * @param q0 the first known value (x1)
	 * @param q1 the second known value (x2)
	 * @return the interpolated value
	 */
	public static double lerp(double x, double x1, double x2, double q0, double q1) {
		return ((x2 - x) / (x2 - x1)) * q0 + ((x - x1) / (x2 - x1)) * q1;
	}

	/**
	 * Calculates the value at x,y using bilinear interpolation
	 *
	 * @param x the X coord of the value to interpolate
	 * @param y the Y coord of the value to interpolate
	 * @param q00 the first known value (x1, y1)
	 * @param q01 the second known value (x1, y2)
	 * @param q10 the third known value (x2, y1)
	 * @param q11 the fourth known value (x2, y2)
	 * @param x1 the X coord of q00 and q01
	 * @param x2 the X coord of q10 and q11
	 * @param y1 the Y coord of q00 and q10
	 * @param y2 the Y coord of q01 and q11
	 * @return the interpolated value
	 */
	public static double biLerp(double x, double y, double q00, double q01,
			double q10, double q11, double x1, double x2, double y1, double y2) {
		double q0 = lerp(x, x1, x2, q00, q10);
		double q1 = lerp(x, x1, x2, q01, q11);
		return lerp(y, y1, y2, q0, q1);
	}

	/**
	 * Calculates the value at x,y,z using trilinear interpolation
	 *
	 * @param x the X coord of the value to interpolate
	 * @param y the Y coord of the value to interpolate
	 * @param z the Z coord of the value to interpolate
	 * @param q000 the first known value (x1, y1, z1)
	 * @param q001 the second known value (x1, y2, z1)
	 * @param q010 the third known value (x1, y1, z2)
	 * @param q011 the fourth known value (x1, y2, z2)
	 * @param q100 the fifth known value (x2, y1, z1)
	 * @param q101 the sixth known value (x2, y2, z1)
	 * @param q110 the seventh known value (x2, y1, z2)
	 * @param q111 the eighth known value (x2, y2, z2)
	 * @param x1 the X coord of q000, q001, q010 and q011
	 * @param x2 the X coord of q100, q101, q110 and q111
	 * @param y1 the Y coord of q000, q010, q100 and q110
	 * @param y2 the Y coord of q001, q011, q101 and q111
	 * @param z1 the Z coord of q000, q001, q100 and q101
	 * @param z2 the Z coord of q010, q011, q110 and q111
	 * @return the interpolated value
	 */
	public static double triLerp(double x, double y, double z, double q000, double q001,
			double q010, double q011, double q100, double q101, double q110, double q111,
			double x1, double x2, double y1, double y2, double z1, double z2) {
		double q00 = lerp(x, x1, x2, q000, q100);
		double q01 = lerp(x, x1, x2, q010, q110);
		double q10 = lerp(x, x1, x2, q001, q101);
		double q11 = lerp(x, x1, x2, q011, q111);
		double q0 = lerp(y, y1, y2, q00, q10);
		double q1 = lerp(y, y1, y2, q01, q11);
		return lerp(z, z1, z2, q0, q1);
	}
}
