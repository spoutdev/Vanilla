/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import gnu.trove.list.array.TFloatArrayList;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.ChunkSnapshot;
import org.spout.api.geo.cuboid.ChunkSnapshotModel;
import org.spout.api.material.BlockMaterial;
import org.spout.api.render.BufferContainer;
import org.spout.api.render.effect.BufferEffect;

import org.spout.vanilla.world.lighting.VanillaCuboidLightBuffer;
import org.spout.vanilla.world.lighting.VanillaLighting;

public class LightBufferEffect implements BufferEffect {
	@Override
	public void post(ChunkSnapshotModel chunkModel, BufferContainer container) {
		TFloatArrayList vertexBuffer = (TFloatArrayList) container.getBuffers().get(0);

		/*
		 * Use a shader light (2) and skylight (4)
		 * 
		 * WE NEED TO USE 2 BECAUSE WE DON'T USE COLOR
		 * OPENGL 2 NEED TO USE LAYOUT IN THE ORDER
		 * WE CAN'T USE 3 IF 2 ISN'T USED
		 * 
		 * One float per vertice
		 * file://Vanilla/shaders/terrain.120.vert 
		 * file://Vanilla/shaders/terrain.330.vert
		 */

		TFloatArrayList lightBuffer = (TFloatArrayList) container.getBuffers().get(1);
		TFloatArrayList skylightBuffer = (TFloatArrayList) container.getBuffers().get(4);

		if (lightBuffer == null) {
			lightBuffer = new TFloatArrayList(vertexBuffer.size() / 4);
			container.setBuffers(1, lightBuffer);
		}

		if (skylightBuffer == null) {
			skylightBuffer = new TFloatArrayList(vertexBuffer.size() / 4);
			container.setBuffers(4, skylightBuffer);
		}

		for (int i = 0; i < vertexBuffer.size(); ) {
			float x = vertexBuffer.get(i++);
			float y = vertexBuffer.get(i++);
			float z = vertexBuffer.get(i++);
			i++; // w component

			//TODO : Create a buffer for each light registred by plugin

			generateLightOnVertices(chunkModel, x, y, z, lightBuffer, skylightBuffer);
		}
	}

	/**
	 * Compute the light for one vertex
	 * @param chunkModel
	 * @param x
	 * @param y
	 * @param z
	 * @param lightBuffer
	 */
	private void generateLightOnVertices(ChunkSnapshotModel chunkModel, float x, float y, float z, TFloatArrayList lightBuffer, TFloatArrayList skylightBuffer) {
		int xi = (int) x;
		int yi = (int) y;
		int zi = (int) z;
		if (chunkModel != null) {
			float light = 0;
			float skylight = 0;
			int count = 0;

			int xs = (x == xi) ? (xi - 1) : xi;
			int ys = (y == yi) ? (yi - 1) : yi;
			int zs = (z == zi) ? (zi - 1) : zi;

			for (int xx = xs; xx <= xi; xx++) {
				for (int yy = ys; yy <= yi; yy++) {
					int zOld = 0;
					ChunkSnapshot chunk = null;
					VanillaCuboidLightBuffer blockLight = null;
					VanillaCuboidLightBuffer skyLight = null;

					for (int zz = zs; zz <= zi; zz++) {
						int zChunk = zz >> Chunk.BLOCKS.BITS;
						if (zChunk != zOld || chunk == null) {
							chunk = chunkModel.getChunkFromBlock(xx, yy, zz);
							blockLight = (VanillaCuboidLightBuffer) chunk.getLightBuffer(VanillaLighting.BLOCK_LIGHT.getId());
							skyLight = (VanillaCuboidLightBuffer) chunk.getLightBuffer(VanillaLighting.SKY_LIGHT.getId());
							zOld = zChunk;
						}
						BlockMaterial m = chunk.getBlockMaterial(xx, yy, zz);
						if (!m.isOpaque()) {
							light += blockLight.get(xx, yy, zz);
							skylight += skyLight.get(xx, yy, zz); //use the SkyLightRaw, the real sky state would be apply by the shader
							count++;
						}
					}
				}
			}

			if (count == 0) {
				count++;
			}

			light /= count;
			skylight /= count;
			light /= 16;
			skylight /= 16;

			//TODO : To replace by 2 byte buffer for Vanilla

			lightBuffer.add(light);
			skylightBuffer.add(skylight);
		} else {
			lightBuffer.add(1f);
			skylightBuffer.add(1f);
		}
	}
}
