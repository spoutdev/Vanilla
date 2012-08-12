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

import java.util.EnumMap;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.ListEffectRange;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.attachable.AbstractAttachable;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.util.RedstonePowerMode;

public abstract class AttachedRedstoneSource extends AbstractAttachable implements RedstoneSource {
	private static EnumMap<BlockFace, EffectRange> physicsRanges = new EnumMap<BlockFace, EffectRange>(BlockFace.class);

	static {
		for (BlockFace face : BlockFaces.NESWBT) {
			physicsRanges.put(face, new ListEffectRange(EffectRange.THIS_AND_NEIGHBORS, EffectRange.THIS_AND_NEIGHBORS.translate(face)));
		}
	}

	protected AttachedRedstoneSource(short dataMask, String name, int id) {
		super(dataMask, name, id);
	}

	protected AttachedRedstoneSource(String name, int id) {
		super(name, id);
	}

	public AttachedRedstoneSource(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, id, data, parent);
	}

	@Override
	public abstract boolean hasRedstonePower(Block block, RedstonePowerMode powerMode);

	@Override
	public EffectRange getPhysicsRange(short data) {
		return physicsRanges.get(getAttachedFace(data));
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public short getRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasRedstonePowerTo(block, direction, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) && this.getAttachedFace(block) == direction;
	}
}
