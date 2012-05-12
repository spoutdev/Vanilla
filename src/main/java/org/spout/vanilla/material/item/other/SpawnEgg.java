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
package org.spout.vanilla.material.item.other;

import org.spout.api.material.Material;
import org.spout.vanilla.material.item.VanillaItemMaterial;

public class SpawnEgg extends VanillaItemMaterial {
	private static final SpawnEgg PARENT = register(new SpawnEgg("Spawn Egg")); //There is no entity with the ID 0 so this egg is invalid
	public static final SpawnEgg CREEPER = register(new SpawnEgg("Spawn Creeper", 50, PARENT));
	public static final SpawnEgg SKELETON = register(new SpawnEgg("Spawn Skeleton", 51, PARENT));
	public static final SpawnEgg SPIDER = register(new SpawnEgg("Spawn Spider", 52, PARENT));
	public static final SpawnEgg ZOMBIE = register(new SpawnEgg("Spawn Zombie", 54, PARENT));
	public static final SpawnEgg SLIME = register(new SpawnEgg("Spawn Slime", 55, PARENT));
	public static final SpawnEgg GHAST = register(new SpawnEgg("Spawn Ghast", 56, PARENT));
	public static final SpawnEgg PIGMAN = register(new SpawnEgg("Spawn Pigman", 57, PARENT));
	public static final SpawnEgg ENDERMAN = register(new SpawnEgg("Spawn Enderman", 58, PARENT));
	public static final SpawnEgg CAVESPIDER = register(new SpawnEgg("Spawn Cavespider", 59, PARENT));
	public static final SpawnEgg SILVERFISH = register(new SpawnEgg("Spawn Silverfish", 60, PARENT));
	public static final SpawnEgg BLAZE = register(new SpawnEgg("Spawn Blaze", 61, PARENT));
	public static final SpawnEgg MAGMACUBE = register(new SpawnEgg("Spawn Magmacube", 62, PARENT));
	public static final SpawnEgg PIG = register(new SpawnEgg("Spawn Pig", 90, PARENT));
	public static final SpawnEgg SHEEP = register(new SpawnEgg("Spawn Sheep", 91, PARENT));
	public static final SpawnEgg COW = register(new SpawnEgg("Spawn Cow", 92, PARENT));
	public static final SpawnEgg CHICKEN = register(new SpawnEgg("Spawn Chicken", 93, PARENT));
	public static final SpawnEgg SQUID = register(new SpawnEgg("Spawn Squid", 94, PARENT));
	public static final SpawnEgg WOLF = register(new SpawnEgg("Spawn Wolf", 95, PARENT));
	public static final SpawnEgg MOOSHROOM = register(new SpawnEgg("Spawn Mooshroom", 96, PARENT));
	public static final SpawnEgg VILLAGER = register(new SpawnEgg("Spawn Villager", 120, PARENT));
	public static final SpawnEgg OCELOT = register(new SpawnEgg("Spawn Ocelot", 98, PARENT));

	private SpawnEgg(String name) {
		super(name, 383);
	}

	private SpawnEgg(String name, int data, Material parent) {
		super(name, 383, data, parent);
	}
}
