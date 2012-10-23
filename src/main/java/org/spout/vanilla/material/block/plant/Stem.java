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

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Crop;
import org.spout.vanilla.material.block.Growing;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.util.VanillaBlockUtil;

public abstract class Stem extends GroundAttachable implements Growing, Crop, DynamicMaterial {
	private BlockMaterial lastMaterial;

	public Stem(String name, int id) {
		super(name, id);
		this.setLiquidObstacle(false);
		this.setResistance(0.0F).setHardness(0.0F).setTransparent();
	}

	@Override
	public int getGrowthStageCount() {
		return 8;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 9;
	}

	@Override
	public int getGrowthStage(Block block) {
		return block.getDataField(0x7);
	}

	@Override
	public void setGrowthStage(Block block, int stage) {
		block.setData(stage & 0x7);
	}

	@Override
	public boolean isFullyGrown(Block block) {
		return block.getData() == 0x7;
	}

	/**
	 * Sets the material placed after this Stem is fully grown
	 * @param material of the last stage
	 */
	public void setLastStageMaterial(BlockMaterial material) {
		this.lastMaterial = material;
	}

	/**
	 * Gets the material placed after this Stem is fully grown
	 * @return material of the last stage
	 */
	public BlockMaterial getLastStageMaterial() {
		return this.lastMaterial;
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		return face == BlockFace.TOP && block.isMaterial(VanillaMaterials.FARMLAND);
	}

	@Override
	public void onInteractBy(Entity entity, Block block, PlayerInteractEvent.Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		PlayerQuickbar inv = entity.get(Human.class).getInventory().getQuickbar();
		ItemStack current = inv.getCurrentItem();
		if (current != null && current.isMaterial(Dye.BONE_MEAL)) {
			if (this.getGrowthStage(block) != 0x7) {
				if (entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.SURVIVAL)) {
					inv.addAmount(0, -1);
				}
				this.setGrowthStage(block, 0x7);
			}
		}
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS_AND_NEIGHBORS;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		//TODO : Delay before first grow
		b.dynamicUpdate(10000 + currentTime);
	}

	@Override
	public void onDynamicUpdate(Block block, Region region, long updateTime, int data) {
		if (block.translate(BlockFace.TOP).getLight() < this.getMinimumLightToGrow()) {
			block.dynamicUpdate(updateTime + 10000);
			return;
		}
		int chance = VanillaBlockUtil.getCropGrowthChance(block) + 1;
		Random rand = new Random(block.getWorld().getAge());
		if (rand.nextInt(chance) == 0) {
			if (isFullyGrown(block)) {
				for (BlockFace face : BlockFaces.NESW) {
					if (block.translate(face).isMaterial(this.getLastStageMaterial())) {
						return;
					}
				}
				Block spread = block.translate(BlockFaces.NESW.get(rand.nextInt(4)));
				if (spread.isMaterial(VanillaMaterials.AIR)) {
					BlockMaterial belowSpread = spread.translate(BlockFace.BOTTOM).getMaterial();
					if (belowSpread.isMaterial(VanillaMaterials.FARMLAND, VanillaMaterials.DIRT, VanillaMaterials.GRASS)) {
						spread.setMaterial(this.getLastStageMaterial());
					}
				}
			} else {
				block.addData(1);
			}
		}

		block.dynamicUpdate(updateTime + 10000);
	}
}
