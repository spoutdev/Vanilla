/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.material.item;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.entity.object.vehicle.minecart.TransportMinecart;
import org.spout.vanilla.material.block.MinecartTrack;
import org.spout.vanilla.material.generic.GenericItem;

public class Minecart extends GenericItem {
	public Minecart(String name, int id) {
		super(name, id);
	}

	/**
	 * Creates a new minecart controller to spawn when interacted
	 * @return a new Minecart controller
	 */
	protected Controller getSpawnedEntity() {
		return new TransportMinecart();
	}

	@Override
	public void onInteract(Entity entity, Point position, Action type, BlockFace clickedface) {
		super.onInteract(entity, position, type, clickedface);

		//is clicked position a track?
		World world = position.getWorld();
		Block block = world.getBlock(position);
		if (block.getMaterial() instanceof MinecartTrack) {
			//spawn minecart on rails
			world.createAndSpawnEntity(position, this.getSpawnedEntity());
			//TODO: Subtracting one from the held item?
			//Shouldn't the held item be passed to this function instead?
		}
	}
}
