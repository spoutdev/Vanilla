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

import org.spout.vanilla.material.block.component.SkullBlock;
import org.spout.vanilla.material.item.BlockItem;

public class Skull extends BlockItem {
	public static final Skull SKELETON_SKULL = new Skull("Skeleton Skull");
	public static final Skull WITHER_SKELETON_SKULL = new Skull("Wither Skeleton Skull", 1, SkullBlock.WITHER_SKELETON_SKULL);
	public static final Skull ZOMBIE_HEAD = new Skull("Zombie Head", 2, SkullBlock.ZOMBIE_HEAD);
	public static final Skull HEAD = new Skull("Head", 3, SkullBlock.HEAD);
	public static final Skull CREEPER_HEAD = new Skull("Creeper Head", 4, SkullBlock.CREEPER_HEAD);

	private Skull(String name) {
		super((short) 0x7, name, 397, SkullBlock.SKELETON_SKULL);
	}

	private Skull(String name, int data, SkullBlock placed) {
		super(name, 397, data, SKELETON_SKULL, placed);
	}

	@Override
	public Skull getParentMaterial() {
		return (Skull) super.getParentMaterial();
	}
}
