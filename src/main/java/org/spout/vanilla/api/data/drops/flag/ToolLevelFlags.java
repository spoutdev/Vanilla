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
package org.spout.vanilla.api.data.drops.flag;

import org.spout.api.util.flag.FlagBoolean;
import org.spout.api.util.flag.FlagBundle;
import org.spout.api.util.flag.FlagSingle;

public class ToolLevelFlags {
	/**
	 * An unknown type was used, always evaluates True
	 */
	public static final FlagBoolean NONE = new FlagBoolean(true);
	/**
	 * A wooden tool or weapon was used
	 */
	public static final FlagSingle WOOD = new FlagSingle();
	/**
	 * A stone tool or weapon was used
	 */
	public static final FlagSingle STONE = new FlagSingle();
	/**
	 * An iron tool or weapon was used
	 */
	public static final FlagSingle IRON = new FlagSingle();
	/**
	 * A golden tool or weapon was used
	 */
	public static final FlagSingle GOLD = new FlagSingle();
	/**
	 * A diamond tool or weapon was used
	 */
	public static final FlagSingle DIAMOND = new FlagSingle();
	/**
	 * Any type of level
	 */
	public static final FlagBundle NONE_UP = new FlagBundle(NONE);
	/**
	 * A wooden or higher tool or weapon level
	 */
	public static final FlagBundle WOOD_UP = new FlagBundle(WOOD, STONE, IRON, GOLD, DIAMOND);
	/**
	 * A stone or higher tool or weapon level
	 */
	public static final FlagBundle STONE_UP = new FlagBundle(STONE, IRON, GOLD, DIAMOND);
	/**
	 * An iron or higher tool or weapon level
	 */
	public static final FlagBundle IRON_UP = new FlagBundle(IRON, GOLD, DIAMOND);
	/**
	 * A gold or higher tool or weapon level
	 */
	public static final FlagBundle GOLD_UP = new FlagBundle(GOLD, DIAMOND);
	/**
	 * A diamond or higher tool or weapon level
	 */
	public static final FlagBundle DIAMOND_UP = new FlagBundle(DIAMOND);
}
