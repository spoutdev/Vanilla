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
package org.spout.vanilla.material.block.component;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Slot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.component.block.material.Dropper;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.PlayerUtil;
import org.spout.vanilla.util.RedstoneUtil;

public class DropperBlock extends ComponentMaterial implements Directional, RedstoneTarget {
	public static final BlockFaces BTEWNS = new BlockFaces(BlockFace.BOTTOM, BlockFace.TOP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);

	public DropperBlock(String name, int id) {
		super(name, id, Dropper.class, VanillaMaterialModels.DROPPER);
		this.setHardness(3.5F).setResistance(5.8F);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		Dropper dropper = block.get(Dropper.class);
		if (!dropper.isPowered() && this.isReceivingPower(block)) {
			shootItem(block, dropper.getInventory().getFirstUsedSlot());
		}
		dropper.setPowered(this.isReceivingPower(block));
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BTEWNS.get(block.getData() & 0x7);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData(BTEWNS.indexOf(facing, 1));
	}

	/**
	 * Shoots an item from the inventory slot of the Dropper
	 * @param block of the Dropper
	 * @param slot to fire
	 */
	public boolean shootItem(Block block, Slot slot) {
		if (slot == null) {
			GeneralEffects.RANDOM_CLICK2.playGlobal(block.getPosition());
			return false;
		}
		Block facingBlock = block.translate(this.getFacing(block));
		if (!facingBlock.getMaterial().isSolid()) {
			Item item = facingBlock.getWorld().createEntity(facingBlock.getPosition(), Item.class).add(Item.class);
			item.setItemStack(slot.get().clone());
			item.getItemStack().setAmount(1);
			facingBlock.getWorld().spawnEntity(item.getOwner());

			GeneralEffects.RANDOM_CLICK1.playGlobal(block.getPosition());
			GeneralEffects.SMOKE.playGlobal(facingBlock.getPosition());
			slot.addAmount(-1);
			return true;
		}
		return false;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setFacing(block, PlayerUtil.getFacing(cause).getOpposite());
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material == VanillaMaterials.VINES
				&& face != BlockFace.TOP && face != BlockFace.BOTTOM) {
			return true;
		}
		return super.canSupport(material, face);
	}
}
