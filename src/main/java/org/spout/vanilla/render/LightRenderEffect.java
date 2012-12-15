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
package org.spout.vanilla.render;

import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector4;
import org.spout.api.render.effect.RenderEffect;
import org.spout.api.render.effect.SnapshotRender;

public class LightRenderEffect implements RenderEffect {
	private static final float size = 256f;

	private static final float freqX = 0.05f;
	private static final float amplX = 0.3f;
	private static final float freqY = 0.31f;
	private static final float amplY = 0.3f;
	
	@Override
	public void preRender(SnapshotRender snapshotRender) {
		//TODO : Replace by the real color of the sky taking account of the time
		//float f = (System.currentTimeMillis() % 15000) / 15000f;
		float f = 1;
		
		snapshotRender.getMaterial().getShader().setUniform("skyColor", new Vector4(f, f, f, 1f));
		
		float x = (float) (MathHelper.mod(2.0 * MathHelper.PI * freqX / 1000.0 * System.currentTimeMillis(), 2.0f * MathHelper.PI) - MathHelper.PI);
		x = amplX * (float) (MathHelper.sin(x) + 1.0f);
		float y = (float) (MathHelper.mod(2.0 * MathHelper.PI * freqY / 1000.0 * System.currentTimeMillis(), 2.0f * MathHelper.PI) - MathHelper.PI);
		y = amplY * (float) (MathHelper.sin(y) + 1.0f);
		
		snapshotRender.getMaterial().getShader().setUniform("sunDir", new Vector4(x * size, 256, y * size, 1f));
	}

	@Override
	public void postRender(SnapshotRender snapshotRender) {
	}
}
