/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.generic.Solid;

public class Slab extends Solid {
	public static final Slab STONE = register(new Slab("Stone Slab"));
	public static final Slab SANDSTONE = register(new Slab("Sandstone Slab", 1, STONE));
	public static final Slab WOOD = register(new Slab("Wooden Slab", 2, STONE));
	public static final Slab COBBLESTONE = register(new Slab("Cobblestone Slab", 3, STONE));
	public static final Slab BRICK = register(new Slab("Brick Slab", 4, STONE));
	public static final Slab STONE_BRICK = register(new Slab("Stone Brick Slab", 5, STONE));
	private DoubleSlab doubletype = VanillaMaterials.DOUBLE_SLABS;

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

	public void setDoubleSlabMaterial(DoubleSlab material) {
		this.doubletype = material;
	}

	@Override
	public boolean onPlacement(World world, int x, int y, int z, short data, BlockFace against, Source source) {
		if (against == BlockFace.BOTTOM) {
			Block below = world.getBlock(x, y, z).move(against);
			if (below.getMaterial() == this.getParentMaterial() && below.getData() == data) {
				//we are stacking on top of another of the same type
				//turn this block into the double type
				Material slab = this.getSubMaterial(data);
				if (slab != null && slab instanceof Slab) {
					below.setBlock(((Slab) slab).doubletype);
					return true;
				}
			}
		}
		return super.onPlacement(world, x, y, z, data, against, source);
	}
}
