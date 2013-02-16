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
package org.spout.vanilla.material.block.fence;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.drops.flag.ToolTypeFlags;
import org.spout.vanilla.material.block.PressurePlate;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.component.SignBase;
import org.spout.vanilla.material.block.misc.Torch;
import org.spout.vanilla.data.resources.VanillaMaterialModels;

public class Wall extends Solid {
	public static final Wall COBBLESTONE_WALL = new Wall("Cobblestone Wall", VanillaMaterialModels.COBBLESTONE_WALL);
	public static final Wall MOSSY_COBBLESTONE_WALL = new Wall("Mossy Cobblestone Wall", (short) 1, COBBLESTONE_WALL, VanillaMaterialModels.MOSSY_COBBLESTONE_WALL);

	private Wall(String name, String model) {
		super((short) 0x1, name, 139, model);
		this.setHardness(1.5F).setResistance(10.0F);
		this.getDrops().NOT_CREATIVE.addFlags(ToolTypeFlags.PICKAXE);
	}

	private Wall(String name, int data, Wall parent, String model) {
		super(name, parent.getMinecraftId(), data, parent, model);
		this.setHardness(1.5F).setResistance(10.0F);
		this.getDrops().NOT_CREATIVE.addFlags(ToolTypeFlags.PICKAXE);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material instanceof SignBase) {
			return true;
		}
		if (face == BlockFace.TOP) {
			if (material instanceof Torch || material instanceof PressurePlate) {
				return true;
			}
		}
		return false;
	}
}
