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
package org.spout.vanilla.material.item.vehicle;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.component.entity.substance.vehicle.MinecartBase;
import org.spout.vanilla.material.block.rail.RailBase;
import org.spout.vanilla.material.item.EntitySpawnItem;

public class MinecartItem<T extends MinecartBase> extends EntitySpawnItem<T> {
	public MinecartItem(String name, int id, Class<? extends T> spawnedComponent) {
		super(name, id, null);
		this.setSpawnedComponent(spawnedComponent);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		super.onInteract(entity, block, type, clickedface);
		if (type == Action.RIGHT_CLICK && block.getMaterial() instanceof RailBase) {
			// Spawn the Minecart on the rail
			//TODO: Spawn position adjustment based on rail type
			this.spawnEntity(block, Vector3.ZERO);
			this.handleSelectionRemove(entity);
		}
	}
}
