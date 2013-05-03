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
package org.spout.vanilla.material.block.plant;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Slot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.GenericMath;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Crop;
import org.spout.vanilla.material.block.Growing;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.util.PlayerUtil;

public abstract class Stem extends GroundAttachable implements Growing, Crop, DynamicMaterial {
	private BlockMaterial lastMaterial;

	public Stem(String name, int id) {
		super(name, id, null);
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
	public boolean grow(Block block, Material material) {
		int stage = this.getGrowthStage(block);
		if (!isFullyGrown(block) && material.isMaterial(Dye.BONE_MEAL) && stage != 0x7) {
			stage += GenericMath.getRandom().nextInt(3) + 2;
			if (stage > 0x7) {
				stage = 0x7;
			}
			this.setGrowthStage(block, stage);
			return true;
		}
		return false;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, PlayerInteractEvent.Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		Slot inv = PlayerUtil.getHeldSlot(entity);
		if (inv != null && inv.get() != null && type.equals(Action.RIGHT_CLICK)) {
			if (grow(block, inv.get().getMaterial()) && !PlayerUtil.isCostSuppressed(entity)) {
				inv.addAmount(-1);
			}
		}
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS_AND_NEIGHBORS;
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
		b.dynamicUpdate(getGrowthTime(b) + currentTime, true);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		if (block.translate(BlockFace.TOP).getLight() < this.getMinimumLightToGrow()) {
			block.dynamicUpdate(updateTime + getGrowthTime(block), true);
			return;
		}
		int chance = VanillaBlockMaterial.getCropGrowthChance(block) + 1;
		final Random rand = GenericMath.getRandom();
		if (rand.nextInt(chance) == 0) {
			if (isFullyGrown(block)) {
				for (int i = 0; i < BlockFaces.NESW.size(); i++) {
					Block spread = block.translate(BlockFaces.NESW.get(i));
					BlockMaterial material = spread.getMaterial();
					if (material == VanillaMaterials.AIR) {
						BlockMaterial belowSpread = spread.translate(BlockFace.BOTTOM).getMaterial();
						if (belowSpread.isMaterial(VanillaMaterials.FARMLAND, VanillaMaterials.DIRT, VanillaMaterials.GRASS)) {
							spread.setMaterial(this.getLastStageMaterial());
							break;
						}
					} else if (material == getLastStageMaterial()) {
						break;
					}
				}
			} else {
				block.addData(1);
			}
		}

		block.dynamicUpdate(updateTime + getGrowthTime(block), true);
	}

	protected long getGrowthTime(Block block) {
		return 10000L + GenericMath.getRandom().nextInt(60000);
	}
}
