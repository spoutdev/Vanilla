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
package org.spout.vanilla.plugin.material.item.misc;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.api.data.PaintingType;
import org.spout.vanilla.plugin.component.substance.Painting;
import org.spout.vanilla.plugin.material.item.VanillaItemMaterial;

public class PaintingItem extends VanillaItemMaterial {
	private final Random random = new Random();

	public PaintingItem(String name, int id) {
		super(name, id, null);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace face) {
		if (!(entity instanceof Player) || type != Action.RIGHT_CLICK) {
			return;
		}

		World world = block.getWorld();
		Entity e = world.createEntity(block.getPosition(), Painting.class);
		PaintingType[] types = PaintingType.values();
		PaintingType paintingType = types[random.nextInt(types.length - 1)];
		BlockFace direction = face.getOpposite();

		e.getTransform().setPosition(paintingType.getCenter(direction, block.getPosition()));
		Painting painting = e.add(Painting.class);
		painting.setType(paintingType);
		painting.setFace(direction);
		world.spawnEntity(e);
	}
}
