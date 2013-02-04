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
package org.spout.vanilla.plugin.world.generator.theend.object;

import java.util.Random;

import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.plugin.component.substance.object.EnderCrystal;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.world.generator.object.VariableHeightObject;

public class SpireObject extends VariableHeightObject {
	// These control the radius of the spires
	private byte baseRadius = 2;
	private byte randRadius = 4;
	private byte totalRadius;
	// materials
	private BlockMaterial main = VanillaMaterials.OBSIDIAN;
	private BlockMaterial crystalBase = VanillaMaterials.BEDROCK;
	// extras
	private boolean spawnEnderCrystal = true;

	public SpireObject() {
		this(null);
	}

	public SpireObject(Random random) {
		super(random, (byte) 6, (byte) 32);
		randomize();
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		for (byte xx = (byte) -totalRadius; xx <= totalRadius; xx++) {
			for (byte zz = (byte) -totalRadius; zz <= totalRadius; zz++) {
				if (xx * xx + zz * zz <= totalRadius * totalRadius + 1) {
					if (w.getBlockMaterial(x + xx, y - 1, z + zz) == VanillaMaterials.AIR) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		for (byte xx = (byte) -totalRadius; xx <= totalRadius; xx++) {
			for (byte zz = (byte) -totalRadius; zz <= totalRadius; zz++) {
				if (xx * xx + zz * zz <= totalRadius * totalRadius + 1) {
					for (byte yy = 0; yy < totalHeight; yy++) {
						w.setBlockMaterial(x + xx, y + yy, z + zz, main, (short) 0, null);
					}
				}
			}
		}
		w.setBlockMaterial(x, y + totalHeight, z, crystalBase, (short) 0, null);
		if (spawnEnderCrystal) {
			w.createAndSpawnEntity(new Point(w, x + 0.5f, y + totalHeight, z + 0.5f), EnderCrystal.class, LoadOption.NO_LOAD);
		}
	}

	@Override
	public final void randomize() {
		totalHeight = (byte) (baseHeight + random.nextInt(randomHeight));
		totalRadius = (byte) (baseRadius + random.nextInt(randRadius));
	}

	public void setBaseRadius(byte baseRadius) {
		this.baseRadius = baseRadius;
	}

	public void setEnderCrystalBaseMaterial(BlockMaterial crystalBase) {
		this.crystalBase = crystalBase;
	}

	public void setMainMaterial(BlockMaterial main) {
		this.main = main;
	}

	public void setRandRadius(byte randRadius) {
		this.randRadius = randRadius;
	}

	public void spawnEnderCrystal(boolean spawnEnderCrystal) {
		this.spawnEnderCrystal = spawnEnderCrystal;
	}

	public void setTotalRadius(byte totalRadius) {
		this.totalRadius = totalRadius;
	}
}
