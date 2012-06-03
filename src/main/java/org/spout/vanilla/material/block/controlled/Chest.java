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
package org.spout.vanilla.material.block.controlled;

import java.util.ArrayList;

import org.spout.api.entity.BlockController;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.ItemUtil;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Chest extends ControlledMaterial implements Directional, Fuel, Mineable {
	public final float BURN_TIME = 15.f;

	public Chest(String name, int id) {
		super(VanillaControllerTypes.CHEST, name, id);
		this.setHardness(2.5F).setResistance(4.2F).setTransparent();
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public void onDestroyBlock(Block block) {
		BlockController old = block.getController();
		if (old != null && old instanceof org.spout.vanilla.controller.block.Chest) {
			//Drop items
			ItemStack[] items = ((org.spout.vanilla.controller.block.Chest) old).getInventory().getContents();
			Point position = block.getPosition();
			for (ItemStack item : items) {
				ItemUtil.dropItemNaturally(position, item);
			}
		}
		super.onDestroyBlock(block);
	}

	@Override
	public org.spout.vanilla.controller.block.Chest getController(Block block) {
		return (org.spout.vanilla.controller.block.Chest) super.getController(block);
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Axe ? (short) 1 : (short) 2;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.EWNS.get(block.getData() - 2);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData((short) (BlockFaces.EWNS.indexOf(facing, 0) + 2));
	}

	/**
	 * Gets the other half of a double chest
	 * @param block of the Double chest
	 * @return the other half, or null if there is none
	 */
	public Block getOtherHalf(Block block) {
		for (BlockFace face : BlockFaces.NESW) {
			if (block.translate(face).getMaterial().equals(this)) {
				return block.translate(face);
			}
		}
		return null;
	}

	/**
	 * Gets whether a certain chest block is a double chest
	 * @param block of the Chest
	 * @return True if it is a double chest, False if it is a single chest
	 */
	public boolean isDouble(Block block) {
		return getOtherHalf(block) != null;
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.canPlace(block, data, against, isClickedBlock)) {
			//no surrounding double-chest blocks?
			int count = 0;
			for (BlockFace face : BlockFaces.NESW) {
				if (block.translate(face).getMaterial().equals(this)) {
					count++;
					if (count == 2 || this.isDouble(block.translate(face))) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			BlockFace facing = VanillaPlayerUtil.getFacing(block.getSource()).getOpposite();
			//search for neighbor and align
			Block neigh;
			for (BlockFace face : BlockFaces.NESW) {
				if ((neigh = block.translate(face)).getMaterial().equals(this)) {
					if (face == facing || face == facing.getOpposite()) {
						if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
							facing = BlockFace.WEST;
						} else {
							facing = BlockFace.SOUTH;
						}
					}
					this.setFacing(neigh, facing);
					break;
				}
			}
			this.setFacing(block, facing);
			return true;
		}
		return false;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action action, BlockFace face) {
		if (action == Action.RIGHT_CLICK) {
			Controller controller = entity.getController();
			if (!(controller instanceof VanillaPlayer)) {
				return;
			}

			// Open the chest
			this.getController(block).getInventory().open((VanillaPlayer) controller);
		}
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}
}
