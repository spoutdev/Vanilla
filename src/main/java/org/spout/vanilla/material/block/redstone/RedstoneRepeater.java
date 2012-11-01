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
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.ListEffectRange;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.RedstonePowerMode;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.util.PlayerUtil;
import org.spout.vanilla.util.RedstoneUtil;

public class RedstoneRepeater extends GroundAttachable implements Directional, RedstoneSource, RedstoneTarget, DynamicMaterial, InitializableMaterial {
	private static final EffectRange[] physicsRanges;
	private final boolean powered;

	static {
		physicsRanges = new EffectRange[4];
		BlockFaces base = BlockFaces.NESWBT.append(BlockFace.THIS);
		List<BlockFace> tmpFaces = new ArrayList<BlockFace>();
		for (int i = 0; i < physicsRanges.length; i++) {
			for (BlockFace face : base) {
				tmpFaces.add(face);
			}
			BlockFace current = BlockFaces.ESWN.get(i);
			tmpFaces.remove(current.getOpposite());
			physicsRanges[i] = new ListEffectRange(tmpFaces.toArray(new BlockFace[0])).translate(current);
			tmpFaces.clear();
		}
	}

	public RedstoneRepeater(String name, int id, boolean powered) {
		super(name, id);
		this.powered = powered;
		this.setHardness(0.0F).setResistance(0.0F).setOpacity(0).setOcclusion((short) 0, BlockFace.BOTTOM);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().add(VanillaMaterials.REDSTONE_REPEATER);
	}

	public boolean isPowered() {
		return this.powered;
	}

	@Override
	public byte getLightLevel(short data) {
		return powered ? (byte) 9 : (byte) 0;
	}

	public void setPowered(Block block, boolean powered) {
		block.setMaterial(powered ? VanillaMaterials.REDSTONE_REPEATER_ON : VanillaMaterials.REDSTONE_REPEATER_OFF, block.getData());
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		block.setMaterial(this);
		this.setFacing(block, PlayerUtil.getFacing(block.getSource()));
		return true;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type == Action.RIGHT_CLICK) {
			int index = this.getTickDelayIndex(block);
			index++;
			if (index >= TICK_DELAYS.length) {
				index = 0;
			}
			this.setTickDelayIndex(block, index);
		}
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		boolean receiving = this.isReceivingPower(block);
		//System.out.println("RECEIVING: " + receiving);
		if (this.isPowered() != receiving) {
			block.dynamicUpdate(block.getWorld().getAge() + this.getTickDelay(block), receiving ? 1 : 0);
		}
	}

	public static final int[] TICK_DELAYS = {100, 200, 300, 400};

	public int getTickDelay(Block block) {
		return TICK_DELAYS[this.getTickDelayIndex(block)];
	}

	public int getTickDelayIndex(Block block) {
		return (block.getData() & 12) >> 2;
	}

	public void setTickDelayIndex(Block block, int tickDelayIndex) {
		block.setData((tickDelayIndex << 2 & 12) | (block.getData() & 0x3));
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.ESWN.get(block.getData() & 0x3);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData((block.getData() & ~0x3) + BlockFaces.ESWN.indexOf(facing, 0));
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return 0;
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return false;
	}

	@Override
	public short getRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasRedstonePowerTo(block, direction, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.isPowered() && this.getFacing(block) == direction;
	}

	@Override
	public boolean isReceivingPower(Block block) {
		BlockFace face = this.getFacing(block).getOpposite();
		return RedstoneUtil.isEmittingPower(block.translate(face));
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		b.dynamicUpdate(this.getTickDelay(b) + currentTime);
	}

	@Override
	public void onDynamicUpdate(Block block, Region r, long updateTime, int data) {
		boolean receiving = this.isReceivingPower(block);
		if ((data & 1) == 1) {
			// Was receiving and should power up
			if (!this.isPowered()) {
				this.setPowered(block, true);
			}
			if (!receiving) {
				block.dynamicUpdate(updateTime + this.getTickDelay(block));
			}
		} else if (receiving != this.isPowered()) {
			// Was not receiving and should update state
			this.setPowered(block, receiving);
		}
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}

	@Override
	public EffectRange getPhysicsRange(short data) {
		return physicsRanges[data & 0x3];
	}
}
