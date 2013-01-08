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
package org.spout.vanilla.plugin.material.block.solid;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CubicEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.model.Model;
import org.spout.api.resource.ResourcePointer;

import org.spout.vanilla.plugin.material.InitializableMaterial;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Solid;
import org.spout.vanilla.plugin.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.plugin.util.RedstoneUtil;

public class RedstoneLamp extends Solid implements InitializableMaterial, RedstoneTarget {
	private final boolean on;
	private final static EffectRange effectRange = new CubicEffectRange(2);
	private static final int HAS_REDSTONE_POWER = 1;
	private static final int HAS_NO_REDSTONE_POWER = 0;

	public RedstoneLamp(String name, int id, boolean on, ResourcePointer<Model> model) {
		super(name, id, model);
		this.on = on;
		// TODO: The resistance is not correct (?)
		this.setHardness(0.3F).setResistance(0.5F);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().add(VanillaMaterials.REDSTONE_LAMP_OFF);
	}

	/**
	 * Whether this redstone lamp block material is in the on state
	 * @return true if on
	 */
	public boolean inOn() {
		return on;
	}

	@Override
	public byte getLightLevel(short data) {
		return on ? (byte) 15 : (byte) 0;
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}

	@Override
	public boolean isRedstoneConductor() {
		return false;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		boolean power = isReceivingPower(block);
		if (power) {
			block.setData(HAS_REDSTONE_POWER);
		} else {
			block.setData(HAS_NO_REDSTONE_POWER);
			for (BlockFace face : BlockFaces.BTEWNS) {
				Block other = block.translate(face);
				if (other.getMaterial() instanceof RedstoneLamp) {
					if (other.getData() == HAS_REDSTONE_POWER) {
						power = true;
						break;
					}
				}
			}
		}
		if (on != power) {
			if (power) {
				block.setMaterial(VanillaMaterials.REDSTONE_LAMP_ON);
			} else {
				block.setMaterial(VanillaMaterials.REDSTONE_LAMP_OFF);
			}
		}
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public EffectRange getPhysicsRange(short data) {
		return effectRange;
	}
}
