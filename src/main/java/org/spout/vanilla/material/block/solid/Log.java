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
package org.spout.vanilla.material.block.solid;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.api.event.Cause;
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
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.flag.Flag;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.Instrument;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.component.FurnaceBlock;
import org.spout.vanilla.material.block.plant.Sapling;
import org.spout.vanilla.material.item.misc.Coal;

public class Log extends Solid implements DynamicMaterial, Fuel, TimedCraftable, Burnable, Directional {
	private static final BlockFaces DIRECTION_OPPOS = new BlockFaces(BlockFace.BOTTOM, BlockFace.NORTH, BlockFace.EAST);
	private static final BlockFaces DIRECTION_FACES = new BlockFaces(BlockFace.TOP, BlockFace.SOUTH, BlockFace.WEST, BlockFace.THIS);
	private static final short dataMask = 0x0003;
	private static final short directionMask = 0x00C;
	public static final short aliveMask = 0x0100;
	public static final short heightMask = 0x0600;
	private static final EffectRange dynamicRange = new CuboidEffectRange(-4, 0, -4, 4, 8, 4);
	
	public static final Log DEFAULT = new Log("Wood", Sapling.DEFAULT, "model://Vanilla/materials/block/solid/oakwood/oakwood.spm");
	public static final Log SPRUCE = new Log("Spruce Wood", 1, DEFAULT, Sapling.SPRUCE, "model://Vanilla/materials/block/solid/sprucewood/sprucewood.spm");
	public static final Log BIRCH = new Log("Birch Wood", 2, DEFAULT, Sapling.BIRCH, "model://Vanilla/materials/block/solid/birchwood/birchwood.spm");
	public static final Log JUNGLE = new Log("Jungle Wood", 3, DEFAULT, Sapling.JUNGLE, "model://Vanilla/materials/block/solid/junglewood/junglewood.spm");
	
	public final float BURN_TIME = 15;
	private final Sapling sapling;

	private Log(String name, Sapling sapling, String model) {
		super(dataMask, name, 17, model);
		this.setHardness(2.0F).setResistance(10.F).setStepSound(SoundEffects.STEP_WOOD);
		this.addMiningType(ToolType.AXE);
		this.sapling = sapling;
	}

	private Log(String name, int data, Log parent, Sapling sapling, String model) {
		super(name, 17, data, parent, model);
		this.setHardness(2.0F).setResistance(10.F).setStepSound(SoundEffects.STEP_WOOD);
		this.addMiningType(ToolType.AXE);
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
		return Instrument.BASS_GUITAR;
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
		return 25;
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
	public void onPostDestroy(Block block, Set<Flag> flags) {
		super.onPostDestroy(block, flags);
		LeafDecayTask decay = new LeafDecayTask(block);
		decay.scheduleTask(block.getRegion());
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setFacing(block, against);
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
		if (b.isDataBitSet(aliveMask)) {
			b.dynamicUpdate(currentTime + getGrowthTime(b), true);
		}
	}

	@Override
	public void onDynamicUpdate(Block b, long updateTime, int updateData) {
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
			b.dynamicUpdate(updateTime + getGrowthTime(b), true);
		} else {
			b.setData(data & dataMask);
		}
	}

	private long getGrowthTime(Block block) {
		return 60000L + (new Random(block.getWorld().getAge()).nextInt(60000)) * 3;
	}

	@Override
	public EffectRange getDynamicRange() {
		return dynamicRange;
	}

	private static class LeafDecayTask implements Runnable {
		private final static int SCAN_RADIUS = 3;
		private final static int LOG_SCAN_RADIUS = 2;
		private final Set<Block> blocks = new HashSet<Block>(SCAN_RADIUS * SCAN_RADIUS * SCAN_RADIUS);
		private final Region r;

		public LeafDecayTask(Block b) {
			this.r = b.getRegion();
			for (int dx = -SCAN_RADIUS; dx < SCAN_RADIUS; dx++) {
				for (int dy = -SCAN_RADIUS; dy < SCAN_RADIUS; dy++) {
					for (int dz = -SCAN_RADIUS; dz < SCAN_RADIUS; dz++) {
						blocks.add(b.translate(dx, dy, dz));
					}
				}
			}
		}

		@Override
		public void run() {
			boolean found = false;
			for (Block b : blocks) {
				if (b.getChunk().isLoaded()) {
					if (b.getMaterial().getId() == VanillaMaterials.LEAVES.getId()) {
						if (!isLeafAttached(b)) {
							b.setMaterial(VanillaMaterials.AIR);
							found = true;
							break;
						}
					}
				}
			}
			if (found) {
				scheduleTask(this.r);
			}
		}

		public void scheduleTask(Region r) {
			r.getTaskManager().scheduleSyncDelayedTask(VanillaPlugin.getInstance(), this, (new Random()).nextInt(200) * 50, TaskPriority.LOW);
		}

		private boolean isLeafAttached(Block b) {
			for (int dx = -LOG_SCAN_RADIUS; dx < LOG_SCAN_RADIUS; dx++) {
				for (int dy = -LOG_SCAN_RADIUS; dy < LOG_SCAN_RADIUS; dy++) {
					for (int dz = -LOG_SCAN_RADIUS; dz < LOG_SCAN_RADIUS; dz++) {
						Block block = b.translate(dx, dy, dz);
						if (block.getMaterial().getId() == VanillaMaterials.LOG.getId()) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}
}
