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
package org.spout.vanilla.plugin.render;

import org.spout.api.math.GenericMath;
import org.spout.api.math.TrigMath;
import org.spout.api.math.Vector2;
import org.spout.api.render.effect.RenderEffect;
import org.spout.api.render.effect.SnapshotRender;

public class LiquidRenderEffect implements RenderEffect {
	private static final float size = 1.0f / 16.0f;
	private static final float freqX = 0.05f;
	private static final float amplX = 0.3f;
	private static final float freqY = 0.31f;
	private static final float amplY = 0.3f;

	@Override
	public void preRender(SnapshotRender snapshotRender) {
		float x = (float) (GenericMath.mod(TrigMath.TWO_PI * freqX / 1000.0 * System.currentTimeMillis(), TrigMath.TWO_PI) - TrigMath.PI);
		x = amplX * (float) (TrigMath.sin(x) + 1.0f);
		float y = (float) (GenericMath.mod(TrigMath.TWO_PI * freqY / 1000.0 * System.currentTimeMillis(), TrigMath.TWO_PI) - TrigMath.PI);
		y = amplY * (float) (TrigMath.sin(y) + 1.0f);
		snapshotRender.getMaterial().getShader().setUniform("animation", new Vector2(x * size, y * size));
	}

	@Override
	public void postRender(SnapshotRender snapshotRender) {
	}
}
