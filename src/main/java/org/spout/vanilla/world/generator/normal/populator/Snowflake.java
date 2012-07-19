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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.ArrayList;
import java.util.Random;


import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;

public class Snowflake implements Source {
	private final World world;
	private int x, y, z;
	private boolean placed = false;
	private final Random random;
	private int iterations = 0;
	private static final int MAX_ITERATIONS = 10; // Endless loop prevention

	public Snowflake(World world, int x, int z, Random random) {
		super();
		this.world = world;
		this.x = x;
		this.y = this.world.getSurfaceHeight(x, z) + 1; // could be changed to
														// world.getHeight() - 1
														// if snowfall is
														// animated we want
														// higher stuff to have
														// snow first
		this.z = z;
		this.random = random;
	}

	/**
	 * Processes one iteration of snowfall, this may or may not set a block at
	 * the current position.
	 * 
	 * @returns if snow has been placed.
	 */
	public boolean fall() {
		iterations ++;
		if (iterations >= MAX_ITERATIONS) {
			placed = true;
			return true;
		}
		Block current = world.getBlock(x, y, z, this);
		Block under = current.translate(BlockFace.BOTTOM);
		if (under.isMaterial(VanillaMaterials.ICE)) {
			placed = true;
		} else if (under
				.isMaterial(VanillaMaterials.AIR, VanillaMaterials.SNOW)) {
			y--;
		} else {
			// Set ice, if it falls on water
			if (under.isMaterial(VanillaMaterials.WATER,
					VanillaMaterials.STATIONARY_WATER)) {
				under.setMaterial(VanillaMaterials.ICE);
				placed = true;
			} else {
				int currentSnowHeight = 0;
				boolean snowed = current.isMaterial(VanillaMaterials.SNOW);
				if (snowed) {
					currentSnowHeight = current.getData();
				}
				
				if (snowed) {
					// Collect neighbors
					Block neighbors[] = new Block[8];
					neighbors[0] = current.translate(BlockFace.NORTH);
					neighbors[1] = current.translate(BlockFace.EAST);
					neighbors[2] = current.translate(BlockFace.SOUTH);
					neighbors[3] = current.translate(BlockFace.WEST);
					neighbors[4] = current.translate(BlockFace.NORTH).translate(BlockFace.EAST); //
					neighbors[5] = current.translate(BlockFace.EAST).translate(BlockFace.SOUTH); //
					neighbors[6] = current.translate(BlockFace.SOUTH).translate(BlockFace.WEST); //
					neighbors[7] = current.translate(BlockFace.WEST).translate(BlockFace.NORTH); //
					ArrayList<Block> slopes = new ArrayList<Block>();
					int slopeCount = 0;
					for (int i = 0; i < 8; i ++) {
						Block n = neighbors[i];
						if (n.isMaterial(VanillaMaterials.AIR)) { // probably more types
							slopes.add(n);
							slopeCount++;
						}
					}
					// if there are slopes, move the flake to one of them. In 1 out of 6 times, stack the existing pile though.
					if (slopeCount > 0 && currentSnowHeight < 15 && random.nextInt(6) != 0) {
						Block selected = slopes.get(random.nextInt(slopeCount));
						x = selected.getX();
						y = selected.getY();
						z = selected.getZ();
						placed = false;
						return placed;
					}
				}
				// stack the existing pile
				if (!placed) {
					if (snowed) {
						currentSnowHeight ++;
					}
					Block below = current.translate(BlockFace.BOTTOM);
					// Check if the material can support snow
					if (below.getMaterial() instanceof VanillaBlockMaterial) {
						VanillaBlockMaterial mat = (VanillaBlockMaterial) below.getMaterial();
						if (mat.canSupport(VanillaMaterials.SNOW, BlockFace.TOP)) {
							current.setMaterial(VanillaMaterials.SNOW, currentSnowHeight);
							placed = true;
						} else {
							//move flake randomly
							int xMove = random.nextInt(2);
							int zMove = random.nextInt(2);
							x += xMove == 0 ? -1 : 1;
							z += zMove == 0 ? -1 : 1;
							y = world.getSurfaceHeight(x, z) + 1;
						}
					}
				}
			}
		}
		return placed;
	}

	public boolean isPlaced() {
		return placed;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
}
