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
package org.spout.vanilla.plugin.protocol.entity.object;

public enum ObjectType {
	BOAT(1),
	ITEM(2),
	MINECART(10),
	STORAGE_MINECART(11),
	POWERED_MINECART(12),
	PRIMED_TNT(50),
	ENDER_CRYSTAL(51),
	ARROW(60),
	SNOWBALL(61),
	EGG(62),
	ENDER_PEARL(65),
	WITHER_SKULL(66),
	FALLING_OBJECT(70),
	ITEM_FRAME(71),
	EYE_OF_ENDER(72),
	POTION(73),
	DRAGON_EGG(74),
	EXP_BOTTLE(75),
	FIREWORKS_ROCKET(76),
	FISHING_BOB(90);
	private final int id;

	private ObjectType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
