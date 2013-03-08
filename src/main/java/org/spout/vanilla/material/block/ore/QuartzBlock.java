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
package org.spout.vanilla.material.block.ore;

import org.spout.api.material.source.DataSource;

import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.block.Solid;

public class QuartzBlock extends Solid {
	public static final QuartzBlock QUARTZ_BLOCK = new QuartzBlock("Block of Quartz", VanillaMaterialModels.QUARTZ);
	public static final QuartzBlock CHISELED_QUARTZ_BLOCK = new QuartzBlock("Chiseled Quartz Block", QuartzBlockType.CHISELED, QUARTZ_BLOCK, VanillaMaterialModels.CHISELED_QUARTZ);
	public static final QuartzBlock PILLAR_QUARTZ = new QuartzBlock("Pillar Quartz Block", QuartzBlockType.PILLAR, QUARTZ_BLOCK, VanillaMaterialModels.PILLAR_QUARTZ);
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

	public static enum QuartzBlockType implements DataSource {
		QUARTZ(0),
		CHISELED(1),
		PILLAR(2),;
		private final short data;

		private QuartzBlockType(int data) {
			this.data = (short) data;
		}

		@Override
		public short getData() {
			return this.data;
		}
	}
}
