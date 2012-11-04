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
package org.spout.vanilla.material.block.misc;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.cause.EntityCause;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.RedstonePowerMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.material.Toggleable;
import org.spout.vanilla.material.block.AttachedRedstoneSource;

public class Lever extends AttachedRedstoneSource implements Toggleable {
	public Lever(String name, int id) {
		super(name, id, (String)null);
		this.setAttachable(BlockFaces.NESWB).setLiquidObstacle(false).setHardness(0.5F).setResistance(1.7F).setTransparent();
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
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isToggled(block);
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
		if (attachedFace == BlockFace.BOTTOM) {
			data = (short) (5 + Math.random());
			if (cause instanceof EntityCause) {
				// set data using direction
				Vector3 direction = block.getPosition().subtract((((EntityCause) cause).getSource()).getTransform().getPosition());
				direction = direction.abs();
				if (direction.getX() > direction.getZ()) {
					data = 6;
				} else {
					data = 5;
				}
			}
		} else {
			data = (short) (BlockFaces.NSEW.indexOf(attachedFace, 0) + 1);
		}
		block.setData(data);
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.NSEWB.get((data & 0x7) - 1);
	}
}
