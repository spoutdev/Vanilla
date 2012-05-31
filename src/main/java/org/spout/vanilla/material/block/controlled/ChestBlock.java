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

import org.spout.api.entity.BlockController;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.block.Chest;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class ChestBlock extends ControlledMaterial implements Fuel, Mineable, Directional {
	public final float BURN_TIME = 15.f;

	public ChestBlock(String name, int id) {
		super(VanillaControllerTypes.CHEST, name, id);
		this.setHardness(2.5F).setResistance(4.2F).setOpacity((byte) 0);
		this.setOccludes(false);
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	private Chest createController(Block block) {
		Chest neighbor = null;
		for (BlockFace face : BlockFaces.NESW) {
			neighbor = getController(block, face);
			if (neighbor != null) {
				break;
			}
		}
		Chest created;
		if (neighbor == null) {
			created = new Chest(false);
		} else {
			created = new Chest(true);
			created.getInventory().setContents(neighbor.getInventory().getContents());
			neighbor.getBlock().setController(null);
		}
		block.setController(created);
		return created;
	}

	private Chest getController(Block block, BlockFace face) {
		block = block.translate(face);
		if (block.getMaterial().equals(VanillaMaterials.CHEST)) {
			BlockController controller = block.getController();
			if (controller instanceof Chest) {
				return (Chest) controller;
			}
		}
		return null;
	}

	@Override
	public Chest getController(Block block) {
		if (block.getMaterial().equals(VanillaMaterials.CHEST)) {
			BlockController controller = block.getController();
			if (controller instanceof Chest) {
				return (Chest) controller;
			} else {
				controller = getController(block, BlockFace.SOUTH);
				if (controller != null) {
					return (Chest) controller;
				}
				controller = getController(block, BlockFace.WEST);
				if (controller != null) {
					return (Chest) controller;
				}
			}
			//create a controller
			return createController(block);
		}
		return null;
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

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.canPlace(block, data, against, isClickedBlock)) {
			//no surrounding double-chest blocks?
			Chest chest;
			for (BlockFace face : BlockFaces.NESW) {
				chest = this.getController(block.translate(face));
				if (chest != null && chest.isDouble()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			this.setFacing(block, VanillaPlayerUtil.getFacing(block.getSource()).getOpposite());
		}
		return true;
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
