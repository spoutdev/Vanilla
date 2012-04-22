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
package org.spout.vanilla.material.item.specific;

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.MathHelper;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.generic.GenericItem;

public class DoorItem extends GenericItem {
	private DoorBlock doorBlock;

	public DoorItem(String name, int id, DoorBlock woodenDoorBlock) {
		super(name, id);
		this.doorBlock = woodenDoorBlock;
	}

	@Override
	public void onInteract(Entity entity, Point position, Action type, BlockFace clickedFace) {
		super.onInteract(entity, position, type, clickedFace);
		if (clickedFace != BlockFace.TOP) {
			return;
		}
		World world = position.getWorld();
		int x = (int) position.getX();
		int y = (int) position.getY();
		int z = (int) position.getZ();
		Block b = world.getBlock(x, y - 1, z);

		if (b.getMaterial() instanceof Solid && b.move(BlockFace.TOP).getMaterial() == VanillaMaterials.AIR && b.move(BlockFace.TOP).getMaterial() == VanillaMaterials.AIR) {
			System.out.println("Placing door!");
		}
		/*
		 * Formula kinda copied from minecraft source
		 */
		short hinge = (short) ((MathHelper.floor((double) ((entity.getYaw() + 180F) * 4F) - 0.5)) & 3);
		placeDoorBlock(world, x, y, z, hinge, doorBlock, entity);
	}

	public static void placeDoorBlock(World world, int x, int y, int z, short hinge, DoorBlock doorBlock, Source source) {
		world.setBlockMaterial(x, y, z, doorBlock, hinge, false, source);
		world.setBlockMaterial(x, y + 1, z, doorBlock, (short) (hinge | 0x8), false, source);
	}
}
