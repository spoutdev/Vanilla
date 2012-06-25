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
package org.spout.vanilla.material.block.liquid;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.protocol.msg.PlayEffectMessage.Messages;
import org.spout.vanilla.util.VanillaNetworkUtil;

public class Lava extends Liquid {

	public Lava(String name, int id, boolean flowing) {
		super(name, id, flowing);
	}

	@Override
	public void onUpdate(Block block) {
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
						this.onLavaFizz(block);
					}
				}
			}
		}
		super.onUpdate(block);
	}

	@Override
	public void onSpread(Block block, int newLevel, BlockFace from) {
		// Check if this block was actually water
		if (block.getMaterial() instanceof Water) {
			if (from == BlockFace.TOP) {
				block.setMaterial(VanillaMaterials.STONE);
				this.onLavaFizz(block);
				return;
			}
		}
		super.onSpread(block, newLevel, from);
	}

	/**
	 * Is called after lava changed into a solid because of nearby water<br>
	 * Plays the effect by default
	 */
	public void onLavaFizz(Block block) {
		VanillaNetworkUtil.playBlockEffect(block, null, Messages.RANDOM_FIZZ);
		block = block.translate(BlockFace.TOP);
		for (int i = 0; i < 8; i++) {
			VanillaNetworkUtil.playBlockEffect(block, null, Messages.PARTICLE_SMOKE, PlayEffectMessage.SMOKE_MIDDLE);
		}
	}

	@Override
	public int getTickDelay() {
		return 10;
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
