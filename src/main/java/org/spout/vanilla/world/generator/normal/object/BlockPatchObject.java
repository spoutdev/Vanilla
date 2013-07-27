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
package org.spout.vanilla.world.generator.normal.object;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.object.RandomObject;
import org.spout.vanilla.world.generator.object.RandomizableObject;

public class BlockPatchObject extends RandomObject implements RandomizableObject {
	// size control
	private byte baseRadius = 5;
	private byte randRadius = 2;
	private byte totalRadius;
	private byte heightRadius = 2;
	// material to place
	private BlockMaterial material;
	private BlockMaterial placeIn = VanillaMaterials.STATIONARY_WATER;
	// materials to be replaced
	private final Set<BlockMaterial> overridable = new HashSet<BlockMaterial>();

	public BlockPatchObject(Random random, BlockMaterial material) {
		super(random);
		this.material = material;
		overridable.add(VanillaMaterials.DIRT);
		overridable.add(VanillaMaterials.GRASS);
		overridable.add(VanillaMaterials.MYCELIUM);
		randomize();
	}

	public BlockPatchObject(BlockMaterial material) {
		this(null, material);
	}

	@Override
	public boolean canPlaceObject(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y, z).isMaterial(placeIn);
	}

	@Override
	public void placeObject(World world, int x, int y, int z) {
		for (byte xx = (byte) -totalRadius; xx <= totalRadius; xx++) {
			for (byte zz = (byte) -totalRadius; zz <= totalRadius; zz++) {
				if (xx * xx + zz * zz <= totalRadius * totalRadius) {
					for (byte yy = (byte) -heightRadius; yy <= heightRadius; yy++) {
						if (overridable.contains(world.getBlockMaterial(x + xx, y + yy, z + zz))
								&& canPlaceBlock(world, x + xx, y + yy, z + zz)) {
							world.setBlockMaterial(x + xx, y + yy, z + zz, material, (short) 0, null);
						}
					}
				}
			}
		}
	}

	private boolean canPlaceBlock(World world, int x, int y, int z) {
		return !world.getBlockMaterial(x, y + 1, z).isMaterial(
				VanillaMaterials.LOG,
				VanillaMaterials.TALL_GRASS,
				VanillaMaterials.FERN,
				VanillaMaterials.DANDELION,
				VanillaMaterials.ROSE);
	}

	@Override
	public final void randomize() {
		totalRadius = (byte) (baseRadius + random.nextInt(randRadius));
	}

	public void setBaseRadius(byte baseRadius) {
		this.baseRadius = baseRadius;
	}

	public void setRandRadius(byte randRadius) {
		this.randRadius = randRadius;
	}

	public void setTotalRadius(byte totalRadius) {
		this.totalRadius = totalRadius;
	}

	public void setHeightRadius(byte heightRadius) {
		this.heightRadius = heightRadius;
	}

	public void setMaterial(BlockMaterial material) {
		this.material = material;
	}

	public void setPlaceIn(BlockMaterial placeIn) {
		this.placeIn = placeIn;
	}

	public Set<BlockMaterial> getOverridableMaterials() {
		return overridable;
	}
}
