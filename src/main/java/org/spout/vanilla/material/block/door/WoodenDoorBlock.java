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
package org.spout.vanilla.material.block.door;

import java.util.ArrayList;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.DoorBlock;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.VanillaNetworkUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class WoodenDoorBlock extends DoorBlock {
	public WoodenDoorBlock(String name, int id) {
		super(name, id);
		this.setHardness(3.0F).setResistance(5.0F).setOpacity((byte) 1);
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action action, BlockFace clickedFace) {
		super.onInteractBy(entity, block, action, clickedFace);
		if (action == Action.LEFT_CLICK && VanillaPlayerUtil.getGameMode(block.getSource()) != null && VanillaPlayerUtil.getGameMode(block.getSource()).hasInstantBreak()) {
			return;
		}
		this.toggleOpen(block);
		VanillaNetworkUtil.playBlockEffect(block, entity, PlayEffectMessage.Messages.RANDOM_DOOR);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(VanillaMaterials.WOODEN_DOOR, 1));
		return drops;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Axe ? (short) 1 : (short) 2;
	}
}
