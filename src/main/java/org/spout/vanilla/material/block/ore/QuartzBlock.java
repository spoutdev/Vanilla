/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.material.block.ore;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.Solid;

public class QuartzBlock extends Solid implements Directional {
	public static final QuartzBlock QUARTZ_BLOCK = new QuartzBlock("Block of Quartz", VanillaMaterialModels.QUARTZ);
	public static final QuartzBlock CHISELED_QUARTZ_BLOCK = new QuartzBlock("Chiseled Quartz Block", QuartzBlockType.CHISELED, QUARTZ_BLOCK, VanillaMaterialModels.CHISELED_QUARTZ);
	public static final QuartzBlock PILLAR_QUARTZ = new QuartzBlock("Pillar Quartz Block", QuartzBlockType.PILLAR, QUARTZ_BLOCK, VanillaMaterialModels.PILLAR_QUARTZ);
	private static final BlockFaces DIRECTION_OPPOS = new BlockFaces(BlockFace.BOTTOM, BlockFace.NORTH, BlockFace.EAST);
	private static final BlockFaces DIRECTION_FACES = new BlockFaces(BlockFace.TOP, BlockFace.SOUTH, BlockFace.WEST, BlockFace.THIS);
	private static final byte directionMask = 0x6;
	private final QuartzBlockType type;

	private QuartzBlock(String name, String model) {
		super((short) 0x0003, name, 155, model);
		this.type = QuartzBlockType.QUARTZ;
		this.setHardness(0.8F).setResistance(1.3F).addMiningType(ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
	}

	private QuartzBlock(String name, QuartzBlockType type, QuartzBlock parent, String model) {
		super(name, 155, type.getData(), parent, model);
		this.type = type;
		this.setHardness(0.8F).setResistance(1.3F).addMiningType(ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
	}

	public QuartzBlockType getType() {
		return type;
	}

	@Override
	public QuartzBlock getParentMaterial() {
		return (QuartzBlock) super.getParentMaterial();
	}

	@Override
	public BlockFace getFacing(Block block) {
		QuartzBlock qBlock = (QuartzBlock) block;
		if (qBlock.getType() == QuartzBlockType.PILLAR) {
			return DIRECTION_FACES.get(block.getDataField(directionMask));
		}
		return BlockFace.THIS;
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		QuartzBlock qBlock = (QuartzBlock) block;
		if (qBlock.getType() == QuartzBlockType.PILLAR) {
			block.setDataField(directionMask, DIRECTION_FACES.indexOf(facing, 0));
		}
	}

	public static enum QuartzBlockType {
		QUARTZ(0),
		CHISELED(1),
		PILLAR(2),;
		private final short data;

		private QuartzBlockType(int data) {
			this.data = (short) data;
		}

		public short getData() {
			return this.data;
		}
	}
}
