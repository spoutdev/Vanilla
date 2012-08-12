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
package org.spout.vanilla.material.item.tool;

import org.spout.vanilla.util.ToolLevel;
import org.spout.vanilla.util.ToolType;

/**
 * A type of tool that allows you to mine certain blocks faster
 */
public class MiningTool extends Tool {
	private float diggingSpeed;

	public MiningTool(String name, int id, ToolLevel toolLevel, ToolType toolType) {
		super(name, id, toolLevel.getMaxDurability(), toolType);
		this.addDropFlags(toolLevel.getToolFlag());
		this.diggingSpeed = toolLevel.getDiggingSpeed();
	}

	/**
	 * Gets the time this type of tool subtracts from the digging time<br>
	 * The default digging speed without tool is 1.0
	 * 
	 * @return digging time
	 */
	public float getDiggingSpeed() {
		return this.diggingSpeed;
	}

	/**
	 * Sets the time this type of tool subtracts from the digging time<br>
	 * The default digging speed without tool is 1.0
	 * 
	 * @param diggingTime to set to
	 * @return this Mining Tool
	 */
	public MiningTool setDiggingSpeed(float diggingSpeed) {
		this.diggingSpeed = diggingSpeed;
		return this;
	}
}
