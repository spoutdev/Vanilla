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
package org.spout.vanilla.world.generator.normal.object.largeplant;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.solid.MushroomBlock;
import org.spout.vanilla.world.generator.object.LargePlantObject;

public class HugeMushroomObject extends LargePlantObject {
	// shape of the mushroom
	private HugeMushroomShape shape;
	// materials to use
	private BlockMaterial capMaterial;
	private BlockMaterial stemMaterial;
	// size control
	private byte capRadius;
	private byte capThickness;
	// use texture data only for huge mushroom blocks
	private boolean useTextureMetadata;
	// radius to check for canPlaceObject
	private byte initCheckRadius = 0;
	private byte finCheckRadius = 3;
	// for canPlaceObject check
	private final Set<BlockMaterial> overridable = new HashSet<BlockMaterial>();

	public HugeMushroomObject(HugeMushroomType type) {
		this(null, type);
	}

	public HugeMushroomObject(Random random, HugeMushroomType type) {
		super(random, (byte) 3, (byte) 4);
		shape = type.shape;
		capMaterial = type.material;
		stemMaterial = type.material;
		capRadius = type.capRadius;
		capThickness = type.capThickness;
		overridable.add(VanillaMaterials.AIR);
		overridable.add(VanillaMaterials.LEAVES);
		overridable.add(VanillaMaterials.RED_MUSHROOM);
		overridable.add(VanillaMaterials.BROWN_MUSHROOM);
		checkIfUseTextureMetadata();
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		final BlockMaterial under = w.getBlockMaterial(x, y - 1, z);
		if (under != VanillaMaterials.DIRT && under != VanillaMaterials.GRASS && under != VanillaMaterials.MYCELIUM) {
			return false;
		}
		byte radiusToCheck = initCheckRadius;
		for (int yy = y; yy < y + totalHeight + 2; yy++) {
			if (yy == y + 1) {
				radiusToCheck = finCheckRadius;
			}
			for (int xx = x - radiusToCheck; xx < x + 1 + radiusToCheck; xx++) {
				for (int zz = z - radiusToCheck; zz < z + 1 + radiusToCheck; zz++) {
					if (!overridable.contains(w.getBlockMaterial(xx, yy, zz))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		w.setBlockMaterial(x, y - 1, z, VanillaMaterials.DIRT, (short) 0, w);
		switch (shape) {
			case FLAT:
				generateHugeFlatMushroom(w, x, y, z);
				return;
			case ROUND:
				generateHugeRoundMushroom(w, x, y, z);
		}
	}

	private void generateHugeFlatMushroom(World world, int x, int y, int z) {
		final int capYStart = totalHeight + y + 1 - capThickness;
		final byte capSize = capRadius;
		for (int yy = capYStart; yy < y + 1 + totalHeight; yy++) { // generate cap
			for (int xx = x - capSize; xx < x + 1 + capSize; xx++) {
				for (int zz = z - capSize; zz < z + 1 + capSize; zz++) {
					if (Math.abs(x - xx) == capSize && Math.abs(z - zz) == capSize) {
						continue;
					}
					final short data;
					if (useTextureMetadata) {
						data = getFlatMushroomCapData(x, y, z, xx, yy, zz, totalHeight, capSize);
					} else {
						data = 0;
					}
					world.getBlock(xx, yy, zz).setMaterial(capMaterial, data);
				}
			}
		}
		final short data = useTextureMetadata ? (short) 10 : (short) 0;
		for (int yy = y; yy < capYStart; yy++) { // generate stem
			world.getBlock(x, yy, z).setMaterial(stemMaterial, data);
		}
	}

	private void generateHugeRoundMushroom(World world, int x, int y, int z) {
		final int capYStart = totalHeight + y + 1 - capThickness;
		byte capSize = capRadius;
		for (int yy = capYStart; yy < y + 1 + totalHeight; yy++) { // generate cap
			if (yy == y + totalHeight) {
				capSize--;
			}
			for (int xx = x - capSize; xx < x + 1 + capSize; xx++) {
				for (int zz = z - capSize; zz < z + 1 + capSize; zz++) {
					if (yy != y + totalHeight) {
						int xDif = Math.abs(x - xx);
						int zDif = Math.abs(z - zz);
						if ((xDif < capSize && zDif < capSize) || (xDif == capSize && zDif == capSize)) {
							continue;
						}
					}
					final short data;
					if (useTextureMetadata) {
						data = getRoundMushroomCapData(x, y, z, xx, yy, zz, totalHeight, capSize);
					} else {
						data = 0;
					}
					world.getBlock(xx, yy, zz).setMaterial(capMaterial, data);
				}
			}
		}
		final short data = useTextureMetadata ? (short) 10 : (short) 0;
		for (int yy = y; yy < totalHeight + y; yy++) { // generate stem
			world.setBlockMaterial(x, yy, z, stemMaterial, data, world);
		}
	}

	private short getFlatMushroomCapData(int x, int y, int z, int xx, int yy, int zz, int height, int capSize) {
		if (Math.abs(x - xx) < capSize && Math.abs(z - zz) < capSize) { // interior blocks
			if (yy < y + height) { // bottom
				return 0;
			} else { // top
				return 5;
			}
		} else if (x - xx == capSize) { // exterior west blocks
			if (z - zz == capSize - 1) { // west & north
				return 1;
			} else if (z - zz == -capSize + 1) { // west & south
				return 7;
			} else { // west only
				return 4;
			}
		} else if (x - xx == -capSize) { // exterior east blocks
			if (z - zz == capSize - 1) { // east & north
				return 3;
			} else if (z - zz == -capSize + 1) { // east & south
				return 9;
			} else { // east only
				return 6;
			}
		} else if (z - zz == capSize) { // exterior north
			if (x - xx == capSize - 1) { // west & north
				return 1;
			} else if (x - xx == -capSize + 1) { // east & north
				return 3;
			} else { // north only
				return 2;
			}
		} else if (z - zz == -capSize) { // exterior south
			if (x - xx == capSize - 1) { // west & south
				return 7;
			} else if (x - xx == -capSize + 1) { // east & south
				return 9;
			} else { // north only
				return 8;
			}
		} else { // unknown location
			return 0;
		}
	}

	private short getRoundMushroomCapData(int x, int y, int z, int xx, int yy, int zz, int height, int capSize) {
		if (yy == y + height) { // top blocks
			if (Math.abs(x - xx) < capSize && Math.abs(z - zz) < capSize) { // interior blocks
				return 5;
			} else if (x - xx == capSize) { // exterior west blocks
				if (z - zz == capSize) { // west & north
					return 1;
				} else if (z - zz == -capSize) { // west & south
					return 7;
				} else { // west only
					return 4;
				}
			} else if (x - xx == -capSize) { // exterior east blocks
				if (z - zz == capSize) { // east & north
					return 3;
				} else if (z - zz == -capSize) { // east & south
					return 9;
				} else { // east only
					return 6;
				}
			} else if (z - zz == capSize) { // exterior north only
				return 2;
			} else if (z - zz == -capSize) { // exterior south only
				return 8;
			} else { // unknown location
				return 0;
			}
		} else { // bottom blocks
			if (x - xx == capSize) { // exterior west blocks
				if (z - zz == capSize - 1) { // west & north
					return 1;
				} else if (z - zz == -capSize + 1) { // west & south
					return 7;
				} else { // west only
					return 4;
				}
			} else if (x - xx == -capSize) { // exterior east blocks
				if (z - zz == capSize - 1) { // east & north
					return 3;
				} else if (z - zz == -capSize + 1) { // east & south
					return 9;
				} else { // east only
					return 6;
				}
			} else if (z - zz == capSize) { // exterior north
				if (x - xx == capSize - 1) { // west & north
					return 1;
				} else if (x - xx == -capSize + 1) { // east & north
					return 3;
				} else { // north only
					return 2;
				}
			} else if (z - zz == -capSize) { // exterior south
				if (x - xx == capSize - 1) { // west & south
					return 7;
				} else if (x - xx == -capSize + 1) { // east & south
					return 9;
				} else { // north only
					return 8;
				}
			} else { // unknown location
				return 0;
			}
		}
	}

	private void checkIfUseTextureMetadata() {
		useTextureMetadata = (capMaterial instanceof MushroomBlock && stemMaterial instanceof MushroomBlock);
	}

	public void setCapRadius(byte capRadius) {
		this.capRadius = capRadius;
	}

	public void setCapThickness(byte capThickness) {
		this.capThickness = capThickness;
	}

	public void setFinCheckRadius(byte finCheckRadius) {
		this.finCheckRadius = finCheckRadius;
	}

	public void setInitCheckRadius(byte initCheckRadius) {
		this.initCheckRadius = initCheckRadius;
	}

	public void setCapMaterial(BlockMaterial material) {
		this.capMaterial = material;
		checkIfUseTextureMetadata();
	}

	public void setStemMaterial(BlockMaterial material) {
		this.stemMaterial = material;
		checkIfUseTextureMetadata();
	}

	public void setShape(HugeMushroomShape shape) {
		this.shape = shape;
	}

	public Set<BlockMaterial> getOverridableMaterials() {
		return overridable;
	}

	public static enum HugeMushroomType {

		RED(HugeMushroomShape.ROUND, VanillaMaterials.HUGE_RED_MUSHROOM, (byte) 2, (byte) 4),
		BROWN(HugeMushroomShape.FLAT, VanillaMaterials.HUGE_BROWN_MUSHROOM, (byte) 3, (byte) 1);
		//
		private final HugeMushroomShape shape;
		private final BlockMaterial material;
		private final byte capRadius;
		private final byte capThickness;

		private HugeMushroomType(HugeMushroomShape shape, BlockMaterial material, byte capRadius, byte capThickness) {
			this.shape = shape;
			this.material = material;
			this.capRadius = capRadius;
			this.capThickness = capThickness;
		}

		public byte getCapRadius() {
			return capRadius;
		}

		public byte getCapThickness() {
			return capThickness;
		}

		public HugeMushroomShape getShape() {
			return shape;
		}

		public BlockMaterial getMaterial() {
			return material;
		}
	}

	public static enum HugeMushroomShape {

		FLAT, ROUND;
	}
}
