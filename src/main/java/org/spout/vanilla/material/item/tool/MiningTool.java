/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.material.item.tool;

import java.util.Set;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;

/**
 * A type of tool that allows you to mine certain blocks faster
 */
public class MiningTool extends Tool {
	private float diggingSpeed;
	private final ToolLevel toolLevel;

	public MiningTool(String name, int id, ToolLevel toolLevel, ToolType toolType) {
		super(name, id, toolLevel.getMaxDurability(), toolType, null);
		this.toolLevel = toolLevel;
		this.diggingSpeed = toolLevel.getDiggingSpeed();
		this.setEnchantability(toolLevel.getEnchantability());
	}

	@Override
	public void getItemFlags(ItemStack item, Set<Flag> flags) {
		super.getItemFlags(item, flags);
		flags.add(this.toolLevel.getToolFlag());
	}

	/**
	 * Gets the time this type of tool subtracts from the digging time<br> The default digging speed without tool is 1.0
	 *
	 * @return digging time
	 */
	public float getDiggingSpeed() {
		return this.diggingSpeed;
	}

	/**
	 * Sets the time this type of tool subtracts from the digging time<br> The default digging speed without tool is 1.0
	 *
	 * @param diggingSpeed to set to
	 * @return this Mining Tool
	 */
	public MiningTool setDiggingSpeed(float diggingSpeed) {
		this.diggingSpeed = diggingSpeed;
		return this;
	}
}
