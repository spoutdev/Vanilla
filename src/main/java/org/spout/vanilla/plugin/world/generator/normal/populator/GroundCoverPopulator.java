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
package org.spout.vanilla.plugin.world.generator.normal.populator;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector3;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.util.MathHelper;
import org.spout.vanilla.plugin.world.generator.normal.NormalGenerator;
import org.spout.vanilla.plugin.world.generator.normal.biome.NormalBiome;

public class GroundCoverPopulator implements GeneratorPopulator {
	@Override
	public void populate(CuboidBlockMaterialBuffer blockData, int x, int y, int z, BiomeManager biomes, long seed) {
		if (y < 0 || y >= NormalGenerator.HEIGHT) {
			return;
		}
		final Vector3 size = blockData.getSize();
		final int sizeX = size.getFloorX();
		final int sizeY = GenericMath.clamp(size.getFloorY(), 0, NormalGenerator.HEIGHT);
		final int sizeZ = size.getFloorZ();
		for (int xx = 0; xx < sizeX; xx++) {
			for (int zz = 0; zz < sizeZ; zz++) {
				final Biome biome = biomes.getBiome(xx, 0, zz);
				if (!(biome instanceof NormalBiome)) {
					continue;
				}
				final GroundCoverLayer[] layers = ((NormalBiome) biome).getGroundCover();
				int yy = sizeY - 1;
				yIteration:
				while (yy >= 0) {
					yy = getNextStone(blockData, x + xx, y, z + zz, yy);
					if (yy < 0) {
						break yIteration;
					}
					int layerNumber = 0;
					for (GroundCoverLayer layer : layers) {
						final int depthY = yy - layer.getDepth(x + xx, z + zz, (int) seed);
						final BlockMaterial cover = layer.getMaterial(y + yy < NormalGenerator.SEA_LEVEL);
						for (; yy > depthY; yy--) {
							if (yy < 0) {
								break yIteration;
							}
							if (blockData.get(x + xx, y + yy, z + zz).equals(VanillaMaterials.STONE)) {
								blockData.set(x + xx, y + yy, z + zz, cover);
							}
						}
					}
					yy = getNextAir(blockData, x + xx, y, z + zz, yy);
					layerNumber++;
				}
			}
		}
	}

	private int getNextStone(CuboidBlockMaterialBuffer blockData, int x, int y, int z, int yy) {
		for (; yy >= 0 && !(blockData.get(x, y + yy, z).equals(VanillaMaterials.STONE)); yy--) {
			// iterate until we reach stone
		}
		return yy;
	}

	private int getNextAir(CuboidBlockMaterialBuffer blockData, int x, int y, int z, int yy) {
		for (; yy >= 0 && blockData.get(x, y + yy, z).equals(VanillaMaterials.STONE); yy--) {
			// iterate until we exit the stone column
		}
		return yy;
	}

	public static abstract class GroundCoverLayer {
		private static final Map<String, GroundCoverLayerFactory> FACTORIES =
				new HashMap<String, GroundCoverLayerFactory>();
		private BlockMaterial aboveSea;
		private BlockMaterial bellowSea;

		public GroundCoverLayer(BlockMaterial aboveSea, BlockMaterial bellowSea) {
			this.aboveSea = aboveSea;
			this.bellowSea = bellowSea;
		}

		public abstract byte getDepth(int x, int z, int seed);

		public BlockMaterial getMaterial(boolean isBellowSea) {
			return isBellowSea ? bellowSea : aboveSea;
		}

		public abstract String getTypeName();

		public void load(ConfigurationNode node) {
			final ConfigurationNode materials = node.getNode("materials");
			aboveSea = (BlockMaterial) MaterialRegistry.get(materials.getNode("above-sea").
					getString(aboveSea.getDisplayName()));
			bellowSea = (BlockMaterial) MaterialRegistry.get(materials.getNode("bellow-sea").
					getString(bellowSea.getDisplayName()));
		}

