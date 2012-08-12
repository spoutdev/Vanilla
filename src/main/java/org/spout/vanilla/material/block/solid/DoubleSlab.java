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
package org.spout.vanilla.material.block.solid;

import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.misc.Slab;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.ToolLevel;
import org.spout.vanilla.util.ToolType;

public class DoubleSlab extends Solid {
	public static final DoubleSlab STONE_SLAB = new DoubleSlab("Stone Double Slab", Slab.STONE_SLAB);
	public static final DoubleSlab SANDSTONE_SLAB = new DoubleSlab("Sandstone Double Slab", 1, STONE_SLAB, Slab.SANDSTONE_SLAB);
	public static final DoubleSlab WOOD_SLAB = new DoubleSlab("Wooden Double Slab", 2, STONE_SLAB, Slab.WOOD_SLAB);
	public static final DoubleSlab COBBLESTONE_SLAB = new DoubleSlab("Cobblestone Double Slab", 3, STONE_SLAB, Slab.COBBLESTONE_SLAB);
	public static final DoubleSlab BRICK_SLAB = new DoubleSlab("Brick Double Slab", 4, STONE_SLAB, Slab.BRICK_SLAB);
	public static final DoubleSlab STONE_BRICK_SLAB = new DoubleSlab("Stone Brick Double Slab", 5, STONE_SLAB, Slab.STONE_BRICK_SLAB);
	private Slab singletype;

	public Slab getSingleType() {
		return this.singletype;
	}

	public DoubleSlab setSingleType(Slab slab) {
		this.singletype = slab;
		slab.setDoubleType(this);
		return this;
	}

	private DoubleSlab(String name, Slab slab) {
		super((short) 0x0007, name, 43);
		this.setSingleType(slab).setHardness(2.0F).setResistance(10.0F).setMiningType(ToolType.PICKAXE, ToolLevel.WOOD);
	}

	private DoubleSlab(String name, int data, DoubleSlab parent, Slab slab) {
		super(name, 43, data, parent);
		this.setSingleType(slab).setHardness(2.0F).setResistance(10.0F).setMiningType(ToolType.PICKAXE, ToolLevel.WOOD);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSDRUM;
	}
}
