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

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.ListEffectRange;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.block.attachable.AbstractAttachable;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.util.RedstonePowerMode;
import org.spout.vanilla.util.VanillaPlayerUtil;

import static org.spout.vanilla.util.VanillaNetworkUtil.playBlockEffect;

public class Lever extends AbstractAttachable implements Mineable, RedstoneSource {
	private static final EffectRange[] physicsRanges;

	static {
		physicsRanges = new EffectRange[6];
		BlockFaces faces = BlockFaces.NESWB.append(BlockFace.BOTTOM);
		for (int i = 0; i < physicsRanges.length; i++) {
			physicsRanges[i] = new ListEffectRange(EffectRange.THIS_AND_NEIGHBORS, EffectRange.THIS_AND_NEIGHBORS.translate(faces.get(i)));
		}
	}

	public Lever(String name, int id) {
		super(name, id);
		this.setAttachable(BlockFaces.NESWB).setLiquidObstacle(false).setHardness(0.5F).setResistance(1.7F).setTransparent();
	}

	@Override
	public void onDestroy(Block block, double dropChance) {
		super.onDestroy(block, dropChance);
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action action, BlockFace clickedFace) {
		super.onInteractBy(entity, block, action, clickedFace);
		if (action == Action.LEFT_CLICK && VanillaPlayerUtil.isCreative(block.getSource())) {
			return;
		}
		this.toggle(block);
		if (this.isDown(block)) {
			playBlockEffect(block, entity, PlayEffectMessage.Messages.RANDOM_CLICK_1);
		} else {
			playBlockEffect(block, entity, PlayEffectMessage.Messages.RANDOM_CLICK_2);
		}
	}

	public boolean isDown(Block block) {
		return block.isDataBitSet(0x8);
	}

	public void setDown(Block block, boolean toggled) {
		block.setDataBits(0x8, toggled);
	}

	public void toggle(Block block) {
		block.setData(block.getData() ^ 0x8);
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace) {
		short data;
		if (attachedFace == BlockFace.BOTTOM) {
			Source source = block.getSource();
			data = (short) (5 + Math.random());
			if (source instanceof Entity) {
				// set data using direction
				Vector3 direction = block.getPosition().subtract(((Entity) source).getPosition());
				direction = direction.abs();
				if (direction.getX() > direction.getZ()) {
					data = 6;
				} else {
					data = 5;
				}
			}
		} else {
			data = (short) (BlockFaces.NSEW.indexOf(attachedFace, 0) + 1);
		}
		System.out.println("Block attached: " + block.getPosition().toBlockString());
		block.setData(data);
	}

	@Override
	public BlockFace getAttachedFace(Block block) {
		return BlockFaces.NSEWB.get(block.getDataField(0x7) - 1);
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isDown(block);
	}

	@Override
	public short getRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasRedstonePowerTo(block, direction, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.isDown(block) && this.getAttachedFace(block) == direction;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}

	@Override
	public EffectRange getPhysicsRange(short data) {
		data = (short) ((data & 0x7) - 1);
		if (data < 0) {
			return physicsRanges[0];
		} else if (data >= physicsRanges.length) {
			return physicsRanges[physicsRanges.length - 1];
		} else {
			return physicsRanges[data];
		}
	}
}
