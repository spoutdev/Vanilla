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
package org.spout.vanilla.material.block.plant;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.player.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Slot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.GenericMath;

import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Growing;
import org.spout.vanilla.material.block.Spreading;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.block.solid.Log;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.util.PlayerUtil;
import org.spout.vanilla.world.generator.normal.object.tree.TreeObject;

public class Sapling extends GroundAttachable implements Growing, Spreading, Plant, Fuel, DynamicMaterial {
	public static final Sapling DEFAULT = new Sapling("Sapling");
	public static final Sapling SPRUCE = new Sapling("Spruce Sapling", 1, DEFAULT);
	public static final Sapling BIRCH = new Sapling("Birch Sapling", 2, DEFAULT);
	public static final Sapling JUNGLE = new Sapling("Jungle Sapling", 3, DEFAULT);
	public final float BURN_TIME = 5;
	private static final short dataMask = 0x3;
	private static final int GROWTH_MASK = 0x8;

	private Sapling(String name) {
		super(dataMask, name, 6, null, null);
		this.setLiquidObstacle(false).setHardness(0.0F).setResistance(0.0F).setTransparent().setGhost(true);
	}

	private Sapling(String name, int data, Sapling parent) {
		super(name, 6, data, parent, null, null);
		this.setLiquidObstacle(false).setHardness(0.0F).setResistance(0.0F).setTransparent().setGhost(true);
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public int getMinimumLightToSpread() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (super.canAttachTo(block, face)) {
			return block.isMaterial(VanillaMaterials.GRASS, VanillaMaterials.DIRT);
		}
		return false;
	}

	@Override
	public boolean grow(Block block, Material material) {
		if (material.isMaterial(Dye.BONE_MEAL)) {
			int stage = getGrowthStage(block);
			if (stage == 0) {
				setGrowthStage(block, 1);
			} else {
				this.growTree(block);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type != Action.RIGHT_CLICK) {
			return;
		}
		Slot inv = PlayerUtil.getHeldSlot(entity);
		if (inv != null && inv.get() != null && inv.get().isMaterial(Dye.BONE_MEAL)) {
			if (GenericMath.getRandom().nextDouble() < 0.45D && grow(block, inv.get().getMaterial()) && !PlayerUtil.isCostSuppressed(entity)) {
				inv.addAmount(-1);
			}
		}
	}

	/**
	 * Grows a full-sized tree from the sapling at the block given
	 * @param block to place a tree at
	 */
	public void growTree(Block block) {
		BlockMaterial mat = block.getMaterial();
		if (mat instanceof Sapling) {
			this.growTree(block, (Sapling) mat);
		}
	}

	/**
	 * Grows a full-sized tree from the sapling type given
	 * @param block to grow a tree at
	 * @param type of tree
	 */
	public void growTree(Block block, Sapling type) {
		TreeObject.growTree(type, block, GenericMath.getRandom());
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		block.clearDataBits((short) (~dataMask));
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
		b.dynamicUpdate(currentTime + getGrowthTime(b), true);
	}

	@Override
	public void onDynamicUpdate(Block b, long updateTime, int data) {
		short oldData = b.getBlockData();
		b.setMaterial(Log.DEFAULT);
		b.setData(oldData & dataMask);
		b.setDataBits(Log.aliveMask);
	}

	private long getGrowthTime(Block block) {
		return 240000L + GenericMath.getRandom().nextInt(240000);
	}

	@Override
	public int getMinimumLightToGrow() {
		return 9;
	}

	@Override
	public int getGrowthStageCount() {
		return 2;
	}

	@Override
	public int getGrowthStage(Block block) {
		return block.getDataField(GROWTH_MASK);
	}

	@Override
	public void setGrowthStage(Block block, int stage) {
		block.setDataField(GROWTH_MASK, stage);
	}

	@Override
	public boolean isFullyGrown(Block block) {
		return false; //When fully grown saplings generate trees.
	}
}
