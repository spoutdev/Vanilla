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
package org.spout.vanilla.material.block.solid;

import java.util.ArrayList;
import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.block.Plant;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.controlled.Furnace;
import org.spout.vanilla.material.item.misc.Coal;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.world.generator.normal.object.SmallTreeObject;
import org.spout.vanilla.world.generator.normal.object.SmallTreeObject.SmallTreeType;

public class Log extends Solid implements Plant, Fuel, TimedCraftable, Mineable, DynamicMaterial {
	public static final Log DEFAULT = new Log("Wood");
	public static final Log SPRUCE = new Log("Spruce Wood", 1, DEFAULT);
	public static final Log BIRCH = new Log("Birch Wood", 2, DEFAULT);
	public static final Log JUNGLE = new Log("Jungle Wood", 3, DEFAULT);
	private static final short dataMask = 0x0003;
	public static final short aliveMask = 0x0100;
	public static final short heightMask = 0x0600;
	
	private static final Vector3[] maxRange =  new Vector3[] {new Vector3(4, 0, 4), new Vector3(4, 8, 4)};
	public final float BURN_TIME = 15.f;

	private Log(String name) {
		super(dataMask, name, 17);
		this.setHardness(2.0F).setResistance(10.F).setOpacity((byte) 1);
	}

	private Log(String name, int data, Log parent) {
		super(name, 17, data, parent);
		this.setHardness(2.0F).setResistance(10.F).setOpacity((byte) 1);
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public boolean hasGrowthStages() {
		return false;
	}

	@Override
	public int getNumGrowthStages() {
		return 0;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 0;
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public ItemStack getResult() {
		return new ItemStack(Coal.CHARCOAL, 1);
	}

	@Override
	public float getCraftTime() {
		return Furnace.SMELT_TIME;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Axe ? (short) 1 : (short) 2;
	}

	@Override
	public boolean canBurn() {
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}

	@Override
	public Vector3[] maxRange() {
		return maxRange;
	}

	@Override
	public long update(Block b, long updateTime, long lastUpdateTime, boolean first) {
		int data = b.getData() & 0xFFFF;
		if ((data & aliveMask) == 0) {
			return -1;
		} else {
			if (first) {
				return b.getWorld().getAge() + 10000;
			} else {
				Block trunk = b;
				int blockData = b.getData();
				int expectHeight = (blockData & heightMask) >> 9;
				for (int i = 0; i < expectHeight + 1; i++) {
					if (!(trunk.getMaterial() instanceof Log)) {
						return -1;
					}
					trunk = trunk.translate(BlockFace.TOP);
				}
				if (expectHeight == 3) {
					trunk = b;
					for (int i = 0; i < expectHeight + 1; i++) {
						trunk.setMaterial(BlockMaterial.AIR);
						trunk = trunk.translate(BlockFace.TOP);
					}
					// TODO - make it match the type of log
					SmallTreeObject object = new SmallTreeObject(new Random(), SmallTreeType.OAK);
					object.placeObject(b.getWorld(), b.getX(), b.getY(), b.getZ());
					return -1;
				} else if (trunk.getMaterial() == BlockMaterial.AIR) {
					trunk.setMaterial(b.getMaterial());
					int newHeight = (expectHeight + 1) << 9;
					newHeight &= heightMask;
					int alive = 1 << 8;
					alive &= aliveMask;
					data = data & dataMask;
					data |= alive;
					data |= newHeight;
					b.setData(data);
					return updateTime + 10000;
				} else {
					b.setData(data & dataMask);
					return -1;
				}

			}
		}
	}
}
