/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.material.item.misc;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Placeable;
import org.spout.api.material.block.BlockFace;

import org.spout.math.GenericMath;
import org.spout.math.vector.Vector3;
import org.spout.vanilla.component.entity.substance.Painting;
import org.spout.vanilla.data.PaintingType;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.VanillaItemMaterial;

public class PaintingItem extends VanillaItemMaterial implements Placeable {
	public PaintingItem(String name, int id) {
		super(name, id, null);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		return !BlockFace.TOP.equals(against) && !BlockFace.BOTTOM.equals(against) && block.translate(against.getOpposite()).isMaterial(VanillaMaterials.AIR);
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		List<PaintingType> list = new ArrayList<PaintingType>();

		World world = block.getWorld();
		for (PaintingType paintingType : PaintingType.values()) {
			if (paintingType.canBePlaced(block, against)) {
				list.add(paintingType);
			}
		}
		PaintingType paintingType = list.get(GenericMath.getRandom().nextInt(list.size() - 1));

		Entity e = world.createEntity(paintingType.getCenter(against, block.getPosition()), Painting.class);
		Painting painting = e.add(Painting.class);
		painting.setType(paintingType);
		painting.setFace(against);
		world.spawnEntity(e);
	}
}
