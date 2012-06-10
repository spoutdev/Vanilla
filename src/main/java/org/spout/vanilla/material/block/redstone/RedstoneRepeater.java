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

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.InsertionPolicy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.RedstonePowerMode;
import org.spout.vanilla.util.RedstoneUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class RedstoneRepeater extends GroundAttachable implements Directional, Mineable, RedstoneSource, RedstoneTarget, DynamicMaterial {
	private final boolean powered;
	private static final Vector3[] maxRange = new Vector3[]{new Vector3(0, 0, 0), new Vector3(0, 0, 0)};

	public RedstoneRepeater(String name, int id, boolean powered) {
		super(name, id);
		this.powered = powered;
		this.setHardness(0.0F).setResistance(0.0F).setOpacity(0).setOcclusion(BlockFaces.NONE).getOcclusion().set(BlockFace.BOTTOM, true); 
	}

	public boolean isPowered() {
		return this.powered;
	}

	@Override
	public byte getLightLevel(short data) {
		return powered ? (byte) 9 : (byte) 0;
	}

	public void onDestroy(Block block, double dropChance) {
		this.doRedstoneUpdates(block);
		super.onDestroy(block, dropChance);
	}

	public void setPowered(Block block, boolean powered) {
		block.setMaterial(powered ? VanillaMaterials.REDSTONE_REPEATER_ON : VanillaMaterials.REDSTONE_REPEATER_OFF, block.getData());
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		block.setMaterial(this);
		this.setFacing(block, VanillaPlayerUtil.getFacing(block.getSource()));
		this.doRedstoneUpdates(block);
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
			block.update();
		}
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		boolean receiving = this.isReceivingPower(block);
		if (this.isPowered() != receiving) {
			block.dynamicUpdate(block.getWorld().getAge() + this.getTickDelay(block), InsertionPolicy.WEAK_REPLACE_LATER, receiving);
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
	public void doRedstoneUpdates(Block block) {
		block.setSource(this).update().translate(this.getFacing(block)).update();
	}

	@Override
	public boolean isReceivingPower(Block block) {
		BlockFace face = this.getFacing(block).getOpposite();
		return RedstoneUtil.isPowered(block.translate(face), face.getOpposite());
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return 1;
	}

	@Override
	public Vector3[] maxRange() {
		return maxRange;
	}

	@Override
	public long onPlacement(Block b, Region r, long currentTime) {
		long nextUpdate = this.getTickDelay(b) + currentTime;
		return nextUpdate;
	}

	@Override
	public long update(Block block, Region r, long updateTime, long lastUpdateTime, Object hint) {
		boolean receiving = this.isReceivingPower(block);
		if (hint != null && hint instanceof Boolean && (Boolean) hint) {
			this.setPowered(block, true);
			if (!receiving) {
				return updateTime + this.getTickDelay(block);
			}
		} else if (receiving != this.isPowered()) {
			this.setPowered(block, receiving);
		}
		return -1;
	}
}
