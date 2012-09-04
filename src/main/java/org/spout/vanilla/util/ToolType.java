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

import org.spout.api.util.flag.Flag;

import org.spout.vanilla.data.drops.flag.ToolTypeFlags;

public class ToolType {
	public static final ToolType OTHER = new ToolType(ToolTypeFlags.OTHER);
	public static final ToolType BOW = new ToolType(ToolTypeFlags.BOW);
	public static final ToolType SWORD = new ToolType(ToolTypeFlags.SWORD);
	public static final ToolType FLINT_AND_STEEL = new ToolType(ToolTypeFlags.FLINT_AND_STEEL);
	public static final ToolType FISHING_ROD = new ToolType(ToolTypeFlags.FISHING_ROD);
	public static final ToolType SHEARS = new ToolType(ToolTypeFlags.SHEARS);
	public static final ToolType AXE = new ToolType(ToolTypeFlags.AXE);
	public static final ToolType PICKAXE = new ToolType(ToolTypeFlags.PICKAXE);
	public static final ToolType SPADE = new ToolType(ToolTypeFlags.SPADE);
	public static final ToolType HOE = new ToolType(ToolTypeFlags.HOE);
	private final Flag toolFlag;

	public ToolType(Flag toolFlag) {
		this.toolFlag = toolFlag;
	}

	/**
	 * Gets the tool flag for this particular type of tool
	 * @return tool flag
	 */
	public Flag getToolFlag() {
		return this.toolFlag;
	}

	/**
	 * Gets the block drop flag for this particular type of tool
	 * @return drop flag
	 */
	public Flag getDropFlag() {
		return this.toolFlag;
	}
}
