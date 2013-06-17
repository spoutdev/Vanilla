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
package org.spout.vanilla.material.block.misc;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.cause.EntityCause;
import org.spout.api.event.player.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.block.BlockSnapshot;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.RedstonePowerMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.Toggleable;
import org.spout.vanilla.material.block.AttachedRedstoneSource;

public class Lever extends AttachedRedstoneSource implements Toggleable {
	public Lever(String name, int id) {
		super(name, id, VanillaMaterialModels.LEVER);
		this.setAttachable(BlockFaces.NESWBT).setLiquidObstacle(false).setHardness(0.5F).setResistance(1.7F).setTransparent();
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action action, BlockFace clickedFace) {
		super.onInteractBy(entity, block, action, clickedFace);
		if (action == Action.LEFT_CLICK && entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.CREATIVE)) {
			return;
		}
		this.toggle(block);
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isToggled(block) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
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
	}

	@Override
	public boolean toggle(Block block) {
		boolean toggled = !this.isToggled(block);
		this.setToggled(block, toggled);
		return toggled;
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		short data;
		final boolean bottom = attachedFace == BlockFace.BOTTOM;
		if (bottom || attachedFace == BlockFace.TOP) {
			if (bottom) {
				data = 5;
			} else {
				data = 7;
			}
			// Add +1 to change direction
			if (cause instanceof EntityCause) {
				// set data using direction
				Vector3 direction = block.getPosition().subtract((((EntityCause) cause).getSource()).getScene().getPosition());
				direction = direction.abs();
				if (direction.getX() > direction.getZ()) {
					data++;
				}
			} else {
				data += (int) Math.round(Math.random());
			}
			// Wrap 0x8 around as 0x0
			data &= 0x7;
		} else {
			data = (short) (BlockFaces.NSEW.indexOf(attachedFace, 0) + 1);
		}
		block.setDataField(0x7, data);
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		final int bits = data & 0x7;
		if (bits == 0 || bits == 7) {
			return BlockFace.TOP;
		} else if (bits == 5 || bits == 6) {
			return BlockFace.BOTTOM;
		} else {
			return BlockFaces.NSEW.get(bits - 1);
		}
	}

	@Override
	public short getRedstonePowerStrength(BlockSnapshot state) {
		return ((state.getData() & 0x8) == 1) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}
}
