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
package org.spout.vanilla.item;

import gnu.trove.map.TObjectIntMap;

import org.junit.Test;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.enchantment.Enchantment;
import org.spout.vanilla.material.enchantment.EnchantmentRegistry;
import org.spout.vanilla.material.enchantment.VanillaEnchantments;

import static org.junit.Assert.assertTrue;

public class EnchantmentTest {
	@Test
	public void testEnchantmentsList() {
		Enchantment[] list = EnchantmentRegistry.values();
		assertTrue(list.length > 0);
		for (Enchantment enchantment : list) {
			assertTrue(enchantment != null);
		}
	}

	@Test
	public void testAddEnchantmentPick() {
		assertTrue(VanillaEnchantments.UNBREAKING.canEnchant(VanillaMaterials.DIAMOND_PICKAXE));
		ItemStack itemStack = new ItemStack(VanillaMaterials.DIAMOND_PICKAXE, 1);
		Enchantment.addEnchantment(itemStack, VanillaEnchantments.EFFICIENCY, 3, false);
		Enchantment.addEnchantment(itemStack, VanillaEnchantments.FORTUNE, 2, false);
		TObjectIntMap<Enchantment> enchantments = Enchantment.getEnchantments(itemStack);
		assertTrue(enchantments.size() == 2);
		assertTrue(enchantments.containsKey(VanillaEnchantments.EFFICIENCY));
		assertTrue(enchantments.get(VanillaEnchantments.EFFICIENCY) == 3);
		assertTrue(Enchantment.getEnchantmentLevel(itemStack, VanillaEnchantments.FORTUNE) == 2);
	}

	@Test
	public void testEnchantmentBow() {
		ItemStack itemStack = new ItemStack(VanillaMaterials.BOW, 1);
		Enchantment.addEnchantment(itemStack, VanillaEnchantments.PUNCH, 2, false);
		Enchantment.addEnchantment(itemStack, VanillaEnchantments.FORTUNE, 2, false);
		assertTrue(!VanillaEnchantments.FORTUNE.canEnchant(VanillaMaterials.BOW));
		assertTrue(Enchantment.getEnchantmentLevel(itemStack, VanillaEnchantments.PUNCH) == 2);
		assertTrue(Enchantment.getEnchantmentLevel(itemStack, VanillaEnchantments.FORTUNE) == 0);
	}

	@Test
	public void testEnchantmentSword() {
		ItemStack itemStack = new ItemStack(VanillaMaterials.DIAMOND_SWORD, 1);
		Enchantment.addEnchantment(itemStack, VanillaEnchantments.KNOCKBACK, 2, false);
		Enchantment.addEnchantment(itemStack, VanillaEnchantments.FORTUNE, 2, false);
		assertTrue(!VanillaEnchantments.FORTUNE.canEnchant(VanillaMaterials.DIAMOND_SWORD));
		assertTrue(Enchantment.getEnchantmentLevel(itemStack, VanillaEnchantments.KNOCKBACK) == 2);
		assertTrue(Enchantment.getEnchantmentLevel(itemStack, VanillaEnchantments.FORTUNE) == 0);
	}
}
