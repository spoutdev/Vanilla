/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.block;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.Solid;

public class Slab extends Solid {
	public static final Slab STONE = register(new Slab("Stone Slab"));
	public static final Slab SANDSTONE = register(new Slab("Sandstone Slab", 1, STONE));
	public static final Slab WOOD = register(new Slab("Wooden Slab", 2, STONE));
	public static final Slab COBBLESTONE = register(new Slab("Cobblestone Slab", 3, STONE));
	public static final Slab BRICK = register(new Slab("Brick Slab", 4, STONE));
	public static final Slab STONE_BRICK = register(new Slab("Stone Brick Slab", 5, STONE));
	private DoubleSlab doubletype;

	public Slab setDoubleType(DoubleSlab doubletype) {
		this.doubletype = doubletype;
		return this;
	}

	public DoubleSlab getDoubleType() {
		return this.doubletype;
	}

	private Slab(String name) {
		super(name, 44);
		this.setDefault();
	}

	private Slab(String name, int data, Slab parent) {
		super(name, 44, data, parent);
		this.setDefault();
	}

	private void setDefault() {
		this.setHardness(2.0F).setResistance(10.0F);
	}

	@Override
	public void onDestroy(Block block) {
		if (!block.getSubMaterial().equals(this.doubletype)) {
			super.onDestroy(block);
		}
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (block.getSubMaterial().equals(this)) {
			return !isClickedBlock || against == BlockFace.TOP;
		} else {
			return super.canPlace(block, data, against, isClickedBlock);
		}
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (block.getSubMaterial().equals(this)) {
			block.setMaterial(this.doubletype).update();
			return true;
		} else {
			return super.onPlacement(block, data, against, isClickedBlock);
		}
	}
}
