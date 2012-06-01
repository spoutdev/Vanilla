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
import java.util.Random;
import java.util.Set;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.material.BlockMaterial;

public abstract class TreeObject extends WorldGeneratorObject {
	// random number generation
	protected final Random random;
	// size control
	protected byte baseHeight;
	protected byte randomHeight;
	protected byte totalHeight;
	// metadata control
	protected short leavesMetadata;
	protected short logMetadata;
	// for canPlaceObject check
	protected final Set<BlockMaterial> overridable = new HashSet<BlockMaterial>();

	protected TreeObject(Random random, byte baseHeight, byte randomHeight, short metadata) {
		this(random, baseHeight, randomHeight, metadata, metadata);
	}

	protected TreeObject(Random random, byte baseHeight, byte randomHeight, short leavesMetadata, short logMetadata) {
		this.random = random;
		this.baseHeight = baseHeight;
		this.randomHeight = randomHeight;
		this.leavesMetadata = leavesMetadata;
		this.logMetadata = logMetadata;
		randomizeHeight();
	}

	public final void randomizeHeight() {
		totalHeight = (byte) (baseHeight + random.nextInt(randomHeight));
	}

	public void setLeavesMetadata(short leavesMetadata) {
		this.leavesMetadata = leavesMetadata;
	}

	public void setLogMetadata(short logMetadata) {
		this.logMetadata = logMetadata;
	}

	public void setBaseHeight(byte baseHeight) {
		this.baseHeight = baseHeight;
		randomizeHeight();
	}

	public void setRandomHeight(byte randHeight) {
		this.randomHeight = randHeight;
		randomizeHeight();
	}

	public void setTotalHeight(byte height) {
		this.totalHeight = height;
	}

	public Set<BlockMaterial> getOverridableMaterials() {
		return overridable;
	}
}
