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

import java.util.Random;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;

public class LootProbability {
	private double probability, pStart, pEnd;
	private Material loot;
	private int min, max;

	public LootProbability(double probability, double pStart, Material loot, int min, int max) {
		super();
		this.probability = probability;
		this.pStart = pStart;
		this.pEnd = pStart + probability;
		this.loot = loot;
		this.min = min;
		this.max = max;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public Material getLoot() {
		return loot;
	}

	public void setLoot(Material loot) {
		this.loot = loot;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public double getpStart() {
		return pStart;
	}

	public void setpStart(double pStart) {
		this.pStart = pStart;
	}

	public double getpEnd() {
		return pEnd;
	}

	public void setpEnd(double pEnd) {
		this.pEnd = pEnd;
	}

	public ItemStack getRandomStack(Random random) {
		int amount;
		if (max == min) {
			amount = min;
		} else {
			amount = random.nextInt(max - min) + min;
		}
		return new ItemStack(getLoot(), amount);
	}
}