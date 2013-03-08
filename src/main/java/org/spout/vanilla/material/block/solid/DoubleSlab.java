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
package org.spout.vanilla.material.block.solid;

import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.misc.Slab;

public class DoubleSlab extends Solid implements Burnable {
	public static final DoubleSlab STONE = new DoubleSlab((short) 0xF, "Stone Double Slab", 43, false, Slab.STONE, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab SANDSTONE = new DoubleSlab("Sandstone Double Slab", 0x1, STONE, Slab.SANDSTONE, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab STONE_WOOD = new DoubleSlab("Wooden Double Slab", 0x2, STONE, Slab.STONE_WOOD, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab COBBLESTONE = new DoubleSlab("Cobblestone Double Slab", 0x3, STONE, Slab.COBBLESTONE, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab BRICK = new DoubleSlab("Brick Double Slab", 0x4, STONE, Slab.BRICK, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab STONE_BRICK = new DoubleSlab("Stone Brick Double Slab", 0x5, STONE, Slab.STONE_BRICK, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab NETHER_BRICK = new DoubleSlab("Nether Brick Double Slab", 0x6, STONE, Slab.NETHER_BRICK, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab NETHER_QUARTZ = new DoubleSlab("Nether Quartz Double Slab", 0x7, STONE, Slab.QUARTZ, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab SMOOTH_STONE = new DoubleSlab("Smooth Stone Double Slab", 8, STONE, Slab.STONE, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab SMOOTH_SANDSTONE = new DoubleSlab("Smooth Sandstone Double Slab", 9, STONE, Slab.SANDSTONE, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab OAK_WOOD = new DoubleSlab((short) 0x3, "Oak Wooden Double Slab", 125, true, Slab.OAK_WOOD, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab SPRUCE_WOOD = new DoubleSlab("Spruce Wooden Double Slab", 0x1, OAK_WOOD, Slab.SPRUCE_WOOD, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab BIRCH_WOOD = new DoubleSlab("Birch Wooden Double Slab", 0x2, OAK_WOOD, Slab.BIRCH_WOOD, VanillaMaterialModels.DOUBLE_SLAB);
	public static final DoubleSlab JUNGLE_WOOD = new DoubleSlab("Jungle Wooden Double Slab", 0x3, OAK_WOOD, Slab.JUNGLE_WOOD, VanillaMaterialModels.DOUBLE_SLAB);
	private Slab singletype;
	private final boolean wood;

	private DoubleSlab(short datamask, String name, int id, boolean wood, Slab slab, String model) {
		super(datamask, name, id, model);
		this.wood = wood;
		this.setSingleType(slab).setHardness(2.0F).setResistance(10.0F);
		this.addMiningType(wood ? ToolType.AXE : ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
	}

	private DoubleSlab(String name, int data, DoubleSlab parent, Slab slab, String model) {
		super(name, parent.getMinecraftId(), data, parent, model);
		this.wood = parent.wood;
		this.setSingleType(slab).setHardness(2.0F).setResistance(10.0F);
		this.addMiningType(wood ? ToolType.AXE : ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
	}

	public Slab getSingleType() {
		return this.singletype;
	}

	public DoubleSlab setSingleType(Slab slab) {
		this.singletype = slab;
		if (slab.getDoubleType() == null) {
			slab.setDoubleType(this);
		}
		return this;
	}

	/**
	 * Gets if this Double Slab is made of Wood
	 * @return True if wooden, False if not
	 */
	public boolean isWooden() {
		return this.wood;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASS_DRUM;
	}

	@Override
	public int getBurnPower() {
		return isWooden() ? 5 : 0;
	}

	@Override
	public int getCombustChance() {
		return isWooden() ? 20 : 0;
	}
}
