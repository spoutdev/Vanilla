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

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.plugin.data.GameMode;
import org.spout.vanilla.plugin.data.RedstonePowerMode;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.data.effect.store.GeneralEffects;
import org.spout.vanilla.plugin.material.block.AttachedRedstoneSource;
import org.spout.vanilla.plugin.material.block.attachable.PointAttachable;

public class StoneButton extends AttachedRedstoneSource implements PointAttachable, DynamicMaterial {
	public static final int TICK_DELAY = 1000;

	public StoneButton(String name, int id) {
		super(name, id, (String) null);
		this.setAttachable(BlockFaces.NESW).setLiquidObstacle(false).setHardness(0.5F).setResistance(0.8F).setTransparent();
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isPressed(block);
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		return face != BlockFace.TOP && super.canAttachTo(block, face);
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (oldMaterial == this && this.isPressed(block)) {
			block.dynamicUpdate(block.getWorld().getAge() + TICK_DELAY, true);
		}
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type != Action.LEFT_CLICK || !entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.CREATIVE)) {
			this.setPressed(block, true);
		}
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		block.setData((short) (BlockFaces.NSEW.indexOf(attachedFace, 3) + 1), cause);
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.NSEW.get((data & 0x7) - 1);
	}

	public boolean isPressed(Block block) {
		return block.isDataBitSet(0x8);
	}

	public void setPressed(Block block, boolean pressed) {
		if (this.isPressed(block) != pressed) {
			block.setDataBits(0x8, pressed);
			GeneralEffects.BLOCK_PRESS.playGlobal(block.getPosition(), pressed);
		}
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		this.setPressed(block, false);
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}

	@Override
	public short getRedstonePowerStrength(short data) {
		return ((data & 0x8) == 1) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}
}
