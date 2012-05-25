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
package org.spout.vanilla.material.item.misc;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Inventory;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.controller.object.misc.Painting;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class PaintingItem extends VanillaItemMaterial {
	private Random random = new Random();

	public enum PaintingStyle {
		Kebab,
		Aztec,
		Alban,
		Aztec2,
		Bomb,
		Plant,
		Wasteland,
		Pool,
		Courbet,
		Sea,
		Sunset,
		Creebet,
		Wanderer,
		Graham,
		Match,
		Bust,
		Stage,
		Void,
		SkullAndRoses,
		Fighters,
		Pointer,
		Pigscene,
		BurningSkull,
		Skeleton,
		DonkeyKong;
	}

	public PaintingItem(String name, int id) {
		super(name, id);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		if (type != Action.RIGHT_CLICK) {
			return;
		}
		Painting painting = new Painting(PaintingStyle.values()[random.nextInt(PaintingStyle.values().length)], BlockFaces.NESW.indexOf(clickedface, -1));//TODO fix the 0 here, and the position on the next line
		block.getWorld().createAndSpawnEntity(block.translate(clickedface).getPosition(), painting);
		Inventory inventory = VanillaPlayerUtil.getInventory(entity);
		if (inventory != null) {
			inventory.addCurrentItemAmount(-1);
		}
	}
}
