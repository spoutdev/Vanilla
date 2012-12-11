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

import java.awt.Color;

import org.spout.api.geo.World;
import org.spout.api.math.Vector3;
import org.spout.api.render.effect.VertexEffect;

import org.spout.vanilla.world.generator.biome.VanillaBiome;

public abstract class AverageBiomeColorVertexEffect extends VertexEffect {
	public AverageBiomeColorVertexEffect(int layout) {
		super(layout);
	}

	@Override
	public float[] getVertexData(World world, Vector3 pos) {
		float[] rgb = new float[4];
		for (byte x = -1; x <= 1; x++) {
			for (byte z = -1; z <= 1; z++) {
				final Vector3 p = pos.add(x, 0, z);
				final float[] brgb = getBiomeColor((VanillaBiome) world.getBiome(p.getFloorX(), p.getFloorY(), p.getFloorZ())).
						getRGBColorComponents(null);
				rgb[0] += brgb[0];
				rgb[1] += brgb[1];
				rgb[2] += brgb[2];
			}
		}
		rgb[0] /= 9;
		rgb[1] /= 9;
		rgb[2] /= 9;
		rgb[3] = 1;
		return rgb;
	}

	protected abstract Color getBiomeColor(VanillaBiome biome);
}
