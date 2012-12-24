/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.render;

import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;
import org.spout.api.math.Vector4;
import org.spout.api.render.effect.RenderEffect;
import org.spout.api.render.effect.SnapshotRender;

public class SkyRenderEffect implements RenderEffect {

	private static final float size = 256f;
	private static final float lat = (float) ((25.0 / 180.0) * MathHelper.PI);
	private static final float sunSize = 0.2f;
	private static final float ambient = 0.33f;
	private static final Vector4 nightColor = new Vector4(0.0f, 0.0f, 0.0f, 1.0f);
	private static final Vector4 dayColor = new Vector4(135/255f, 206/255f, 235/255f, 1.0f);
	private static final Vector4 dawnColor = new Vector4(1f, 0.5f, 0.5f, 1.0f);
	private static final float cY = (float) Math.cos(lat);
	private static final float cZ = (float) Math.sin(lat);
	private static volatile boolean force = false;
	private static volatile float xForce = 0;
	private static volatile float yForce = 0;
	private static volatile float zForce = 0;

	public static void setSun(Vector3 pos) {
		if (pos == null) {
			force = false;
		} else {
			xForce = pos.getX();
			yForce = pos.getY();
			zForce = pos.getZ();
			force = true;
		}
	}

	@Override
	public void preRender(SnapshotRender snapshotRender) {
		//TODO : Replace by the real color of the sky taking account of the time
		float f = (float) ((System.currentTimeMillis() % 15000) / 15000.0);

		float rads = (float) (f * 2 * MathHelper.PI);

		float x = (float) Math.sin(rads);

		float y1 = (float) Math.cos(rads);

		float y = (float) (y1 * cY);

		float z = (float) (y1 * cZ);

		if (force) {
			x = xForce;
			y = yForce;
			z = zForce;
		}

		float sunWeight;
		Vector4 skyColor;

		float yAbs = Math.abs(y);
		if (yAbs < sunSize) {
			sunWeight = (y + sunSize) / sunSize / 2.0f;
			Vector4 weightedSun;
			if (y < 0) {
				weightedSun = dawnColor;
			} else {
				float dawnWeight = y / sunSize;
				weightedSun = dayColor.multiply(dawnWeight).add(dawnColor.multiply(1 - dawnWeight));
			}
			skyColor = weightedSun.multiply(sunWeight).add(nightColor.multiply((1 - sunWeight)));
		} else {
			if (y < 0) {
				sunWeight = 0;
				skyColor = nightColor;
			} else {
				sunWeight = 1;
				skyColor = dayColor;
			}
		}

		snapshotRender.getMaterial().getShader().setUniform("ambient", ambient);
		snapshotRender.getMaterial().getShader().setUniform("skyColor", skyColor);
		snapshotRender.getMaterial().getShader().setUniform("sunColor", dayColor.multiply(sunWeight));
		snapshotRender.getMaterial().getShader().setUniform("moonColor", nightColor.multiply(1 - sunWeight));

		Vector4 sunDir = new Vector4(x * size, y * size, z * size, 1.0f);

		//Spout.getLogger().info("f = " + f + " rads = " + rads + " vector " + sunDir);

		snapshotRender.getMaterial().getShader().setUniform("sunDir", sunDir);
	}
	@Override
	public void postRender(SnapshotRender snapshotRender) {
		// TODO Auto-generated method stub

	}

}
