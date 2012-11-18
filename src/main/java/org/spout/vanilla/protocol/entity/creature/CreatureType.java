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
package org.spout.vanilla.protocol.entity.creature;

public enum CreatureType {
	BAT(65),
	BLAZE(61),
	CAVE_SPIDER(59),
	CHICKEN(93),
	COW(92),
	CREEPER(50),
	ENDER_DRAGON(63),
	ENDERMAN(58),
	GHAST(56),
	GIANT(53),
	IRON_GOLEM(99),
	MAGMA_CUBE(62),
	MUSHROOM_COW(96),
	OCELOT(98),
	PIG(90),
	PIG_ZOMBIE(57),
	SHEEP(91),
	SILVERFISH(60),
	SKELETON(51),
	SLIME(55),
	SNOW_GOLEM(97),
	SPIDER(52),
	SQUID(94),
	VILLAGER(120),
	WITCH(66),
	WITHER(64),
	WOLF(95),
	ZOMBIE(54);
	private int id;

	private CreatureType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
