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
package org.spout.vanilla.plugin.material.item.minecart;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.plugin.component.substance.object.projectile.Snowball;
import org.spout.vanilla.plugin.component.substance.object.vehicle.Minecart;
import org.spout.vanilla.plugin.material.block.rail.RailBase;
import org.spout.vanilla.plugin.material.item.VanillaItemMaterial;

import com.bulletphysics.collision.shapes.SphereShape;

public class MinecartItem extends VanillaItemMaterial {
	private Class<? extends Minecart> spawnedEntity;
	
	public MinecartItem(String name, int id, Class<? extends Minecart> spawnedEntity) {
		super(name, id, null);
		this.spawnedEntity = spawnedEntity;
	}

	public Class<? extends Minecart> getSpawnedEntity() {
		return spawnedEntity;
	}
	
	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		super.onInteract(entity, block, type, clickedface);

		//is clicked position a track?
		if (block.getMaterial() instanceof RailBase) {
			//spawn minecart on rail
			Minecart minecart = block.getWorld().createEntity(block.getPosition(), getSpawnedEntity()).add(getSpawnedEntity());
			block.getWorld().spawnEntity(minecart.getOwner());
			//TODO: Subtracting one from the held item?
			//Shouldn't the held item be passed to this function instead?
		}
	}
}
