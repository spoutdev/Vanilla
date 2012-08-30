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
package org.spout.vanilla.material.block.controlled;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.components.VanillaControllerTypes;
import org.spout.vanilla.components.VanillaPlayerController;
import org.spout.vanilla.components.block.CraftingTable;
import org.spout.vanilla.util.Instrument;

public class CraftingTableBlock extends ControlledMaterial {
	public CraftingTableBlock(String name, int id) {
		super(VanillaControllerTypes.CRAFTING_TABLE, name, id);
		this.setHardness(4.2F);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public CraftingTable getController(Block block) {
		return (CraftingTable) super.getController(block);
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action action, BlockFace face) {
		if (action == Action.RIGHT_CLICK) {
			Controller controller = entity.getController();
			if (!(controller instanceof VanillaPlayerController)) {
				return;
			}

			// Open the crafting table
			getController(block).open((VanillaPlayerController) controller);
		}
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
