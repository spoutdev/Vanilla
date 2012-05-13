/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.controller.object.misc.EnderCrystal;
import org.spout.vanilla.material.VanillaMaterials;

public class SpireObject extends WorldGeneratorObject {

	private final Random random;
	// These control the height of the spires
	private byte baseHeight = 6;
	private byte randHeight = 32;
	private byte totalHeight;
	// These control the radius of the spires
	private byte baseRadius = 2;
	private byte randRadius = 4;
	private byte totalRadius;
	// materials
	private BlockMaterial main = VanillaMaterials.OBSIDIAN;
	private BlockMaterial crystalBase = VanillaMaterials.BEDROCK;
	// extras
	private boolean spawnEnderCrystal = true;

	public SpireObject(Random random) {
		this.random = random;
		findNewRandomHeight();
		findNewRandomRadius();
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
			w.createAndSpawnEntity(new Point(w, (float) x + 0.5f, y + totalHeight - 1, (float) z + 0.5f), new EnderCrystal());
		}
	}

	private byte getDiameter() {
		return (byte) (totalRadius * 2);
	}

	private short getRadiusSquare() {
		return (short) (totalRadius * totalRadius + 1);
	}

	public final void findNewRandomHeight() {
		totalHeight = (byte) (baseHeight + random.nextInt(randHeight));
	}

	public final void findNewRandomRadius() {
		totalRadius = (byte) (baseRadius + random.nextInt(randRadius));
	}

	public void setBaseHeight(byte baseHeight) {
		this.baseHeight = baseHeight;
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

	public void setRandHeight(byte randHeight) {
		this.randHeight = randHeight;
	}

	public void setRandRadius(byte randRadius) {
		this.randRadius = randRadius;
	}

	public void spawnEnderCrystal(boolean spawnEnderCrystal) {
		this.spawnEnderCrystal = spawnEnderCrystal;
	}

	public void setTotalHeight(byte totalHeight) {
		this.totalHeight = totalHeight;
	}

	public void setTotalRadius(byte totalRadius) {
		this.totalRadius = totalRadius;
	}
}
