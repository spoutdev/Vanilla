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
package org.spout.vanilla.world.generator.normal.structure.temple;

import org.spout.vanilla.world.generator.biome.VanillaBiomes;
import org.spout.vanilla.world.generator.normal.NormalGenerator;

import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Quaternion;

import org.spout.vanilla.material.VanillaMaterials;

import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;
import org.spout.vanilla.world.generator.structure.StructureComponent.BoundingBox;

public class Temple extends Structure {
	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final StructureComponent temple = getTemple(w.getBiome(x, y, z));
		if (temple == null) {
			return;
		}
		final BoundingBox boundingBox = temple.getBoundingBox();
		y = getAverageHeight(w, x, z, (int) boundingBox.getXSize(), (int) boundingBox.getZSize());
		temple.setPosition(new Point(w, x, y, z));
		temple.setRotation(new Quaternion(random.nextInt(4) * 90, 0, 1, 0));
		if (temple.canPlace()) {
			temple.place();
		}
	}

	private StructureComponent getTemple(Biome biome) {
		final StructureComponent temple;
		if (biome == VanillaBiomes.DESERT || biome == VanillaBiomes.DESERT_HILLS) {
			temple = new DesertTemple(this);
		} else if (biome == VanillaBiomes.JUNGLE || biome == VanillaBiomes.JUNGLE_HILLS) {
			temple = new JungleTemple(this);
		} else {
			temple = null;
		}
		return temple;
	}

	private int getAverageHeight(World world, int x, int z, int sizeX, int sizeZ) {
		int heightSum = 0;
		for (int xx = 0; xx < sizeX; xx++) {
			for (int zz = 0; zz < sizeZ; zz++) {
				heightSum += getSurfaceHeight(world, x + xx, z + zz);
			}
		}
		return heightSum / (sizeX * sizeZ);
	}

	private int getSurfaceHeight(World w, int x, int z) {
		int y = NormalGenerator.HEIGHT;
		while (!w.getBlockMaterial(x, y, z).isMaterial(
				VanillaMaterials.STONE, VanillaMaterials.GRAVEL,
				VanillaMaterials.DIRT, VanillaMaterials.GRASS,
				VanillaMaterials.SAND, VanillaMaterials.SANDSTONE)) {
			if (--y == 0) {
				return 0;
			}
		}
		return ++y;
	}
}
