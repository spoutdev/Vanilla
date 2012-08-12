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
package org.spout.vanilla.util;

import org.spout.vanilla.data.drops.flag.DropFlagBundle;
import org.spout.vanilla.data.drops.flag.DropFlagSingle;
import org.spout.vanilla.data.drops.flag.ToolLevelFlags;

public class ToolLevel {
	public static final ToolLevel NONE = new ToolLevel(ToolLevelFlags.NONE, ToolLevelFlags.NONE_UP, 1.0f, 0, 0);
	public static final ToolLevel WOOD = new ToolLevel(ToolLevelFlags.WOOD, ToolLevelFlags.WOOD_UP, 2.0f, 59, 0);
	public static final ToolLevel STONE = new ToolLevel(ToolLevelFlags.STONE, ToolLevelFlags.STONE_UP, 4.0f, 131, 1);
	public static final ToolLevel IRON = new ToolLevel(ToolLevelFlags.IRON, ToolLevelFlags.IRON_UP, 6.0f, 250, 2);
	public static final ToolLevel DIAMOND = new ToolLevel(ToolLevelFlags.DIAMOND, ToolLevelFlags.DIAMOND_UP, 8.0f, 1561, 3);
	public static final ToolLevel GOLD = new ToolLevel(ToolLevelFlags.GOLD, ToolLevelFlags.GOLD_UP, 12.0f, 32, 0);

	private final DropFlagSingle toolFlag;
	private final DropFlagBundle dropFlag;
	private final float diggingSpeed;
	private final short maxDurability;
	private final short damageBonus;

	public ToolLevel(DropFlagSingle toolFlag, DropFlagBundle dropFlag, float diggingSpeed, int maxDurability, int damageBonus) {
		this.toolFlag = toolFlag;
		this.dropFlag = dropFlag;
		this.diggingSpeed = diggingSpeed;
		this.maxDurability = (short) maxDurability;
		this.damageBonus = (short) damageBonus;
	}

	/**
	 * Gets the additional damage this tool level provides
	 * 
	 * @return tool damage bonus
	 */
	public short getDamageBonus() {
		return this.damageBonus;
	}

	/**
	 * Gets the particular tool flag for this tool level
	 * 
	 * @return Tool flag
	 */
	public DropFlagSingle getToolFlag() {
		return this.toolFlag;
	}

	/**
	 * Gets the particular block drop flag for this tool level
	 * 
	 * @return Drop flag
	 */
	public DropFlagBundle getDropFlag() {
		return this.dropFlag;
	}

	/**
	 * Gets the time it takes to dig using this tool level
	 * 
	 * @return digging time
	 */
	public float getDiggingSpeed() {
		return this.diggingSpeed;
	}

	/**
	 * Gets the maximum durability a tool with this level has
	 * 
	 * @return maximum durability
	 */
	public short getMaxDurability() {
		return this.maxDurability;
	}
}
