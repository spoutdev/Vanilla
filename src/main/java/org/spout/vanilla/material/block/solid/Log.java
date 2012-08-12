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
package org.spout.vanilla.material.block.solid;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.controlled.FurnaceBlock;
import org.spout.vanilla.material.block.plant.Sapling;
import org.spout.vanilla.material.item.misc.Coal;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Log extends Solid implements DynamicMaterial, Fuel, TimedCraftable, Burnable, Directional {
	public static final Log DEFAULT = new Log("Wood", Sapling.DEFAULT);
	public static final Log SPRUCE = new Log("Spruce Wood", 1, DEFAULT, Sapling.SPRUCE);
	public static final Log BIRCH = new Log("Birch Wood", 2, DEFAULT, Sapling.BIRCH);
	public static final Log JUNGLE = new Log("Jungle Wood", 3, DEFAULT, Sapling.JUNGLE);
	private static final BlockFaces DIRECTION_OPPOS = new BlockFaces(BlockFace.BOTTOM, BlockFace.NORTH, BlockFace.EAST);
	private static final BlockFaces DIRECTION_FACES = new BlockFaces(BlockFace.TOP, BlockFace.SOUTH, BlockFace.WEST, BlockFace.THIS);
	private static final short dataMask = 0x0003;
	private static final short directionMask = 0x00C;
	public static final short aliveMask = 0x0100;
	public static final short heightMask = 0x0600;
	private static final EffectRange dynamicRange = new CuboidEffectRange(-4, 0, -4, 4, 8, 4);
	public final float BURN_TIME = 15.f;
	private final Sapling sapling;

	private Log(String name, Sapling sapling) {
		super(dataMask, name, 17);
		this.setHardness(2.0F).setResistance(10.F).setStepSound(SoundEffects.STEP_WOOD).setOpacity((byte) 1);
		this.sapling = sapling;
	}

	private Log(String name, int data, Log parent, Sapling sapling) {
		super(name, 17, data, parent);
		this.setHardness(2.0F).setResistance(10.F).setStepSound(SoundEffects.STEP_WOOD).setOpacity((byte) 1);
		this.sapling = sapling;
	}

	/**
	 * Gets the sapling associated with this Log
	 * @return the sapling
	 */
	public Sapling getSapling() {
		return sapling;
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public int getBurnPower() {
		return 5;
	}

	@Override
	public int getCombustChance() {
		return 5;
	}

	@Override
	public ItemStack getResult() {
		return new ItemStack(Coal.CHARCOAL, 1);
	}

	@Override
	public float getCraftTime() {
		return FurnaceBlock.SMELT_TIME;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (material.equals(VanillaMaterials.FIRE)) {
			return true;
		} else {
			return super.canSupport(material, face);
		}
	}

	@Override
	public BlockFace getFacing(Block block) {
		return DIRECTION_FACES.get(block.getDataField(directionMask));
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		if (DIRECTION_OPPOS.contains(facing)) {
			facing = facing.getOpposite();
		}
		block.setDataField(directionMask, DIRECTION_FACES.indexOf(facing, 0));
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		block.setMaterial(this);
		this.setFacing(block, VanillaPlayerUtil.getBlockFacing(block));
		return true;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		if (b.isDataBitSet(aliveMask)) {
			b.dynamicUpdate(currentTime + 10000);
		}
	}

	@Override
	public void onDynamicUpdate(Block b, Region r, long updateTime, int updateData) {
		int data = b.getData() & 0xFFFF;
		if ((data & aliveMask) == 0) {
			return;
		}

		Block trunk = b;
		int expectHeight = b.getDataField(heightMask);
		for (int i = 0; i < expectHeight + 1; i++) {
			if (!(trunk.getMaterial() instanceof Log)) {
				return;
			}
			trunk = trunk.translate(BlockFace.TOP);
		}
		Material trunkMaterial = trunk.getMaterial();
		if (expectHeight == 3) {
			trunk = b;
			for (int i = 0; i < expectHeight + 1; i++) {
				trunk.setMaterial(BlockMaterial.AIR);
				trunk = trunk.translate(BlockFace.TOP);
			}
			sapling.growTree(b, sapling);
			return;
		}

		if (trunkMaterial == BlockMaterial.AIR || trunkMaterial instanceof Leaves) {
			trunk.setMaterial(b.getMaterial());
			trunk = trunk.translate(BlockFace.TOP);
			if (trunk.getMaterial() == BlockMaterial.AIR) {
				trunk.setMaterial(Leaves.DEFAULT);
				trunk.setData(data & dataMask);
			}
			b.setDataField(heightMask, expectHeight + 1);
			b.dynamicUpdate(updateTime + 10000);
		} else {
			b.setData(data & dataMask);
		}
	}

	@Override
	public EffectRange getDynamicRange() {
		return dynamicRange;
	}
}
