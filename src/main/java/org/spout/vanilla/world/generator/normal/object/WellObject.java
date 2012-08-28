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
package org.spout.vanilla.world.generator.normal.object;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.misc.Slab;

public class WellObject extends WorldGeneratorObject {
	// main materials
	private BlockMaterial main = VanillaMaterials.SANDSTONE;
	private BlockMaterial slab = Slab.SANDSTONE;
	private BlockMaterial liquid = VanillaMaterials.STATIONARY_WATER;
	// material we can place it on
	private BlockMaterial placeableOn = VanillaMaterials.SAND;
	// materials we can override
	private final Set<BlockMaterial> overridable = new HashSet<BlockMaterial>();

	public WellObject() {
		overridable.add(VanillaMaterials.AIR);
		overridable.add(VanillaMaterials.SAND);
		overridable.add(VanillaMaterials.SANDSTONE);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		y -= 1;
		if (w.getBlockMaterial(x, y, z) != placeableOn) {
			return false;
		}
		for (int xx = x - 2; xx < x + 3; xx++) {
			for (int zz = z - 2; zz < z + 3; zz++) {
				final Block block = w.getBlock(xx, y - 1, zz, w);
				if (!overridable.contains(block.getMaterial()) || !overridable.contains(block.translate(BlockFace.BOTTOM).getMaterial())) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		y -= 1;
		for (int xx = x - 2; xx < x + 3; xx++) {
			for (int zz = z - 2; zz < z + 3; zz++) {
				for (int yy = y - 1; yy < y + 5; yy++) {
					if (yy > y - 2 && yy < y + 1) {
						w.setBlockMaterial(xx, yy, zz, main, (short) 0, w);
					}
					if (yy == y) {
						if ((xx == x && z == zz) || ((xx == x - 1 || xx == x + 1) && zz == z) || ((zz == z + 1 || zz == z - 1) && x == xx)) {
							w.setBlockMaterial(xx, yy, zz, liquid, (short) 0, w);
						}
					}
					if (yy == y + 1) {
						if (xx == x - 2 || xx == x + 2 || zz == z - 2 || zz == z + 2) {
							w.setBlockMaterial(xx, yy, zz, main, (short) 0, w);
						}
						if (((xx == x - 2 || xx == x + 2) && zz == z) || ((zz == z + 2 || zz == z - 2) && x == xx)) {
							w.setBlockMaterial(xx, yy, zz, slab, slab.getData(), w);
						}
					}
					if (yy == y + 4 && xx > x - 2 && xx < x + 2 && zz > z - 2 && zz < z + 2) {
						if (xx == x && zz == z) {
							w.setBlockMaterial(xx, yy, zz, main, (short) 0, w);
						} else {
							w.setBlockMaterial(xx, yy, zz, slab, slab.getData(), w);
						}
					}
					if (yy > y && yy < y + 4) {
						if ((xx == x - 1 || xx == x + 1) && (zz == z + 1 || zz == z - 1)) {
							w.setBlockMaterial(xx, yy, zz, main, (short) 0, w);
						}
					}
				}
			}
		}
	}

	public void setLiquid(BlockMaterial liquid) {
		this.liquid = liquid;
	}

	public void setMain(BlockMaterial main) {
		this.main = main;
	}

	public void setPlaceableOn(BlockMaterial placeableOn) {
		this.placeableOn = placeableOn;
	}

	public void setSlab(BlockMaterial slab) {
		this.slab = slab;
	}

	public Set<BlockMaterial> getOverridableMaterials() {
		return overridable;
	}
}
