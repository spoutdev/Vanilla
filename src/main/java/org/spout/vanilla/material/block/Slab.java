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
import org.spout.api.material.SubMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.material.MovingBlock;
import org.spout.vanilla.material.generic.GenericBlock;

public class Slab extends GenericBlock implements MovingBlock, SubMaterial {
	public final Slab STONE;
	public final Slab SANDSTONE;
	public final Slab WOOD;
	public final Slab COBBLESTONE;
	public final Slab BRICK;
	public final Slab STONE_BRICK;
	
	private final Slab parent;
	private final short data;
	private DoubleSlab doubletype = VanillaMaterials.DOUBLE_SLABS;
	
	private void setDefault() {
		this.setHardness(2.0F).setResistance(10.0F);
	}
	
	private Slab(String name, int data, Slab parent) {
		super(name, 32);
		this.setDefault();
		this.parent = parent;
		this.data = (short) data;
		parent.registerSubMaterial(this);
		this.register();

		this.STONE = parent.STONE;
		this.SANDSTONE = parent.SANDSTONE;
		this.WOOD = parent.WOOD;
		this.COBBLESTONE = parent.COBBLESTONE;
		this.BRICK = parent.BRICK;
		this.STONE_BRICK = parent.STONE_BRICK;
	}
	
	public Slab(String name) {
		super(name, 32);
		this.setDefault();
		this.parent = this;
		this.data = 0;
		this.register();

		this.STONE = new Slab("Stone Slab", 0, this);
		this.SANDSTONE = new Slab("Sandstone Slab", 1, this);
		this.WOOD = new Slab("Wooden Slab", 2, this);
		this.COBBLESTONE = new Slab("Cobblestone Slab", 3, this);
		this.BRICK = new Slab("Brick Slab", 4, this);
		this.STONE_BRICK = new Slab("Stone Brick Slab", 5, this);
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

	@Override
	public boolean isMoving() {
		return false;
	}

	@Override
	public short getData() {
		return this.data;
	}

	@Override
	public Slab getParentMaterial() {
		return this.parent;
	}
}
