/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.block.piston;

import java.util.ArrayList;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.util.LogicUtil;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.RedstoneUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Piston extends VanillaBlockMaterial implements Directional, RedstoneTarget {
	public static final BlockFaces BTEWNS = new BlockFaces(BlockFace.BOTTOM, BlockFace.TOP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);
	private final boolean sticky;

	public Piston(String name, int id, boolean sticky) {
		super(name, id);
		this.sticky = sticky;
		this.setHardness(0.5F).setResistance(0.8F).setOpacity((byte) 0);
	}

	public boolean isSticky() {
		return this.sticky;
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onDestroyBlock(Block block) {
		super.onDestroyBlock(block);
		if (this.isExtended(block)) {
			Block extension = block.translate(this.getFacing(block));
			if (extension.getMaterial() instanceof PistonExtension) {
				extension.setMaterial(VanillaMaterials.AIR).update();
			}
		}
		block.setMaterial(VanillaMaterials.AIR).update();
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		boolean powered = this.isReceivingPower(block);
		if (powered != this.isExtended(block)) {
			this.setExtended(block, powered);
		}
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block, false);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return this.isExtended(block) ? MoveReaction.DENY : MoveReaction.ALLOW;
	}

	/**
	 * Extends or retracts a piston block, complete with the animation and block changes.
	 * @param block    of the piston
	 * @param extended True to extend, False to retract
	 * @return True if the piston really got extended or retracted, False if not
	 */
	public boolean setExtended(Block block, boolean extended) {
		if (extended == this.isExtended(block)) {
			return true;
		} else {
			boolean succ;
			if (extended) {
				succ = this.extend(block);
			} else {
				succ = this.retract(block);
			}
			if (succ) {
				block.setData(LogicUtil.setBit(block.getData(), 0x8, extended));
			}
			return succ;
		}
	}

	public boolean extend(Block block) {
		int length = this.getExtendableLength(block);
		if (length != -1) {
			if (length > 0) {
				block.update();
				BlockFace facing = this.getFacing(block);
				MoveReaction reac;

				//the previous material
				BlockMaterial prevMat = VanillaMaterials.PISTON_EXTENSION;
				short prevData = (short) (block.getData() & 0x7);
				Block previous = block.translate(facing);
				if (this.isSticky()) {
					prevData |= 0x8;
				}

				//temporary values to use while swapping
				BlockMaterial nextMat;
				short nextData;

				Block next; // = block.translate(facing);
				for (int i = 0; i < length; i++) {
					//prepare the next material
					nextMat = previous.getMaterial();
					nextData = previous.getData();
					next = previous.translate(facing);

					reac = this.getReaction(previous);
					if (reac == MoveReaction.BREAK) {
						//break block
						nextMat.getSubMaterial(nextData).onDestroy(previous);
						previous.setMaterial(prevMat, prevData).update();
						break;
					} else if (reac == MoveReaction.ALLOW) {
						//transfer data over and continue
						previous.setMaterial(prevMat, prevData).update();
						previous = next;
						prevMat = nextMat;
						prevData = nextData;
					}
				}
			}
			VanillaNetworkSynchronizer.playBlockAction(block, (byte) 0, (byte) (block.getData() & 0x7));
			return true;
		}
		return false;
	}

	public boolean retract(Block block) {
		BlockFace facing = this.getFacing(block);
		Block next = block.translate(facing);
		if (this.isSticky()) {
			Block from = next.translate(facing);
			if (this.getReaction(from) == MoveReaction.ALLOW) {
				next.setMaterial(from).update();
				next = from;
			}
		}
		next.setMaterial(VanillaMaterials.AIR);
		next.update();
		block.update();
		VanillaNetworkSynchronizer.playBlockAction(block, (byte) 1, (byte) (block.getData() & 0x7));
		return true;
	}

	private MoveReaction getReaction(Block block) {
		BlockMaterial mat = block.getSubMaterial();
		if (mat.equals(VanillaMaterials.AIR)) {
			return MoveReaction.BREAK;
		}
		if (mat instanceof VanillaBlockMaterial) {
			return ((VanillaBlockMaterial) mat).getMoveReaction(block);
		} else {
			return MoveReaction.DENY;
		}
	}

	/**
	 * Gets the amount of blocks this piston material can push
	 * @param block of the piston
	 * @return True if it can extend
	 */
	public int getExtendableLength(Block block) {
		final int maxlength = 13;
		BlockFace face = this.getFacing(block);
		MoveReaction reaction;
		for (int i = 0; i < maxlength; i++) {
			block = block.translate(face);
			reaction = getReaction(block);
			if (reaction == MoveReaction.DENY) {
				return -1; //blocked
			} else if (reaction == MoveReaction.BREAK) {
				return i + 1;
			}
		}
		return -1; //too long
	}

	/**
	 * Gets whether this piston block is extended
	 * @param block to get it of
	 * @return True if extended, False if not
	 */
	public boolean isExtended(Block block) {
		return LogicUtil.getBit(block.getData(), 0x8);
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BTEWNS.get(block.getData() & 0x7);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData(BTEWNS.indexOf(facing, 1));
	}

	public BlockFace getPlacedFacing(Block pistonBlock, Entity entity) {
		Vector3 diff = pistonBlock.getPosition().subtract(entity.getPosition());
		diff = diff.subtract(0.0f, 0.2f, 0.0f);
		Vector3 diffabs = diff.abs();
		if (diffabs.getX() < 2.0f && diffabs.getZ() < 2.0f) {
			if (diff.getY() < 0.0f) {
				return BlockFace.TOP;
			} else if (diff.getY() > 2.0f) {
				return BlockFace.BOTTOM;
			}
		}
		return VanillaPlayerUtil.getFacing(entity).getOpposite();
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			BlockFace facing = BlockFace.TOP;
			if (block.getSource() instanceof Entity) {
				facing = this.getPlacedFacing(block, (Entity) block.getSource());
			}
			this.setFacing(block, facing);
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (sticky) {
			drops.add(new ItemStack(VanillaMaterials.PISTON_STICKY_BASE, block.getData(), 1));
		} else {
			drops.add(new ItemStack(VanillaMaterials.PISTON_BASE, block.getData(), 1));
		}
		return drops;
	}
}
