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
package org.spout.vanilla.world.generator.skylands.populator;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector3;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.annotated.Load;
import org.spout.api.util.config.annotated.Save;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.MathHelper;
import org.spout.vanilla.world.generator.normal.NormalGenerator;

public class SkylandsGroundCoverPopulator implements GeneratorPopulator {
	private GroundCoverLayer[] groundCover = new GroundCoverLayer[]{
		new GroundCoverUniformLayer(VanillaMaterials.GRASS, (byte) 1),
		new GroundCoverVariableLayer(VanillaMaterials.DIRT, (byte) 1, (byte) 4)
	};

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
				int yy = sizeY - 1;
				yIteration:
				while (yy >= 0) {
					yy = getNextStone(blockData, x + xx, y, z + zz, yy);
					if (yy < 0) {
						break yIteration;
					}
					int layerNumber = 0;
					for (GroundCoverLayer layer : groundCover) {
						final int depthY = yy - layer.getDepth(x + xx, z + zz, (int) seed);
						final BlockMaterial cover = layer.getMaterial();
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

	@Load
	private void load(ConfigurationNode node) {
		final ConfigurationNode groundCoverNode = node.getNode("ground-cover");
		final int count = groundCoverNode.getKeys(false).size();
		if (count == 0) {
			save(node);
			return;
		}
		groundCover = new GroundCoverLayer[count];
		for (int i = 0; i < count; i++) {
			groundCover[i] = GroundCoverLayer.loadNew(groundCoverNode.getNode(Integer.toString(i + 1)));
		}
	}

	@Save
	private void save(ConfigurationNode node) {
		final ConfigurationNode groundCoverNode = node.getNode("ground-cover");
		byte number = 0;
		for (GroundCoverLayer layer : groundCover) {
			layer.save(groundCoverNode.getNode(Byte.toString(++number)));
		}
	}

	public static abstract class GroundCoverLayer {
		private static final Map<String, GroundCoverLayerFactory> FACTORIES =
				new HashMap<String, GroundCoverLayerFactory>();
		private BlockMaterial material;

		public GroundCoverLayer(BlockMaterial material) {
			this.material = material;
		}

		public abstract byte getDepth(int x, int z, int seed);

		public BlockMaterial getMaterial() {
			return material;
		}

		public abstract String getTypeName();

		public void load(ConfigurationNode node) {
			material = (BlockMaterial) MaterialRegistry.get(node.getNode("material").
					getString(material.getDisplayName()));
		}

		public void save(ConfigurationNode node) {
			node.getNode("type").setValue(getTypeName());
			node.getNode("material").setValue(material.getDisplayName());
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
					final BlockMaterial material = (BlockMaterial) MaterialRegistry.
							get(node.getNode("material").getString());
					final ConfigurationNode depthNode = node.getNode("depth");
					final byte minDepth = depthNode.getNode("min").getByte();
					final byte maxDepth = depthNode.getNode("max").getByte();
					return new GroundCoverVariableLayer(material, minDepth, maxDepth);
				}
			});
		}

		public GroundCoverVariableLayer(BlockMaterial material, byte minDepth, byte maxDepth) {
			super(material);
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
					final BlockMaterial material = (BlockMaterial) MaterialRegistry.
							get(node.getNode("material").getString());
					final byte depth = node.getNode("depth").getByte();
					return new GroundCoverUniformLayer(material, depth);
				}
			});
		}

		public GroundCoverUniformLayer(BlockMaterial material, byte depth) {
			super(material);
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
