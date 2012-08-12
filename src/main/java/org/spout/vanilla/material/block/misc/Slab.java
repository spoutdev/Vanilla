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

import java.util.Set;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.util.bytebit.ByteBitSet;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.solid.DoubleSlab;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.ToolLevel;
import org.spout.vanilla.util.ToolType;

public class Slab extends VanillaBlockMaterial {
	public static final Slab STONE_SLAB = new Slab("Stone Slab");
	public static final Slab SANDSTONE_SLAB = new Slab("Sandstone Slab", 1, STONE_SLAB);
	public static final Slab WOOD_SLAB = new Slab("Wooden Slab", 2, STONE_SLAB);
	public static final Slab COBBLESTONE_SLAB = new Slab("Cobblestone Slab", 3, STONE_SLAB);
	public static final Slab BRICK_SLAB = new Slab("Brick Slab", 4, STONE_SLAB);
	public static final Slab STONE_BRICK_SLAB = new Slab("Stone Brick Slab", 5, STONE_SLAB);
	private DoubleSlab doubletype;
	private final ByteBitSet occlusionTop = new ByteBitSet(BlockFace.TOP);
	private final ByteBitSet occlusionBottom = new ByteBitSet(BlockFace.BOTTOM);

	private Slab(String name) {
		super((short) 0x0007, name, 44);
		this.setHardness(2.0F).setResistance(10.0F).setOpacity(0);
		this.setCollision(CollisionStrategy.SOLID);
		this.addMiningType(ToolType.AXE).addMiningType(ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
	}

	private Slab(String name, int data, Slab parent) {
		super(name, 44, data, parent);
		this.setHardness(2.0F).setResistance(10.0F).setOpacity(0);
		this.setCollision(CollisionStrategy.SOLID);
		this.addMiningType(ToolType.AXE).addMiningType(ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
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
		return block.isDataBitSet(0x8);
	}

	/**
	 * Sets if this half slab is the top-half
	 * @param block to set it for
	 * @param top state
	 */
	public void setTop(Block block, boolean top) {
		block.setDataBits(0x8, top);
	}

	@Override
	public Slab getSubMaterial(short data) {
		return (Slab) super.getSubMaterial(data);
	}

	@Override
	public boolean destroy(Block block, Set<Flag> flags) {
		if (!block.isMaterial(this.doubletype)) {
			return false;
		}
		return super.destroy(block, flags);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		if (!block.getMaterial().equals(this)) {
			return super.canPlace(block, data, against, clickedPos, isClickedBlock);
		}

		if (!isClickedBlock) {
			return true;
		}

		if (this.isTop(block)) {
			return against == BlockFace.BOTTOM;
		} else {
			return against == BlockFace.TOP;
		}
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		if (block.getMaterial().equals(this)) {
			block.setMaterial(this.doubletype);
		} else {
			block.setMaterial(this);
			this.setTop(block, against == BlockFace.TOP || (BlockFaces.NESW.contains(against) && clickedPos.getY() > 0.5f));
		}
		return true;
	}

	@Override
	public ByteBitSet getOcclusion(short data) {
		if ((data & 0x8) == 0x8) {
			return this.occlusionTop;
		} else {
			return this.occlusionBottom;
		}
	}
}
