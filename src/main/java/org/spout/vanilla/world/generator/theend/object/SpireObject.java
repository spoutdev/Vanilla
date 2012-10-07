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
package org.spout.vanilla.world.generator.theend.object;

import java.util.Random;

import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.component.substance.object.EnderCrystal;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.object.LargePlantObject;

public class SpireObject extends LargePlantObject {
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
		final byte diameter = getDiameter();
		final short radiusSquare = getRadiusSquare();
		for (byte xx = 0; xx < diameter; xx++) {
			for (byte zz = 0; zz < diameter; zz++) {
				if (xx * xx + zz * zz <= radiusSquare) {
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
		x -= totalRadius;
		z -= totalRadius;
		final byte diameter = getDiameter();
		final short radiusSquare = getRadiusSquare();
		for (byte xx = (byte) -totalRadius; xx <= diameter; xx++) {
			for (byte zz = (byte) -totalRadius; zz <= diameter; zz++) {
				if (xx * xx + zz * zz <= radiusSquare) {
					for (byte yy = 0; yy < totalHeight; yy++) {
						w.setBlockMaterial(x + xx, y + yy, z + zz, main, (short) 0, w);
					}
				}
			}
		}
		w.setBlockMaterial(x, y + totalHeight, z, crystalBase, (short) 0, w);
		if (spawnEnderCrystal) {
			w.createAndSpawnEntity(new Point(w, x + 0.5f, y + totalHeight - 1, z + 0.5f), EnderCrystal.class, LoadOption.NO_LOAD);
		}
	}

	@Override
	public final void randomize() {
		totalHeight = (byte) (baseHeight + random.nextInt(randomHeight));
		totalRadius = (byte) (baseRadius + random.nextInt(randRadius));
	}

	private byte getDiameter() {
		return (byte) (totalRadius * 2);
	}

	private short getRadiusSquare() {
		return (short) (totalRadius * totalRadius + 1);
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
