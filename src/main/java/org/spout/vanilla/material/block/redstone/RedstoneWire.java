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
package org.spout.vanilla.material.block.redstone;

import java.util.ArrayList;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CubicEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.ListEffectRange;
import org.spout.api.material.range.PlusEffectRange;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.RedstonePowerMode;
import org.spout.vanilla.util.RedstoneUtil;

public class RedstoneWire extends GroundAttachable implements Mineable, RedstoneSource, RedstoneTarget {
	private static final EffectRange physicsRange = new ListEffectRange(
			new PlusEffectRange(2, false),
			new CubicEffectRange(1));
	private static final EffectRange maximumPhysicsRange = new PlusEffectRange(15, true);

	public RedstoneWire(String name, int id) {
		super(name, id);
		this.setLiquidObstacle(false).setHardness(0.0F).setResistance(0.0F).setTransparent();
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	private void disableRedstone(Block middle) {
		Block block;
		for (BlockFace face : BlockFaces.NESWBT) {
			block = middle.translate(face);
			if (block.getMaterial().equals(this)) {
				if (block.getData() > 0) {
					block.setData(0);
					this.disableRedstone(block);
				}
			}
		}
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (!VanillaConfiguration.REDSTONE_PHYSICS.getBoolean()) {
			return;
		}

		if (block.getMaterial().equals(this)) {
			short receiving = this.getReceivingPower(block);
			short current = this.getRedstonePower(block);
			if (current == receiving) {

			} else if (receiving > current) {
				//Power became more, perform simple updating
				block.setMaterial(this, receiving);
			} else {
				//Power became less, disable all attached wires and recalculate
				block = block.setSource(this);
				this.disableRedstone(block);
				for (BlockFace face : BlockFaces.NESWB) {
					this.disableRedstone(block.translate(face));
				}
			}
		}
	}

	@Override
	public boolean hasRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.getRedstonePowerTo(block, direction, powerMode) > 0;
	}

	@Override
	public short getRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		if (powerMode == RedstonePowerMode.ALLEXCEPTWIRE) {
			return REDSTONE_POWER_MIN;
		}

		short power = this.getRedstonePower(block);
		if (power == REDSTONE_POWER_MIN) {
			return power;
		}

		BlockMaterial mat = block.translate(direction).getMaterial();
		if (mat instanceof RedstoneSource || mat instanceof RedstoneTarget || !isDistractedFrom(block, direction)) {
			return power;
		}

		return REDSTONE_POWER_MIN;
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return powerMode == RedstonePowerMode.ALLEXCEPTWIRE ? REDSTONE_POWER_MIN : block.getData();
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return powerMode == RedstonePowerMode.ALLEXCEPTWIRE ? false : block.getData() > 0;
	}

	@Override
	public byte getLightLevel(short data) {
		return (byte) data;
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return this.getReceivingPower(block) > 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(VanillaMaterials.REDSTONE_DUST, 1));
		return drops;
	}

	public short getReceivingPower(Block block) {
		short maxPower = 0;
		Block rel, relvert;
		BlockMaterial mat;
		//detect power from direct neighbouring sources
		boolean topIsConductor = false;
		for (BlockFace face : BlockFaces.BTEWNS) {
			rel = block.translate(face);
			mat = rel.getMaterial();
			if (mat.equals(this)) {
				//handle neighbouring redstone wires
				maxPower = (short) Math.max(maxPower, this.getRedstonePower(rel) - 1);
			} else if (mat instanceof VanillaBlockMaterial) {
				//handle solid blocks and redstone sources
				maxPower = (short) Math.max(maxPower, ((VanillaBlockMaterial) mat).getRedstonePower(rel, RedstonePowerMode.ALLEXCEPTWIRE));
				if (mat instanceof RedstoneSource) {
					maxPower = (short) Math.max(maxPower, ((RedstoneSource) mat).getRedstonePowerTo(rel, face.getOpposite(), RedstonePowerMode.ALL));
				}
			}
			//shortcut just in case the answer is simple
			if (maxPower == REDSTONE_POWER_MAX) {
				return maxPower;
			}
			//check relatively up and down faces
			if (face == BlockFace.TOP) {
				topIsConductor = RedstoneUtil.isConductor(mat);
			} else if (face != BlockFace.BOTTOM) {
				//check below for wire
				if (!RedstoneUtil.isConductor(mat)) {
					relvert = rel.translate(BlockFace.BOTTOM);
					if (relvert.getMaterial().equals(this)) {
						maxPower = (short) Math.max(maxPower, this.getRedstonePower(relvert) - 1);
					}
				}
				//check above for wire
				if (!topIsConductor) {
					relvert = rel.translate(BlockFace.TOP);
					if (relvert.getMaterial().equals(this)) {
						maxPower = (short) Math.max(maxPower, this.getRedstonePower(relvert) - 1);
					}
				}
			}
		}
		return maxPower;
	}

	/**
	 * Checks if a redstone wire is connected to a certain block<br>
	 * No solid block connections are checked.
	 * @param block of the wire
	 * @param face to connect to
	 * @return True if connected
	 */
	public boolean isConnectedToSource(Block block, BlockFace face) {
		Block target = block.translate(face);
		BlockMaterial mat = target.getMaterial();
		if (mat instanceof RedstoneSource || mat instanceof RedstoneTarget) {
			return true;
		}
		//check below
		if (!RedstoneUtil.isConductor(mat)) {
			if (target.translate(BlockFace.BOTTOM).isMaterial(this)) {
				return true;
			}
		}
		//check above
		if (target.translate(BlockFace.TOP).isMaterial(this)) {
			if (!RedstoneUtil.isConductor(block.translate(BlockFace.TOP))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if this wire is distracted from connecting to a certain solid block
	 * @param block of the wire
	 * @param direction it tries to connect to a solid block
	 * @return True if the wire is distracted
	 */
	public boolean isDistractedFrom(Block block, BlockFace direction) {
		switch (direction) {
			case NORTH:
			case SOUTH:
				return this.isConnectedToSource(block, BlockFace.EAST) || this.isConnectedToSource(block, BlockFace.WEST);
			case EAST:
			case WEST:
				return this.isConnectedToSource(block, BlockFace.NORTH) || this.isConnectedToSource(block, BlockFace.SOUTH);
			default:
				return false;
		}
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return 1;
	}

	@Override
	public EffectRange getPhysicsRange(short data) {
		return physicsRange;
	}

	@Override
	public EffectRange getMaximumPhysicsRange(short data) {
		return maximumPhysicsRange;
	}
}
