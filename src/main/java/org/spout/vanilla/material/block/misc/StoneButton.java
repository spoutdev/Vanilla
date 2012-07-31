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

import java.util.EnumMap;

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

import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.block.attachable.AbstractAttachable;
import org.spout.vanilla.material.block.attachable.PointAttachable;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.util.RedstonePowerMode;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class StoneButton extends AbstractAttachable implements Mineable, PointAttachable, RedstoneSource, DynamicMaterial {
	public static final int TICK_DELAY = 1000;
	private static EnumMap<BlockFace, EffectRange> physicsRanges = new EnumMap<BlockFace, EffectRange>(BlockFace.class);

	static {
		for (BlockFace face : BlockFaces.NESW) {
			physicsRanges.put(face, new ListEffectRange(EffectRange.THIS_AND_NEIGHBORS, EffectRange.THIS_AND_NEIGHBORS.translate(face)));
		}
	}

	public StoneButton(String name, int id) {
		super(name, id);
		this.setAttachable(BlockFaces.NESW).setLiquidObstacle(false).setHardness(0.5F).setResistance(0.8F).setTransparent();
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isPressed(block);
	}

	@Override
	public short getRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.getAttachedFace(block) == direction && this.isPressed(block);
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		return face != BlockFace.TOP && super.canAttachTo(block, face);
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (oldMaterial == this && this.isPressed(block)) {
			block.dynamicUpdate(block.getWorld().getAge() + TICK_DELAY);
		}
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type != Action.LEFT_CLICK || !VanillaPlayerUtil.isCreative(entity)) {
			if (!this.isPressed(block)) {
				this.setPressed(block, true);
				GeneralEffects.RANDOM_CLICK2.playGlobal(block.getPosition(), entity);
			}
		}
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace) {
		block.setData((short) (BlockFaces.NSEW.indexOf(attachedFace, 3) + 1));
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.NSEW.get((data & 0x7) - 1);
	}

	public boolean isPressed(Block block) {
		return block.isDataBitSet(0x8);
	}

	public void setPressed(Block block, boolean pressed) {
		block.setDataBits(0x8, pressed);
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
	}

	@Override
	public void onDynamicUpdate(Block block, Region r, long updateTime, long lastUpdateTime, int data, Object hint) {
		if (!this.isPressed(block)) {
			return;
		}
		this.setPressed(block, false);
		GeneralEffects.RANDOM_CLICK2.playGlobal(block.getPosition());
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}

	@Override
	public EffectRange getPhysicsRange(short data) {
		return physicsRanges.get(getAttachedFace(data));
	}
}
