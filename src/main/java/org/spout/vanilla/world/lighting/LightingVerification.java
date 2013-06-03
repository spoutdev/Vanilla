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
package org.spout.vanilla.world.lighting;

import java.util.ArrayList;
import java.util.Collection;

import org.spout.api.Spout;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Vector3;
import org.spout.api.util.bytebit.ByteBitSet;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;

public class LightingVerification {
	private final static BlockFace[] allFaces = BlockFaces.NESWBT.toArray();

	public static boolean checkAll(World w, boolean breakOnError) {
		Collection<Region> regions = w.getRegions();
		Collection<Chunk> chunks = new ArrayList<Chunk>();
		for (Region r : regions) {
			chunks.addAll(getChunks(r));
		}
		return checkChunks(chunks, breakOnError);
	}

	public static boolean checkRegion(Region r, boolean breakOnError) {
		Collection<Chunk> chunks = getChunks(r);
		return checkChunks(chunks, breakOnError);
	}

	public static Collection<Chunk> getChunks(Region r) {
		int size = Region.CHUNKS.SIZE;
		ArrayList<Chunk> chunks = new ArrayList<Chunk>(size * size * size);
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					Chunk c = r.getChunk(x, y, z, LoadOption.NO_LOAD);
					if (c != null) {
						chunks.add(c);
					}
				}
			}
		}
		return chunks;
	}

	public static boolean checkChunks(Collection<Chunk> chunks, boolean breakOnError) {
		int size = chunks.size();
		int count = 1;
		for (Chunk c : chunks) {
			if (checkChunk((count * 100) / size, c, breakOnError) && breakOnError) {
				return true;
			}
			count++;
		}
		return false;
	}

	public static boolean checkChunk(Chunk c, boolean breakOnError) {
		return checkChunk(100, c, breakOnError);
	}

	public static boolean checkChunk(int percent, Chunk c, boolean breakOnError) {
		boolean failure = false;
		Spout.getLogger().info(percent + "%) Testing skylight for chunk at " + c.getBase().toBlockString());
		failure |= checkChunk(c, VanillaLighting.SKY_LIGHT);
		Spout.getLogger().info(percent + "%) Testing blocklight for chunk at " + c.getBase().toBlockString());
		failure |= checkChunk(c, VanillaLighting.BLOCK_LIGHT);
		return failure;
	}

	private static boolean checkChunk(Chunk c, VanillaLightingManager manager) {
		final VanillaCuboidLightBuffer[][][] lightBuffers = getLightBuffers(c, manager);
		final CuboidBlockMaterialBuffer[][][] materialBuffers = getBlockMaterialBuffers(c);
		final int[][] height = getHeightMap(c);
		final int bx = c.getBlockX();
		final int by = c.getBlockY();
		final int bz = c.getBlockZ();
		LightGenerator lightSource = manager == VanillaLighting.SKY_LIGHT ?
				new LightGenerator() {
					@Override
					public int getEmittedLight(int x, int y, int z) {
						if (y + by > height[x][z]) {
							return 15;
						} else {
							return 0;
						}
					}
				} :
				new LightGenerator() {
					@Override
					public int getEmittedLight(int x, int y, int z) {
						CuboidBlockMaterialBuffer b = materialBuffers[1][1][1];
						if (b == null) {
							return 0;
						}
						BlockMaterial m = b.get(bx + x, by + y, bz + z);
						short data = b.getData(bx + x, by + y, bz + z);
						return m.getLightLevel(data);
					}
				};
		boolean failure = false;
		for (int x = 0; x < Chunk.BLOCKS.SIZE; x++) {
			for (int y = 0; y < Chunk.BLOCKS.SIZE; y++) {
				for (int z = 0; z < Chunk.BLOCKS.SIZE; z++) {
					if (!testLight(x, y, z, materialBuffers, lightBuffers, lightSource)) {
						failure = true;
					}
				}
			}
		}
		return failure;
	}

	private static VanillaCuboidLightBuffer[][][] getLightBuffers(Chunk c, VanillaLightingManager manager) {
		VanillaCuboidLightBuffer[][][] buffers = new VanillaCuboidLightBuffer[3][3][3];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					buffers[x][y][z] = getLightBuffer(c.getWorld(), c.getX() - 1 + x, c.getY() - 1 + y, c.getZ() - 1 + z, manager.getId());
				}
			}
		}
		return buffers;
	}

	private static VanillaCuboidLightBuffer getLightBuffer(World w, int cx, int cy, int cz, short id) {
		Chunk c = w.getChunk(cx, cy, cz, LoadOption.LOAD_ONLY);
		if (c == null) {
			return null;
		}
		return (VanillaCuboidLightBuffer) c.getLightBuffer(id);
	}

	private static CuboidBlockMaterialBuffer[][][] getBlockMaterialBuffers(Chunk c) {
		CuboidBlockMaterialBuffer[][][] buffers = new CuboidBlockMaterialBuffer[3][3][3];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					buffers[x][y][z] = getBlockMaterialBuffer(c.getWorld(), c.getX() - 1 + x, c.getY() - 1 + y, c.getZ() - 1 + z);
				}
			}
		}
		return buffers;
	}

	private static CuboidBlockMaterialBuffer getBlockMaterialBuffer(World w, int cx, int cy, int cz) {
		Chunk c = w.getChunk(cx, cy, cz, LoadOption.LOAD_ONLY);
		if (c == null) {
			return null;
		}
		return c.getCuboid(false);
	}

	private static int[][] getHeightMap(Chunk c) {
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];
		for (int x = 0; x < heights.length; x++) {
			for (int z = 0; z < heights.length; z++) {
				heights[x][z] = c.getWorld().getSurfaceHeight(c.getBlockX() + x, c.getBlockZ() + z);
			}
		}
		return heights;
	}

	public static boolean testLight(int x, int y, int z, CuboidBlockMaterialBuffer[][][] materialBuffers, VanillaCuboidLightBuffer[][][] lightBuffers, LightGenerator lightSource) {
		int emitted = lightSource.getEmittedLight(x, y, z);
		BlockMaterial[][][] materials = getNeighborMaterials(x, y, z, materialBuffers);
		int[][][] lightLevels = getNeighborLight(x, y, z, lightBuffers);
		if (emitted > lightLevels[1][1][1]) {
			log("Light below emit level " + emitted + " > " + lightLevels[1][1][1], x, y, z, materialBuffers, lightBuffers, materials, lightLevels);
			return false;
		}
		int inward = checkFlowFrom(materials, lightLevels);
		if (inward > lightLevels[1][1][1]) {
			log("Light below support level " + inward + " > " + lightLevels[1][1][1], x, y, z, materialBuffers, lightBuffers, materials, lightLevels);
			checkSurrounded(materials);
			return false;
		}
		if (inward < lightLevels[1][1][1] && emitted != lightLevels[1][1][1]) {
			log("Light above support level " + inward + " < " + lightLevels[1][1][1] + " and not equal to emitted level " + emitted, x, y, z, materialBuffers, lightBuffers, materials, lightLevels);
			checkSurrounded(materials);
			return false;
		}
		return true;
	}

	private static void checkSurrounded(BlockMaterial[][][] materials) {
		for (BlockFace face : allFaces) {
			IntVector3 o = face.getIntOffset();
			if (materials[1 + o.getX()][1 + o.getY()][1 + o.getZ()] == null) {
				Spout.getLogger().info("Block is not surrounded");
				return;
			}
		}
	}

	public static int checkFlowFrom(BlockMaterial[][][] materials, int[][][] lightLevels) {
		int bestInward = 0;
		for (BlockFace face : allFaces) {
			int flowFrom = checkFlowFrom(face, materials, lightLevels);
			bestInward = Math.max(bestInward, flowFrom);
		}
		return bestInward;
	}

	public static int checkFlowFrom(BlockFace face, BlockMaterial[][][] materials, int[][][] lightLevels) {
		IntVector3 o = face.getIntOffset();
		int ox = o.getX() + 1;
		int oy = o.getY() + 1;
		int oz = o.getZ() + 1;
		BlockMaterial neighbor = materials[ox][oy][oz];
		if (neighbor == null) {
			return 0;
		}
		ByteBitSet occulusion = neighbor.getOcclusion(neighbor.getData());
		if (occulusion.get(face.getOpposite())) {
			return 0;
		}

		BlockMaterial center = materials[1][1][1];
		occulusion = center.getOcclusion(center.getData());
		if (occulusion.get(face)) {
			return 0;
		}
		return lightLevels[ox][oy][oz] - 1 - center.getOpacity();
	}

	private static BlockMaterial[][][] getNeighborMaterials(int x, int y, int z, CuboidBlockMaterialBuffer[][][] buffers) {
		BlockMaterial[][][] materials = new BlockMaterial[3][3][3];
		for (int xx = x - 1; xx <= x + 1; xx++) {
			for (int yy = y - 1; yy <= y + 1; yy++) {
				for (int zz = z - 1; zz <= z + 1; zz++) {
					materials[xx + 1 - x][yy + 1 - y][zz + 1 - z] = getMaterial(xx, yy, zz, buffers);
				}
			}
		}
		return materials;
	}

	private static BlockMaterial getMaterial(int x, int y, int z, CuboidBlockMaterialBuffer[][][] buffers) {
		int xi = 1;
		int yi = 1;
		int zi = 1;

		if (x < 0) {
			xi--;
			x += Chunk.BLOCKS.SIZE;
		}
		if (x >= Chunk.BLOCKS.SIZE) {
			xi++;
			x -= Chunk.BLOCKS.SIZE;
		}
		if (y < 0) {
			yi--;
			y += Chunk.BLOCKS.SIZE;
		}
		if (y >= Chunk.BLOCKS.SIZE) {
			yi++;
			y -= Chunk.BLOCKS.SIZE;
		}
		if (z < 0) {
			zi--;
			z += Chunk.BLOCKS.SIZE;
		}
		if (z >= Chunk.BLOCKS.SIZE) {
			zi++;
			z -= Chunk.BLOCKS.SIZE;
		}
		CuboidBlockMaterialBuffer b = buffers[xi][yi][zi];
		if (buffers[xi][yi][zi] == null) {
			return null;
		}
		Vector3 base = b.getBase();
		return b.get(x + base.getFloorX(), y + base.getFloorY(), z + base.getFloorZ());
	}

	private static int[][][] getNeighborLight(int x, int y, int z, VanillaCuboidLightBuffer[][][] buffers) {
		int[][][] light = new int[3][3][3];
		for (int xx = x - 1; xx <= x + 1; xx++) {
			for (int yy = y - 1; yy <= y + 1; yy++) {
				for (int zz = z - 1; zz <= z + 1; zz++) {
					light[xx + 1 - x][yy + 1 - y][zz + 1 - z] = getLight(xx, yy, zz, buffers);
				}
			}
		}
		return light;
	}

	private static int getLight(int x, int y, int z, VanillaCuboidLightBuffer[][][] buffers) {
		int xi = 1;
		int yi = 1;
		int zi = 1;
		if (x < 0) {
			xi--;
			x += Chunk.BLOCKS.SIZE;
		}
		if (x >= Chunk.BLOCKS.SIZE) {
			xi++;
			x -= Chunk.BLOCKS.SIZE;
		}
		if (y < 0) {
			yi--;
			y += Chunk.BLOCKS.SIZE;
		}
		if (y >= Chunk.BLOCKS.SIZE) {
			yi++;
			y -= Chunk.BLOCKS.SIZE;
		}
		if (z < 0) {
			zi--;
			z += Chunk.BLOCKS.SIZE;
		}
		if (z >= Chunk.BLOCKS.SIZE) {
			zi++;
			z -= Chunk.BLOCKS.SIZE;
		}
		VanillaCuboidLightBuffer b = buffers[xi][yi][zi];
		if (b == null) {
			return 0;
		}
		return buffers[xi][yi][zi].get(x + b.getBase().getFloorX(), y + b.getBase().getFloorY(), z + b.getBase().getFloorZ()) & 0xFF;
	}

	private static interface LightGenerator {
		public int getEmittedLight(int x, int y, int z);
	}

	private static void log(String message, int x, int y, int z, CuboidBlockMaterialBuffer[][][] material, VanillaCuboidLightBuffer[][][] light, BlockMaterial[][][] localMaterials, int[][][] localLight) {
		Vector3 base = material[1][1][1].getBase();
		x += base.getFloorX();
		y += base.getFloorY();
		z += base.getFloorZ();
		Spout.getLogger().info(message + " at " + x + ", " + y + ", " + z);
		Spout.getLogger().info(getCuboid(localLight, localMaterials));
	}

	private final static String[] layers = new String[]{"Bottom", "Middle", "Top"};

	private static String getCuboid(int[][][] light, BlockMaterial[][][] material) {
		StringBuilder sb = new StringBuilder();
		for (int y = 2; y >= 0; y--) {
			sb.append("\n");
			sb.append(layers[y]);
			sb.append("\n");
			for (int x = 0; x < 3; x++) {
				for (int z = 0; z < 3; z++) {
					sb.append(getLocation(x, y, z, light, material));
					if (z != 2) {
						sb.append(", ");
					}
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	private static String getLocation(int x, int y, int z, int[][][] light, BlockMaterial[][][] material) {
		int level = light[x][y][z];
		BlockMaterial m = material[x][y][z];
		if (m == null) {
			return "{" + level + ", null}";
		} else {
			return "{" + level + ", " + m.getClass().getSimpleName() + "}";
		}
	}
}
