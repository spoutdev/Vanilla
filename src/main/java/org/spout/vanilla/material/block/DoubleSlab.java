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

import org.spout.api.material.SubMaterial;
import org.spout.vanilla.material.MovingBlock;
import org.spout.vanilla.material.generic.GenericBlock;

public class DoubleSlab extends GenericBlock implements MovingBlock, SubMaterial {
	public final DoubleSlab STONE;
	public final DoubleSlab SANDSTONE;
	public final DoubleSlab WOOD;
	public final DoubleSlab COBBLESTONE;
	public final DoubleSlab BRICK;
	public final DoubleSlab STONE_BRICK;

	private final DoubleSlab parent;
	private final short data;
	
	private void setDefault() {
		this.setHardness(2.0F).setResistance(10.0F);
	}
	
	private DoubleSlab(String name, int data, DoubleSlab parent) {
		super(name, 43);
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
	
	public DoubleSlab(String name) {
		super(name, 43);
		this.setDefault();
		this.parent = this;
		this.data = 0;
		this.register();
		
		this.STONE = new DoubleSlab("Stone Double Slab", 0, this);
		this.SANDSTONE = new DoubleSlab("Sandstone Double Slab", 1, this);
		this.WOOD = new DoubleSlab("Wooden Double Slab", 2, this);
		this.COBBLESTONE = new DoubleSlab("Cobblestone Double Slab", 3, this);
		this.BRICK = new DoubleSlab("Brick Double Slab", 4, this);
		this.STONE_BRICK = new DoubleSlab("Stone Brick Double Slab", 5, this);
	}
	
	public DoubleSlab setSlabMaterial(Slab slab) {
		this.setDrop(slab);
		slab.setDoubleSlabMaterial(this);
		return this;
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
	public DoubleSlab getParentMaterial() {
		return this.parent;
	}
}
