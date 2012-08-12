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
package org.spout.vanilla.data.drops.flag;

public class ToolLevelFlags {
	/**
	 * An unknown type was used, always evaluates True
	 */
	public static final DropFlagSingle NONE = new DropFlagTrue();
	/**
	 * A wooden tool or weapon was used
	 */
	public static final DropFlagSingle WOOD = new DropFlagSingle();
	/**
	 * A stone tool or weapon was used
	 */
	public static final DropFlagSingle STONE = new DropFlagSingle();
	/**
	 * An iron tool or weapon was used
	 */
	public static final DropFlagSingle IRON = new DropFlagSingle();
	/**
	 * A golden tool or weapon was used
	 */
	public static final DropFlagSingle GOLD = new DropFlagSingle();
	/**
	 * A diamond tool or weapon was used
	 */
	public static final DropFlagSingle DIAMOND = new DropFlagSingle();
	/**
	 * Any type of level
	 */
	public static final DropFlagBundle NONE_UP = new DropFlagBundle(NONE);
	/**
	 * A wooden or higher tool or weapon level
	 */
	public static final DropFlagBundle WOOD_UP = new DropFlagBundle(WOOD, STONE, IRON, GOLD, DIAMOND);
	/**
	 * A stone or higher tool or weapon level
	 */
	public static final DropFlagBundle STONE_UP = new DropFlagBundle(STONE, IRON, GOLD, DIAMOND);
	/**
	 * An iron or higher tool or weapon level
	 */
	public static final DropFlagBundle IRON_UP = new DropFlagBundle(IRON, GOLD, DIAMOND);
	/**
	 * A gold or higher tool or weapon level
	 */
	public static final DropFlagBundle GOLD_UP = new DropFlagBundle(GOLD, DIAMOND);
	/**
	 * A diamond or higher tool or weapon level
	 */
	public static final DropFlagBundle DIAMOND_UP = new DropFlagBundle(DIAMOND);
}
