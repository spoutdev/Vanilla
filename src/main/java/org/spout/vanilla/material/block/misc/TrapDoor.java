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

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.block.Openable;
import org.spout.vanilla.material.block.attachable.AbstractAttachable;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.RedstoneUtil;

public class TrapDoor extends AbstractAttachable implements Fuel, Openable, RedstoneTarget {
	public final float BURN_TIME = 15.f;

	public TrapDoor(String name, int id) {
		super(name, id);
		this.setAttachable(BlockFaces.NESW).setHardness(3.0F).setResistance(5.0F).setTransparent();
		this.setCollision(CollisionStrategy.SOLID);
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (!(oldMaterial instanceof TrapDoor) && block.getMaterial().equals(this)) {
			boolean powered = this.isReceivingPower(block);
			if (powered != this.isOpen(block)) {
				this.setOpen(block, powered);
				GeneralEffects.DOOR.playGlobal(block.getPosition(), powered);
			}
		}
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		toggleOpen(block);
		if (entity instanceof Player) {
			GeneralEffects.DOOR.playGlobal(block.getPosition(), this.isOpen(block), (Player) entity);
		} else {
			GeneralEffects.DOOR.playGlobal(block.getPosition(), this.isOpen(block));
		}
	}

	@Override
	public void toggleOpen(Block block) {
		block.setData(block.getData() ^ 0x4);
	}

	@Override
	public void setOpen(Block block, boolean open) {
		block.setDataBits(0x4, open);
	}

	@Override
	public boolean isOpen(Block block) {
		return block.isDataBitSet(0x4);
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace) {
		block.setData((short) BlockFaces.WESN.indexOf(attachedFace, 0));
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.WESN.get(data & 0x3);
	}
}
