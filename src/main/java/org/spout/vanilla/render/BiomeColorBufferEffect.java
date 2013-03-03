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

import java.awt.Color;
import java.util.Map;

import gnu.trove.list.TFloatList;
import gnu.trove.list.array.TFloatArrayList;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.ChunkSnapshotModel;
import org.spout.api.math.GenericMath;
import org.spout.api.render.BufferContainer;
import org.spout.api.render.effect.BufferEffect;

import org.spout.vanilla.world.generator.biome.VanillaBiome;
import org.spout.vanilla.world.generator.biome.VanillaBiomes;

public abstract class BiomeColorBufferEffect implements BufferEffect {
	@Override
	public void post(ChunkSnapshotModel chunkModel, BufferContainer value) {
		final Map<Integer, Object> buffers = value.getBuffers();
		final TFloatList vertices = (TFloatList) buffers.get(0);
		final int vertexCount = vertices.size();
		final TFloatList biomeColors;
		if (!buffers.containsKey(5)) {
			biomeColors = new TFloatArrayList(vertexCount);
			buffers.put(5, biomeColors);
		} else {
			biomeColors = (TFloatList) buffers.get(5);
		}
		// This colors whole block faces at once. Doing it for each vertex is too expensive.
		// 24 vertex coords per face (2 mesh face per block face, 3 vertices per mesh face, 4 coords per vertices)
		// This expects vertices to be grouped by block face.
		final World world = chunkModel.getCenter().getWorld();
		for (int i = 0; i < vertexCount; i += 24) {
			float r = 0;
			float g = 0;
			float b = 0;
			final int x = GenericMath.floor(vertices.get(i));
			final int y = GenericMath.floor(vertices.get(i + 1));
			final int z = GenericMath.floor(vertices.get(i + 2));
			for (byte xx = -1; xx <= 1; xx++) {
				for (byte zz = -1; zz <= 1; zz++) {
					VanillaBiome biome = (VanillaBiome) world.getBiome(x + xx, y, z + zz);
					// Default to forest if no biome is set
					if (biome == null) {
						biome = VanillaBiomes.FOREST;
					}
					final Color color = getBiomeColor(biome);
					r += color.getRed();
					g += color.getGreen();
					b += color.getBlue();
				}
			}
			r /= 9;
			r /= 255;
			g /= 9;
			g /= 255;
			b /= 9;
			b /= 255;
			for (int ii = 0; ii < 6; ii++) {
				biomeColors.add(r);
				biomeColors.add(g);
				biomeColors.add(b);
				biomeColors.add(1);
			}
		}
	}

	protected abstract Color getBiomeColor(VanillaBiome biome);
}
