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
package org.spout.vanilla.material.block.plant;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.misc.HeadComponent;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.block.liquid.Water;

public class LilyPad extends GroundAttachable {
	public LilyPad(String name, int id) {
		super(name, id);
		this.setHardness(0.0F).setResistance(0.3F).setTransparent();
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (face != BlockFace.TOP) {
			return false;
		}
		BlockMaterial material = block.getMaterial();
		if (material instanceof Water) {
			return ((Water) material).isSource(block);
		}
		return false;
	}

	@Override
	public void onInteract(Entity entity, Action type) {
		super.onInteract(entity, type);
		if (type == Action.RIGHT_CLICK && entity.has(HeadComponent.class)) {
			BlockIterator iterator = entity.get(HeadComponent.class).getBlockView();
			if (iterator == null || !iterator.hasNext()) {
				return;
			}
			Block block = iterator.next().translate(BlockFace.TOP);
			if (this.canPlace(block, (short) 0)) {
				this.onPlacement(block, (short) 0);

				//TODO Subtract from inventory attribute
				// Subtract item
				if (!entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.SURVIVAL)) {
					return;
				}
				PlayerQuickbar inv = entity.get(Human.class).getInventory().getQuickbar();
				if (inv != null) {
					inv.addAmount(inv.getCurrentSlot(), -1);
				}
			}
		}
	}
}
