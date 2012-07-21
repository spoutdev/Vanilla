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
package org.spout.vanilla.world.generator.normal.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.block.Chest;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.object.RandomObject;

public class LootChestObject extends RandomObject {
	private int maxNumberOfStacks = 8;
	private final List<LootProbability> loot = new ArrayList<LootProbability>();
	private double currentPMax = 0.0;

	public LootChestObject() {
		this(null);
	}

	public LootChestObject(Random random) {
		super(random);
	}

	@Override
	public void randomize() {
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		final Block block = w.getBlock(x, y, z, w);
		final Block above = block.translate(BlockFace.TOP);
		return block.isMaterial(VanillaMaterials.AIR) && above.isMaterial(VanillaMaterials.AIR);
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		w.setBlockMaterial(x, y, z, VanillaMaterials.CHEST, (short) 0, w);
		w.setBlockController(x, y, z, new Chest());
		final Chest chest = (Chest) w.getBlockController(x, y, z);
		final ChestInventory inv = chest.getInventory();
		for (int i = 0; i < getMaxNumberOfStacks(); i++) {
			final int slot = random.nextInt(inv.getSize());
			inv.setItem(slot, getNextStack());
		}
	}

	public ItemStack getNextStack() {
		final double t = random.nextDouble();
		for (LootProbability p : loot) {
			if (t >= p.getpStart() && t < p.getpEnd()) {
				return p.getRandomStack(random);
			}
		}
		return new ItemStack(VanillaMaterials.AIR, 0);
	}

	/**
	 * Adds a new material to the loot
	 *
	 * @param mat the material to add
	 * @param probability the probability that it is selected
	 * @param minItems minimum items of that material per stack
	 * @param maxItems maximum items of that material per stack
	 * @returns the instance for chained calls
	 * @throws IllegalStateException when the total probability is above 1.0
	 */
	public LootChestObject addMaterial(Material mat, double probability, int minItems, int maxItems) throws IllegalStateException {
		final LootProbability toAdd = new LootProbability(probability, currentPMax, mat, minItems, maxItems);
		currentPMax += probability;
		if (currentPMax > 1.0) {
			throw new IllegalStateException("P_total(" + currentPMax + ") is above the allowed threshold of 1.0");
		}
		loot.add(toAdd);
		return this;
	}

	public double getTotalProbability() {
		return currentPMax;
	}

	public void setMaxNumberOfStacks(int maxNumberOfStacks) {
		this.maxNumberOfStacks = maxNumberOfStacks;
	}

	public int getMaxNumberOfStacks() {
		return maxNumberOfStacks;
	}

	private class LootProbability {
		private final double pStart, pEnd;
		private final Material loot;
		private final int min, max;

		private LootProbability(double probability, double pStart, Material loot, int min, int max) {
			this.pStart = pStart;
			this.pEnd = pStart + probability;
			this.loot = loot;
			this.min = min;
			this.max = max;
		}

		private Material getLoot() {
			return loot;
		}

		private double getpStart() {
			return pStart;
		}

		private double getpEnd() {
			return pEnd;
		}

		private ItemStack getRandomStack(Random random) {
			int amount;
			if (max == min) {
				amount = min;
			} else {
				amount = random.nextInt(max - min + 1) + min;
			}
			return new ItemStack(getLoot(), amount);
		}
	}
}
