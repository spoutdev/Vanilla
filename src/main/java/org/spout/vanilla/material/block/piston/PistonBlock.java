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
package org.spout.vanilla.material.block.piston;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.PlusEffectRange;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.PlayerUtil;
import org.spout.vanilla.util.RedstoneUtil;

public class PistonBlock extends VanillaBlockMaterial implements Directional, RedstoneTarget, InitializableMaterial {
	public static final BlockFaces BTEWNS = new BlockFaces(BlockFace.BOTTOM, BlockFace.TOP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);
	public static final int maxLength = 13;
	public static final EffectRange physicsRange = new PlusEffectRange(maxLength, true);
	private final boolean sticky;

	public PistonBlock(String name, int id, boolean sticky) {
		super(name, id, (String) null);
		this.sticky = sticky;
		this.setHardness(0.5F).setResistance(0.8F).setTransparent();
	}

	@Override
	public void initialize() {
		if (this.isSticky()) {
			this.getDrops().DEFAULT.clear().add(VanillaMaterials.PISTON_STICKY_BASE);
		} else {
			this.getDrops().DEFAULT.clear().add(VanillaMaterials.PISTON_BASE);
		}
	}

	public boolean isSticky() {
		return this.sticky;
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public boolean onDestroy(Block block, Cause<?> cause) {
		if (this.isExtended(block)) {
			Block extension = block.translate(this.getFacing(block));
			if (extension.getMaterial() instanceof PistonExtension) {
				extension.setMaterial(VanillaMaterials.AIR, cause);
			}
		}
		return super.onDestroy(block, cause);
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		boolean powered = this.isReceivingPower(block);
		if (powered != this.isExtended(block)) {
			this.setExtended(block, powered);
		}
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return this.isExtended(block) ? MoveReaction.DENY : MoveReaction.ALLOW;
	}

	/**
	 * Extends or retracts a piston block, complete with the animation and block changes.
	 * @param block of the piston
	 * @param extended True to extend, False to retract
	 * @return True if the piston really got extended or retracted, False if not
	 */
	public boolean setExtended(Block block, boolean extended) {
		if (extended == this.isExtended(block)) {
			return true;
		}

		boolean succ;
		if (extended) {
			succ = this.extend(block);
		} else {
			succ = this.retract(block);
		}
		if (succ) {
			block.setDataBits(0x8, extended);
		}
		return succ;
	}

	public boolean extend(Block block) {
		int length = this.getExtendableLength(block);
		if (length != -1) {
			if (length > 0) {
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
						nextMat.getSubMaterial(nextData).destroy(previous, toCause(block));
						previous.setMaterial(prevMat, prevData);
						break;
					} else if (reac == MoveReaction.ALLOW) {
						//transfer data over and continue
						previous.setMaterial(prevMat, prevData);
						previous = next;
						prevMat = nextMat;
						prevData = nextData;
					}
				}
			}
			playBlockAction(block, (byte) 0, (byte) (block.getData() & 0x7));
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
				next.setMaterial(from.getMaterial());
				next = from;
			}
		}
		next.setMaterial(VanillaMaterials.AIR);
		playBlockAction(block, (byte) 1, (byte) (block.getData() & 0x7));
		return true;
	}

	private MoveReaction getReaction(Block block) {
		BlockMaterial mat = block.getMaterial();
		if (mat.equals(VanillaMaterials.AIR)) {
			return MoveReaction.BREAK;
		}
		if (!(mat instanceof VanillaBlockMaterial)) {
			return MoveReaction.DENY;
		}

		return ((VanillaBlockMaterial) mat).getMoveReaction(block);
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

	public EffectRange getUpdateRange() {
		return physicsRange;
	}

	/**
	 * Gets whether this piston block is extended
	 * @param block to get it of
	 * @return True if extended, False if not
	 */
	public boolean isExtended(Block block) {
		return block.isDataBitSet(0x8);
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BTEWNS.get(block.getData() & 0x7);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData(BTEWNS.indexOf(facing, 1));
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		if (super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause)) {
			this.setFacing(block, PlayerUtil.getBlockFacing(block, cause));
			return true;
		}
		return false;
	}
}
