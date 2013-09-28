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
package org.spout.vanilla.material.item.food;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.component.FurnaceBlock;
import org.spout.vanilla.material.item.Food;

public class RawFish extends Food implements TimedCraftable {
	public static final RawFish RAW_FISH = new RawFish("Raw Fish", new FoodEffect(FoodEffects.HUNGER_INCREASE, 2), new FoodEffect(FoodEffects.SATURATION, 1.2f));
	public static final RawFish RAW_SALMON = new RawFish("Raw Salmon", (short) 1, RAW_FISH,  new FoodEffect(FoodEffects.HUNGER_INCREASE, 2));

	public RawFish(String name, FoodEffect... effects) {
		super((short) 0x03, name, 349, null, effects);
	}

	public RawFish(String name, short data, RawFish parent, FoodEffect... effects) {
		super(name, 349, data, parent, null, effects);
	}

	@Override
	public ItemStack getResult() {
		switch (this.getData()) {
			case 0:
				return new ItemStack(VanillaMaterials.COOKED_FISH, 1);
			case 1:
				return new ItemStack(VanillaMaterials.COOKED_SALMON, 1);
			default:
				return null;
		}
	}

	@Override
	public float getCraftTime() {
		return FurnaceBlock.SMELT_TIME;
	}
}
