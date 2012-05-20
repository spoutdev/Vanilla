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
package org.spout.vanilla.material.block.controlled;

import java.util.ArrayList;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.block.EnchantmentTableController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.VanillaItemStack;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.solid.Solid;
import org.spout.vanilla.material.item.tool.MiningTool;
import org.spout.vanilla.material.item.tool.Pickaxe;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.MoveReaction;

public class EnchantmentTable extends Solid implements Directional, Mineable {
	public EnchantmentTable(String name, int id) {
		super(name, id);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(5.0F).setResistance(2000.0F);
		this.setController(VanillaControllerTypes.ENCHANTMENT_TABLE);
	}

	@Override
	public EnchantmentTableController getController(Block block) {
		return (EnchantmentTableController) super.getController(block);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public short getDurabilityPenalty(MiningTool tool) {
		return tool instanceof Pickaxe ? (short) 1 : (short) 2;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSDRUM;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFace.TOP;
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			block.getWorld().createAndSpawnEntity(block.getPosition(), new EnchantmentTableController());
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

			// Open the enchantment table
			this.getController(block).getInventory().open((VanillaPlayer) controller);
		}
	}

	@Override
	public ArrayList<VanillaItemStack> getDrops(Block block) {
		ArrayList<VanillaItemStack> drops = new ArrayList<VanillaItemStack>();
		if (block.getSource() instanceof Entity) {
			ItemStack held = ((Entity) block.getSource()).getInventory().getCurrentItem();
			if (held != null && held.getMaterial() instanceof Pickaxe) {
				drops.add(new VanillaItemStack(this, 1));
			}
		}
		return drops;
	}
}
