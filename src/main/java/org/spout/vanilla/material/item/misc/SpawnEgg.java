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
package org.spout.vanilla.material.item.misc;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.item.VanillaItemMaterial;

public class SpawnEgg extends VanillaItemMaterial {
	private static final SpawnEgg PARENT = new SpawnEgg("Spawn Egg"); //There is no entity with the ID 0 so this egg is invalid
	public static final SpawnEgg CREEPER = new SpawnEgg("Spawn Creeper", 50, PARENT);
	public static final SpawnEgg SKELETON = new SpawnEgg("Spawn Skeleton", 51, PARENT);
	public static final SpawnEgg SPIDER = new SpawnEgg("Spawn Spider", 52, PARENT);
	public static final SpawnEgg ZOMBIE = new SpawnEgg("Spawn Zombie", 54, PARENT);
	public static final SpawnEgg SLIME = new SpawnEgg("Spawn Slime", 55, PARENT);
	public static final SpawnEgg GHAST = new SpawnEgg("Spawn Ghast", 56, PARENT);
	public static final SpawnEgg PIGMAN = new SpawnEgg("Spawn Pigman", 57, PARENT);
	public static final SpawnEgg ENDERMAN = new SpawnEgg("Spawn Enderman", 58, PARENT);
	public static final SpawnEgg CAVESPIDER = new SpawnEgg("Spawn Cavespider", 59, PARENT);
	public static final SpawnEgg SILVERFISH = new SpawnEgg("Spawn Silverfish", 60, PARENT);
	public static final SpawnEgg BLAZE = new SpawnEgg("Spawn Blaze", 61, PARENT);
	public static final SpawnEgg MAGMACUBE = new SpawnEgg("Spawn Magmacube", 62, PARENT);
	public static final SpawnEgg PIG = new SpawnEgg("Spawn Pig", 90, PARENT);
	public static final SpawnEgg SHEEP = new SpawnEgg("Spawn Sheep", 91, PARENT);
	public static final SpawnEgg COW = new SpawnEgg("Spawn Cow", 92, PARENT);
	public static final SpawnEgg CHICKEN = new SpawnEgg("Spawn Chicken", 93, PARENT);
	public static final SpawnEgg SQUID = new SpawnEgg("Spawn Squid", 94, PARENT);
	public static final SpawnEgg WOLF = new SpawnEgg("Spawn Wolf", 95, PARENT);
	public static final SpawnEgg MOOSHROOM = new SpawnEgg("Spawn Mooshroom", 96, PARENT);
	public static final SpawnEgg VILLAGER = new SpawnEgg("Spawn Villager", 120, PARENT);
	public static final SpawnEgg OCELOT = new SpawnEgg("Spawn Ocelot", 98, PARENT);

	private SpawnEgg(String name) {
		super((short) 0x007F, name, 383, null);
	}

	private SpawnEgg(String name, int data, Material parent) {
		super(name, 383, data, parent, null);
	}

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		if (type != Action.RIGHT_CLICK) {
			return;
		}
		//TODO re-write spawn egg spawning handling.
	}
}
