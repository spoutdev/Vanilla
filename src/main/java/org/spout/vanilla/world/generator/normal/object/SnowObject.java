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

import java.util.ArrayList;
import java.util.Random;

import org.spout.api.Source;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.IntVector3;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.liquid.Water;
import org.spout.vanilla.world.generator.VanillaBiome;
import org.spout.vanilla.world.generator.object.RandomObject;

public class SnowObject extends RandomObject implements Source {
	private static final int MAX_ITERATIONS = 10; // Endless loop prevention
	private static final EffectRange NEIGHBORS = new CuboidEffectRange(-1, 0, -1, 1, 0, 1);

	public SnowObject() {
		this(null);
	}
	
	public SnowObject(Random random) {
		super(random);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		Biome biome = w.getBiomeType(x, y, z);
		return biome instanceof VanillaBiome && ((VanillaBiome) biome).getClimate().hasSnowfall();
	}

	/**
	 * Processes one iteration of snowfall, this may or may not set a block at
	 * the current position.
	 * 
	 * @returns if snow has been placed.
	 */
	public boolean fall(World world, IntVector3 position) {
		Block current = world.getBlock(position.getX(), position.getY(), position.getZ(), this);
		Block under = current.translate(BlockFace.BOTTOM);
		BlockMaterial underMat = under.getMaterial();
		if (underMat.equals(VanillaMaterials.ICE)) {
			return true;
		} else if (underMat.equals(VanillaMaterials.AIR, VanillaMaterials.SNOW)) {
			position.setY(position.getY() - 1);
			return false;
		} else if (underMat instanceof Water) {
			// Set ice, if it falls on water
			under.setMaterial(VanillaMaterials.ICE);
			return true;
		} else {
			int newSnowHeight = 0;
			if (current.isMaterial(VanillaMaterials.SNOW)) {
				newSnowHeight = current.getData() + 1;
				// In 1 out of 6 times, stack the existing pile.
				if (random.nextInt(6) != 0 && newSnowHeight < 15) {
					// Collect neighbors
					ArrayList<IntVector3> slopes = new ArrayList<IntVector3>();
					for (IntVector3 neighbor : NEIGHBORS) {
						Block n = current.translate(neighbor);
						if (n.isMaterial(VanillaMaterials.AIR) && Climate.get(n).hasSnowfall()) { // probably more types
							slopes.add(neighbor);
						}
					}

					// if there are slopes, move the flake to one of them. 
					if (!slopes.isEmpty()) {
						position.set(slopes.get(random.nextInt(slopes.size())));
						return false;
					}
				}
			}

			// Check if the material can support snow
			if (underMat instanceof VanillaBlockMaterial) {
				if (((VanillaBlockMaterial) underMat).canSupport(VanillaMaterials.SNOW, BlockFace.TOP)) {
					current.setMaterial(VanillaMaterials.SNOW, newSnowHeight);
					return true;
				}
			}
			//move flake randomly
			position.setX(position.getX() + random.nextInt(2) == 0 ? -1 : 1);
			position.setZ(position.getZ() + random.nextInt(2) == 0 ? -1 : 1);
			if (!this.setHighestWorkableBlock(world, position)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Processes one iteration of snowfall, this may or may not set a block at
	 * the current position.
	 * 
	 * @param world to place in
	 * @param x coordinate
	 * @param y coordinate, unused
	 * @param z coordinate
	 * @returns if snow has been placed.
	 */
	@Override
	public void placeObject(World world, int x, int y, int z) {
		IntVector3 position = new IntVector3(x, 0, z);
		if (!this.setHighestWorkableBlock(world, position)) {
			return;
		}
		for (int i = 0; i < MAX_ITERATIONS && !this.fall(world, position); i++);
	}
	
	@Override
	public void randomize() {
	}

	private boolean setHighestWorkableBlock(World world, IntVector3 position) {
		int y = world.getHeight();
		while (world.getBlockMaterial(position.getX(), y, position.getZ()).equals(VanillaMaterials.AIR)) {
			y--;
			if (y == 0) {
				return  false;
			}
		}
		y++;
		y++;
		position.setY(y);
		return true;
	}
}
