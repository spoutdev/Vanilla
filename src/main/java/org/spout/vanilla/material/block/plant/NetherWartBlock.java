/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.block.plant;

import java.util.ArrayList;
import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.DataSource;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;

public class NetherWartBlock extends GroundAttachable implements Plant {
	private GrowthStage stage = GrowthStage.SEEDLING;

	public NetherWartBlock(String name, int id) {
		super(name, id);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setResistance(0.0F);
	}

	@Override
	public boolean hasGrowthStages() {
		return true;
	}

	@Override
	public int getNumGrowthStages() {
		return 3;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 0;
	}

	@Override
	public short getData() {
		return stage.getData();
	}

	@Override
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		return material.equals(VanillaMaterials.SOUL_SAND) && super.canAttachTo(material, face);
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(VanillaMaterials.NETHER_WART, stage == GrowthStage.LAST ? new Random().nextInt(4) + 2 : 1));
		return drops;
	}

	public GrowthStage getGrowthStage() {
		return stage;
	}

	public enum GrowthStage implements DataSource {
		SEEDLING(1),
		MIDDLE(2),
		LAST(3);
		private final short data;

		GrowthStage(int data) {
			this.data = (short) data;
		}

		@Override
		public short getData() {
			return data;
		}
	}
}
