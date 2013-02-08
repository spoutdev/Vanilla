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
package org.spout.vanilla.plugin.material.block.solid;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.plugin.component.inventory.WindowHolder;
import org.spout.vanilla.api.data.Instrument;
import org.spout.vanilla.plugin.inventory.window.block.CraftingTableWindow;
import org.spout.vanilla.plugin.material.block.Solid;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;

public class CraftingTable extends Solid {
	public CraftingTable(String name, int id) {
		super(name, id, VanillaMaterialModels.CRAFTING_TABLE);
		this.setHardness(4.2F);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASS_GUITAR;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action action, BlockFace face) {
		if (action == Action.RIGHT_CLICK) {
			if (!(entity instanceof Player)) {
				return;
			}
			((Player) entity).get(WindowHolder.class).openWindow(new CraftingTableWindow((Player) entity));
		}
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}
}
