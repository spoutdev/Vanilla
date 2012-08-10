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
package org.spout.vanilla.material.block.misc;

import org.spout.vanilla.data.drops.flag.ToolTypeFlags;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;

public class Web extends VanillaBlockMaterial implements Mineable {
	public Web(String name, int id) {
		super(name, id);
		this.setLiquidObstacle(false).setHardness(4.0F).setResistance(20.0F).setTransparent();
		this.getDrops().NOT_CREATIVE.addFlags(ToolTypeFlags.SWORD, ToolTypeFlags.SHEARS);
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}
}
