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

import org.spout.vanilla.material.MovingBlock;
import org.spout.vanilla.material.generic.GenericBlock;

public class DoubleSlab extends GenericBlock implements MovingBlock {
	public static final DoubleSlab STONE = register(new DoubleSlab("Stone Double Slab"));
	public static final DoubleSlab SANDSTONE = register(new DoubleSlab("Sandstone Double Slab", 1, STONE));
	public static final DoubleSlab WOOD = register(new DoubleSlab("Wooden Double Slab", 2, STONE));
	public static final DoubleSlab COBBLESTONE = register(new DoubleSlab("Cobblestone Double Slab", 3, STONE));
	public static final DoubleSlab BRICK = register(new DoubleSlab("Brick Double Slab", 4, STONE));
	public static final DoubleSlab STONE_BRICK = register(new DoubleSlab("Stone Brick Double Slab", 5, STONE));

	private DoubleSlab(String name) {
		super(name, 43);
		this.setDefault();
	}

	private DoubleSlab(String name, int data, DoubleSlab parent) {
		super(name, 43, data, parent);
		this.setDefault();
	}

	private void setDefault() {
		this.setHardness(2.0F).setResistance(10.0F);
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
}