		public void save(ConfigurationNode node) {
			node.getNode("type").setValue(getTypeName());
			final ConfigurationNode materials = node.getNode("materials");
			materials.getNode("above-sea").setValue(aboveSea.getDisplayName());
			materials.getNode("bellow-sea").setValue(bellowSea.getDisplayName());
		}

		public static void register(String name, GroundCoverLayerFactory facory) {
			FACTORIES.put(name, facory);
		}

		public static GroundCoverLayer loadNew(ConfigurationNode node) {
			return FACTORIES.get(node.getNode("type").getString()).make(node);
		}
	}

	public static class GroundCoverVariableLayer extends GroundCoverLayer {
		private byte min;
		private byte max;

		static {
			register("variable", new GroundCoverLayerFactory() {
				@Override
				public GroundCoverLayer make(ConfigurationNode node) {
					final ConfigurationNode materials = node.getNode("materials");
					final BlockMaterial aboveSea = (BlockMaterial) MaterialRegistry.
							get(materials.getNode("above-sea").getString());
					final BlockMaterial bellowSea = (BlockMaterial) MaterialRegistry.
							get(materials.getNode("bellow-sea").getString());
					final ConfigurationNode depthNode = node.getNode("depth");
					final byte minDepth = depthNode.getNode("min").getByte();
					final byte maxDepth = depthNode.getNode("max").getByte();
					return new GroundCoverVariableLayer(aboveSea, bellowSea, minDepth, maxDepth);
				}
			});
		}

		public GroundCoverVariableLayer(BlockMaterial aboveSea, BlockMaterial bellowSea,
				byte minDepth, byte maxDepth) {
			super(aboveSea, bellowSea);
			this.min = minDepth;
			this.max = maxDepth;
		}

		@Override
		public byte getDepth(int x, int z, int seed) {
			return (byte) (MathHelper.normalizedByte(x, z, seed) * (max - min) + min);
		}

		@Override
		public String getTypeName() {
			return "variable";
		}

		@Override
		public void load(ConfigurationNode node) {
			super.load(node);
			final ConfigurationNode depthNode = node.getNode("depth");
			min = depthNode.getNode("min").getByte(min);
			max = depthNode.getNode("max").getByte(max);
		}

		@Override
		public void save(ConfigurationNode node) {
			super.save(node);
			final ConfigurationNode depthNode = node.getNode("depth");
			depthNode.getNode("min").setValue(min);
			depthNode.getNode("max").setValue(max);
		}
	}

	public static class GroundCoverUniformLayer extends GroundCoverLayer {
		private byte depth;

		static {
			register("uniform", new GroundCoverLayerFactory() {
				@Override
				public GroundCoverLayer make(ConfigurationNode node) {
					final ConfigurationNode materials = node.getNode("materials");
					final BlockMaterial aboveSea = (BlockMaterial) MaterialRegistry.
							get(materials.getNode("above-sea").getString());
					final BlockMaterial bellowSea = (BlockMaterial) MaterialRegistry.
							get(materials.getNode("bellow-sea").getString());
					final byte depth = node.getNode("depth").getByte();
					return new GroundCoverUniformLayer(aboveSea, bellowSea, depth);
				}
			});
		}

		public GroundCoverUniformLayer(BlockMaterial aboveSea, BlockMaterial bellowSea,
				byte depth) {
			super(aboveSea, bellowSea);
			this.depth = depth;
		}

		@Override
		public byte getDepth(int x, int z, int seed) {
			return depth;
		}

		@Override
		public String getTypeName() {
			return "uniform";
		}

		@Override
		public void load(ConfigurationNode node) {
			super.load(node);
			depth = node.getNode("depth").getByte(depth);
		}

		@Override
		public void save(ConfigurationNode node) {
			super.save(node);
			node.getNode("depth").setValue(depth);
		}
	}

	public static interface GroundCoverLayerFactory {
		public GroundCoverLayer make(ConfigurationNode node);
	}
}
