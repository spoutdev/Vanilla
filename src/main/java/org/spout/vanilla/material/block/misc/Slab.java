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

import java.util.ArrayList;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.LogicUtil;

import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.solid.DoubleSlab;
import org.spout.vanilla.material.item.tool.Pickaxe;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Slab extends VanillaBlockMaterial implements Mineable {
	public static final Slab STONE = new Slab("Stone Slab");
	public static final Slab SANDSTONE = new Slab("Sandstone Slab", 1, STONE);
	public static final Slab WOOD = new Slab("Wooden Slab", 2, STONE);
	public static final Slab COBBLESTONE = new Slab("Cobblestone Slab", 3, STONE);
	public static final Slab BRICK = new Slab("Brick Slab", 4, STONE);
	public static final Slab STONE_BRICK = new Slab("Stone Brick Slab", 5, STONE);
	private DoubleSlab doubletype;

	private Slab(String name) {
		super(name, 44);
		this.setHardness(2.0F).setResistance(10.0F).setOpacity((byte) 1);
	}

	private Slab(String name, int data, Slab parent) {
		super(name, 44, data, parent);
		this.setHardness(2.0F).setResistance(10.0F).setOpacity((byte) 1);
	}

	public Slab setDoubleType(DoubleSlab doubletype) {
		this.doubletype = doubletype;
		return this;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSDRUM;
	}

	public DoubleSlab getDoubleType() {
		return this.doubletype;
	}

	/**
	 * Gets if this half slab is the top-half
	 * @param block to get it of
	 * @return True if it is the block half
	 */
	public boolean isTop(Block block) {
		return LogicUtil.getBit(block.getData(), 0x8);
	}

	/**
	 * Sets if this half slab is the top-half
	 * @param block to set it for
	 * @param top state
	 */
	public void setTop(Block block, boolean top) {
		block.setData(LogicUtil.setBit(block.getData(), 0x8, top));
	}

	@Override
	public Slab getSubMaterial(short data) {
		return (Slab) super.getSubMaterial(LogicUtil.setBit(data, 0x8, false));
	}

	@Override
	public void onDestroy(Block block, double dropChance) {
		if (!block.getSubMaterial().equals(this.doubletype)) {
			super.onDestroy(block, dropChance);
		}
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (block.getSubMaterial().equals(this)) {
			if (isClickedBlock) {
				if (this.isTop(block)) {
					return against == BlockFace.BOTTOM;
				} else {
					return against == BlockFace.TOP;
				}
			} else {
				return true;
			}
		} else {
			return super.canPlace(block, data, against, isClickedBlock);
		}
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (block.getSubMaterial().equals(this)) {
			block.setMaterial(this.doubletype).update();
		} else {
			block.setMaterial(this);
			this.setTop(block, against == BlockFace.TOP);
			block.update();
		}
		return true;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Pickaxe ? (short) 1 : (short) 2;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		ItemStack held = VanillaPlayerUtil.getCurrentItem(block.getSource());
		if (held != null && held.getMaterial() instanceof Pickaxe) {
			drops.add(new ItemStack(this, 1));
		}
		return drops;
	}
}
