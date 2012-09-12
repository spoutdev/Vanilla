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
package org.spout.vanilla.item;

import org.junit.Test;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.EnchantmentUtil;

import static org.junit.Assert.assertTrue;

public class EnchantmentTest {
	@Test
	public void testUnbreaking() {
		ItemStack itemStack = new ItemStack(VanillaMaterials.DIAMOND_PICKAXE, 1);
		EnchantmentUtil.addEnchantment(itemStack, Enchantments.UNBREAKING, 3,
				false);
		assertTrue(EnchantmentUtil.getEnchantmentLevel(itemStack,
				Enchantments.UNBREAKING) == 3);

		Tool tool = (Tool) itemStack.getMaterial();
		int durabilityLost = 0, trials = 100;
		for (int i = 0; i < trials; ++i)
			durabilityLost += tool.getDurabilityPenalty(itemStack);
		System.out.println("Unbreaking III lost " + durabilityLost
				+ " durability from " + trials + " actions.");
	}
}
