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
package org.spout.vanilla.plugin.material.block.misc;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.PlusEffectRange;

import org.spout.vanilla.plugin.data.RedstonePowerMode;
import org.spout.vanilla.plugin.data.effect.store.GeneralEffects;
import org.spout.vanilla.plugin.material.Toggleable;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.AttachedRedstoneSource;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;

public class TripWireHook extends AttachedRedstoneSource implements Toggleable, DynamicMaterial {
	private static final EffectRange dynamicRange = new PlusEffectRange(TripWire.MAX_DISTANCE, false);
	private static final long TICK_DELAY = 500;

	public TripWireHook(String name, int id) {
		super(name, id, VanillaMaterialModels.TRIP_WIRE_HOOK);
		this.setAttachable(BlockFaces.NESW);
		this.setHardness(0.0f).setResistance(0.0f).setTransparent();
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		boolean hasHook = VanillaMaterials.TRIPWIRE.findHook(block, getAttachedFace(block).getOpposite()) != null;
		if (!hasHook && !this.isToggled(block) && block.isDataBitSet(0x4)) {
			// play sound of wire snapping
			GeneralEffects.TRIPWIRE_SNAP.playGlobal(block.getPosition());
		}
		block.setDataBits(0x4, hasHook);
	}

	@Override
	public boolean isToggled(Block block) {
		return block.isDataBitSet(0x8);
	}

	@Override
	public void setToggled(Block block, boolean toggled) {
		if (this.isToggled(block) != toggled) {
			block.setDataBits(0x8, toggled);
			GeneralEffects.BLOCK_PRESS.playGlobal(block.getPosition(), toggled);
		}
		block.resetDynamic();
	}

	@Override
	public boolean toggle(Block block) {
		boolean toggled = !this.isToggled(block);
		this.setToggled(block, toggled);
		return toggled;
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		block.setDataField(0x3, BlockFaces.ESWN.indexOf(attachedFace, 0));
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.ESWN.get(data & 0x3);
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isToggled(block);
	}

	@Override
	public EffectRange getDynamicRange() {
		return dynamicRange;
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
		b.dynamicUpdate(currentTime + TICK_DELAY, true);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		if (this.isToggled(block)) {
			BlockFace direction = getAttachedFace(block).getOpposite();
			this.setToggled(block, false);
			block = VanillaMaterials.TRIPWIRE.findHook(block, direction);
			if (block != null) {
				this.setToggled(block, false);
			}
		}
	}

	@Override
	public short getRedstonePowerStrength(short data) {
		return ((data & 0x8) == 1) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}
}
