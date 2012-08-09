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
package org.spout.vanilla.material.block.solid;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.entity.object.moving.PrimedTnt;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.util.RedstoneUtil;

public class TNT extends Solid implements Mineable, RedstoneTarget, Burnable {
	public TNT(String name, int id) {
		super(name, id);
		this.setHardness(0.0F).setResistance(0.0F).setOpacity((byte) 1);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public int getBurnPower() {
		return 15;
	}

	@Override
	public int getCombustChance() {
		return 100;
	}

	@Override
	public boolean canSupport(BlockMaterial mat, BlockFace face) {
		return mat.equals(VanillaMaterials.FIRE);
	}

	@Override
	public void onIgnite(Block block) {
		block.setMaterial(VanillaMaterials.AIR);
		// spawn a primed TNT
		Point point = block.getPosition();
		point.getWorld().createAndSpawnEntity(point, new PrimedTnt());
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (this.isReceivingPower(block)) {
			this.onIgnite(block);
		}
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}
}
