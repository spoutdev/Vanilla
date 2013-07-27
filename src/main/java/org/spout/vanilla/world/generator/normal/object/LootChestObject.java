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
package org.spout.vanilla.world.generator.normal.object;

import java.util.Random;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;

import org.spout.vanilla.component.block.material.chest.Chest;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.MathHelper;
import org.spout.vanilla.world.generator.object.RandomObject;

public class LootChestObject extends RandomObject {
	private int minNumberOfStacks = 0;
	private int maxNumberOfStacks = 8;
	private final TObjectIntMap<LootItem> loot = new TObjectIntHashMap<LootItem>();

	public LootChestObject() {
		this(null);
	}

	public LootChestObject(Random random) {
		super(random);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final Block block = w.getBlock(x, y, z);
		VanillaMaterials.CHEST.onPlacement(block, (short) 0, null, null, false, null);
		final ChestInventory inventory = block.get(Chest.class).getInventory();
		final int numberOfStack = random.nextInt(maxNumberOfStacks - minNumberOfStacks + 1) + minNumberOfStacks;
		final int size = inventory.size();
		for (int i = 0; i < numberOfStack; i++) {
			inventory.set(random.nextInt(size), MathHelper.chooseWeightedRandom(random, loot).getRandomStack(random));
		}
	}

	/**
	 * Adds a new material to the loot
	 * @param mat the material to add
	 * @param probability the probability that it is selected
	 * @param minItems minimum items of that material per stack
	 * @param maxItems maximum items of that material per stack
	 * @return the instance for chained calls
	 */
	public LootChestObject addMaterial(Material mat, int probability, int minItems, int maxItems) {
		loot.put(new LootItem(mat, minItems, maxItems), probability);
		return this;
	}

	public int getMinNumberOfStacks() {
		return minNumberOfStacks;
	}

	public void setMinNumberOfStacks(int minNumberOfStacks) {
		this.minNumberOfStacks = minNumberOfStacks;
	}

	public int getMaxNumberOfStacks() {
		return maxNumberOfStacks;
	}

	public void setMaxNumberOfStacks(int maxNumberOfStacks) {
		this.maxNumberOfStacks = maxNumberOfStacks;
	}

	private static class LootItem {
		private final Material loot;
		private final int min;
		private final int max;

		private LootItem(Material loot, int min, int max) {
			if (max < min) {
				throw new IllegalArgumentException("Max amout cannot be smaller than min amount");
			}
			this.loot = loot;
			this.min = min;
			this.max = max;
		}

		private ItemStack getRandomStack(Random random) {
			return new ItemStack(loot, random.nextInt(max - min + 1) + min);
		}
	}
}
