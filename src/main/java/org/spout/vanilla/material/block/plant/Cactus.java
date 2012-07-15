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

import java.util.HashSet;
import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Plant;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.block.controlled.Furnace;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;

public class Cactus extends GroundAttachable implements Plant, TimedCraftable, DynamicMaterial {
	private static EffectRange dynamicRange = new CuboidEffectRange(0, 0, 0, 0, 1, 0);
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
		this.setHardness(0.4F).setResistance(0.7F).setTransparent();
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		if (VanillaConfiguration.CACTUS_PHYSICS.getBoolean()) {
			super.onUpdate(oldMaterial, block);
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

	@Override
	public int getGrowthStage(Block block) {
		block = getBase(block);
		// go up to the max stage count
		for (int i = 0; i < this.getNumGrowthStages(); i++) {
			block = block.translate(BlockFace.TOP);
			if (!block.isMaterial(this)) {
				return i;
			}
		}
		return this.getNumGrowthStages() - 1;
	}

	@Override
	public void setGrowthStage(Block block, int stage) {
		block = getBase(block);
		if (stage >= this.getNumGrowthStages()) {
			stage = this.getNumGrowthStages() - 1;
		}
		for (int i = 0; i < stage; i++) {
			block = block.translate(BlockFace.TOP);
			block.setMaterial(this);
		}
	}

	@Override
	public boolean addGrowthStage(Block block, int amount) {
		if (amount <= 0) {
			return false;
		}
		Block b = getBase(block).translate(BlockFace.TOP);
		boolean changed = false;
		for (int i = 1; i < getNumGrowthStages(); i++) {
			Block next = b.translate(0, i, 0);
			if (next.getMaterial() == VanillaMaterials.AIR) {
				changed = true;
				next.setMaterial(this);
				amount--;
				if (amount == 0) {
					break;
				}
			}
		}
		return changed;
	}

	/**
	 * Gets the base block this Cactus rests on
	 * 
	 * @param block of the Cactus
	 * @return the base Block
	 */
	public Block getBase(Block block) {
		// get the bottom block
		while (block.isMaterial(this)) {
			block = block.translate(BlockFace.BOTTOM);
		}
		return block;
	}
	
	@Override
	public boolean isFullyGrown(Block block) {
		return block.getData() == 0x3;
	}

	@Override
	public EffectRange getDynamicRange() {
		return dynamicRange;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		if (b.translate(0, -1, 0).getMaterial() == VanillaMaterials.SAND) {
			b.dynamicUpdate(currentTime + 1000 * 150);
		}
	}

	@Override
	public void onDynamicUpdate(Block b, Region r, long updateTime, long queuedTime, int data, Object hint) {
		this.addGrowthStage(b, 1);
		b.dynamicUpdate(r.getWorld().getAge() + 1000 * 150);
	}
}
