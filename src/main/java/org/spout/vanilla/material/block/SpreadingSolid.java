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
package org.spout.vanilla.material.block;

import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.RandomBlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.CubicEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.IntVector3;
import org.spout.vanilla.material.VanillaBlockMaterial;

/**
 * A solid material that can spread to other materials nearby
 */
public abstract class SpreadingSolid extends Solid implements Spreading, RandomBlockMaterial {
	private BlockMaterial replacedMaterial;
	private static final EffectRange DEFAULT_SPREAD_RANGE = new CubicEffectRange(2);

	public SpreadingSolid(short dataMask, String name, int id) {
		super(dataMask, name, id);
	}

	public SpreadingSolid(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, id, data, parent);
	}

	public SpreadingSolid(String name, int id) {
		super(name, id);
	}

	/**
	 * Sets the material replaced by this spreading solid material
	 * 
	 * @param material to set to
	 * @return this spreading solid material
	 */
	public SpreadingSolid setReplacedMaterial(BlockMaterial material) {
		this.replacedMaterial = material;
		return this;
	}

	/**
	 * Gets the material replaced by this spreading solid material
	 * 
	 * @return replacement material
	 */
	public BlockMaterial getReplacedMaterial() {
		return this.replacedMaterial;
	}

	/**
	 * Tests if the block can decay at the block specified<br><br>
	 * 
	 * <b>Note: This should not operate on the block itself, 
	 * as the block is not necessarily this material</b>
	 * 
	 * @param block of this Spreading solid
	 * @return True if it can decay, False if not
	 */
	public abstract boolean canDecayAt(Block block);

	/**
	 * Tests if the block can spread from the block specified
	 * 
	 * @param block of this Spreading solid
	 * @return True if it can spread, False if not
	 */
	public boolean canSpreadAt(Block block) {
		return block.translate(BlockFace.TOP).getLight() >= this.getMinimumLightToSpread();
	}

	/**
	 * Attempts to decay this material at the block specified
	 * 
	 * @param block of this material
	 */
	public void onDecay(Block block) {
		block.setMaterial(this.replacedMaterial);
	}

	/**
	 * Attempts to spread this material at the block specified
	 * 
	 * @param block of this material
	 */
	public void onSpread(Block block) {
		Block around;
		final Random rand = new Random(block.getWorld().getAge());
		for (IntVector3 next : this.getSpreadRange()) {
			if (rand.nextInt(4) == 0) {
				around = block.translate(next);
				if (around.isMaterial(this.replacedMaterial)) {
					if (!this.canDecayAt(around)) {
						around.setMaterial(this);
					}
				}
			}
		}
	}

	/**
	 * Gets the block range within this block can spread
	 * 
	 * @return spread range
	 */
	public EffectRange getSpreadRange() {
		return DEFAULT_SPREAD_RANGE;
	}

	@Override
	public void onRandomTick(Block block) {
		// Attempt to decay or spread this material
		if (this.canDecayAt(block)) {
			this.onDecay(block);
		} else if (this.canSpreadAt(block)) {
			this.onSpread(block);
		}
	}
}
