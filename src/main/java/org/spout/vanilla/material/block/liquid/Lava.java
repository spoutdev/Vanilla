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
package org.spout.vanilla.material.block.liquid;

import org.spout.api.Spout;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.material.block.solid.Ice;
import org.spout.vanilla.render.VanillaEffects;

public class Lava extends Liquid {
	public Lava(String name, int id, boolean flowing) {
		super(name, id, flowing, VanillaMaterialModels.LAVA);
		this.setFlowDelay(1500);
		if (Spout.getEngine().getPlatform() == Platform.CLIENT) {
			if (!getModel().getRenderMaterial().getRenderEffects().contains(VanillaEffects.LIQUID)) {
				getModel().getRenderMaterial().addRenderEffect(VanillaEffects.LIQUID);
			}
		}
		//TODO: Allow this to get past the tests
		//this.setFlowDelay(VanillaConfiguration.LAVA_DELAY.getInt());
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		if (!this.isFlowingDown(block)) {
			int level = this.getLevel(block);
			if (level > 0) {
				for (BlockFace face : BlockFaces.NESWT) {
					if (block.translate(face).getMaterial() instanceof Water) {
						if (level == this.getMaxLevel()) {
							block.setMaterial(VanillaMaterials.OBSIDIAN);
						} else {
							block.setMaterial(VanillaMaterials.COBBLESTONE);
						}
						GeneralEffects.LAVA_FIZZ.playGlobal(block.getPosition());
					} else if (block.translate(face).getMaterial() instanceof Ice) {
						block.translate(face).setMaterial(VanillaMaterials.WATER);
					}
				}
			}
		}
		super.onUpdate(oldMaterial, block);
	}

	@Override
	public void onSpread(Block block, int newLevel, BlockFace from) {
		// Check if this block was actually water
		if (block.getMaterial() instanceof Water) {
			if (from == BlockFace.TOP) {
				block.setMaterial(VanillaMaterials.STONE);
				GeneralEffects.LAVA_FIZZ.playGlobal(block.getPosition());
				return;
			}
		}
		super.onSpread(block, newLevel, from);
	}

	@Override
	public byte getLightLevel(short data) {
		return 15;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean isMaterial(Material... materials) {
		for (Material material : materials) {
			if (material instanceof Lava) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getLevel(Block block) {
		if (this.isMaterial(block.getMaterial())) {
			return (6 - (block.getData() & 0x6)) >> 1;
		} else {
			return -1;
		}
	}

	@Override
	public void setLevel(Block block, int level) {
		if (level < 0) {
			block.setMaterial(VanillaMaterials.AIR);
		} else {
			if (level > 3) {
				level = 3;
			}
			block.setDataField(0x7, (3 - level) << 1);
		}
	}

	@Override
	public boolean hasFlowSource() {
		return false;
	}

	@Override
	public Liquid getFlowingMaterial() {
		return VanillaMaterials.LAVA;
	}

	@Override
	public Liquid getStationaryMaterial() {
		return VanillaMaterials.STATIONARY_LAVA;
	}
}
