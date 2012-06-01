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
package org.spout.vanilla.material.block.plant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Plant;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.block.controlled.Furnace;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;

public class Cactus extends GroundAttachable implements Plant, TimedCraftable {
	private Set<BlockMaterial> allowedNeighbours = new HashSet<BlockMaterial>();

	public Cactus(String name, int id) {
		super(name, id);
		addAllowedNeighbour(VanillaMaterials.AIR,
				VanillaMaterials.TORCH,
				VanillaMaterials.REDSTONE_TORCH_OFF,
				VanillaMaterials.REDSTONE_TORCH_ON,
				VanillaMaterials.LEVER,
				VanillaMaterials.DEAD_BUSH,
				VanillaMaterials.TALL_GRASS,
				VanillaMaterials.REDSTONE_WIRE
		);
		this.setHardness(0.4F).setResistance(0.7F).setOpacity((byte) 0);
	}

	@Override
	public void onUpdate(Block block) {
		if (VanillaConfiguration.CACTUS_PHYSICS.getBoolean()) {
			super.onUpdate(block);
		}
	}

	@Override
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		if (super.canAttachTo(material, face)) {
			return material.equals(VanillaMaterials.SAND, VanillaMaterials.CACTUS);
		}
		return false;
	}

	@Override
	public boolean isValidPosition(Block block, BlockFace attachedFace, boolean seekAlternative) {
		if (super.isValidPosition(block, attachedFace, seekAlternative)) {
			BlockMaterial mat;
			for (BlockFace face : BlockFaces.NESW) {
				mat = block.translate(face).getMaterial();
				if (!this.allowedNeighbours.contains(mat)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return face == BlockFace.TOP && material.equals(VanillaMaterials.CACTUS);
	}

	public void addAllowedNeighbour(BlockMaterial... additions) {
		for (BlockMaterial mat : additions) {
			this.allowedNeighbours.add(mat);
		}
	}

	@Override
	public ItemStack getResult() {
		return new ItemStack(Dye.CACTUS_GREEN, 1);
	}

	@Override
	public float getCraftTime() {
		return Furnace.SMELT_TIME;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
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
}
