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

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.event.player.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.Openable;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.PlayerUtil;
import org.spout.vanilla.util.RedstoneUtil;

public class FenceGate extends VanillaBlockMaterial implements Openable, RedstoneTarget {
	public FenceGate(String name, int id) {
		super(name, id, VanillaMaterialModels.FENCE_GATE);
		this.setHardness(2.0F).setResistance(3.0F).setTransparent();
		this.setCollision(CollisionStrategy.SOLID);
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
		this.toggleOpen(block);
		if (entity instanceof Player) {
			GeneralEffects.DOOR.playGlobal(block.getPosition(), isOpen(block), (Player) entity);
		} else {
			GeneralEffects.DOOR.playGlobal(block.getPosition(), isOpen(block));
		}
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASS_GUITAR;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (!(oldMaterial instanceof FenceGate) && block.getMaterial().equals(this)) {
			boolean powered = this.isReceivingPower(block);
			if (powered != this.isOpen(block)) {
				this.setOpen(block, powered);
				GeneralEffects.DOOR.playGlobal(block.getPosition(), isOpen(block));
			}
		}
	}

	public BlockFace getFacing(Block block) {
		return BlockFaces.WNES.get(block.getBlockData() & 0x3);
	}

	public void setFacing(Block block, BlockFace facing) {
		short data = (short) (block.getBlockData() & ~0x3);
		data += BlockFaces.WNES.indexOf(facing, 0);
		block.setData(data);
	}

	@Override
	public void toggleOpen(Block block) {
		this.setOpen(block, !this.isOpen(block));
	}

	@Override
	public void setOpen(Block block, boolean open) {
		block.setDataBits(0x4, open);
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		block.setMaterial(this, cause);
		this.setFacing(block, PlayerUtil.getFacing(cause));
	}

	@Override
	public boolean isOpen(Block block) {
		return block.isDataBitSet(0x4);
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}
}
