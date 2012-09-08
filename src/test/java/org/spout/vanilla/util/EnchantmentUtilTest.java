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
package org.spout.vanilla.util;

import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.enchantment.Enchantment;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.util.EnchantmentUtil;

import static org.junit.Assert.assertTrue;

public class EnchantmentUtilTest {
	@Test
	public void testRandomEnchantmentLevel() {
		Random random = new Random();
		int level;
		// run a few times due to randomness
		for (int trial = 0; trial < 5; ++trial) {
			level = EnchantmentUtil.getRandomEnchantmentLevel(random, 0, 0);
			assertTrue(level >= 1);
			assertTrue(level <= 2);
			level = EnchantmentUtil.getRandomEnchantmentLevel(random, 0, 15);
			assertTrue(level <= 10);
			assertTrue(level >= 2);
			level = EnchantmentUtil.getRandomEnchantmentLevel(random, 1, 0);
			assertTrue(level >= 1);
			assertTrue(level <= 6);
			level = EnchantmentUtil.getRandomEnchantmentLevel(random, 1, 15);
			assertTrue(level >= 6);
			assertTrue(level <= 21);
			level = EnchantmentUtil.getRandomEnchantmentLevel(random, 2, 0);
			assertTrue(level >= 1);
			assertTrue(level <= 8);
			level = EnchantmentUtil.getRandomEnchantmentLevel(random, 2, 15);
			assertTrue(level == 30);
		}
	}

	@Test
	public void testEnchantmentsList() {
		Enchantment[] list = Enchantments.values();
		for (Enchantment enchantment : list)
			assertTrue(enchantment != null);
	}

	@Test
	public void testAddEnchantment() {
		ItemStack itemStack = new ItemStack(VanillaMaterials.DIAMOND_PICKAXE, 1);
		EnchantmentUtil.addEnchantment(itemStack, Enchantments.EFFICIENCY, 3, false);
		EnchantmentUtil.addEnchantment(itemStack, Enchantments.FORTUNE, 2, false);
		Map<Enchantment, Integer> enchantments = EnchantmentUtil.getEnchantments(itemStack);
		assertTrue(enchantments.size() == 2);
		assertTrue(enchantments.containsKey(Enchantments.EFFICIENCY));
		assertTrue(enchantments.get(Enchantments.EFFICIENCY) == 3);
		assertTrue(EnchantmentUtil.getEnchantmentLevel(itemStack, Enchantments.FORTUNE) == 2);
		System.out.println(itemStack.getNBTData().get("ench").toString());
	}

	@Test
	public void testRandomEnchantments() {
		Random random = new Random();
		VanillaMaterial[] materialList = new VanillaMaterial[] { VanillaMaterials.DIAMOND_PICKAXE, VanillaMaterials.LEATHER_BOOTS, VanillaMaterials.BOW, VanillaMaterials.GOLD_SWORD };
		for (VanillaMaterial material : materialList) {
			for (int level = 1; level < 31; ++level) {
				ItemStack itemStack = new ItemStack(material.getMaterial(), 1);
				boolean success = EnchantmentUtil.addRandomEnchantments(random, itemStack, level);
				Map<Enchantment, Integer> enchantments = EnchantmentUtil.getEnchantments(itemStack);
				assertTrue(success ^ (enchantments.size() == 0));
				System.out.println("Enchanted " + material.toString() + " at level " + level);
				for (Enchantment enchantment : enchantments.keySet()) {
					assertTrue(enchantment.canEnchant(material));
					System.out.println("  " + enchantment.getName() + " " + enchantments.get(enchantment));
				}
			}
		}
	}
}
