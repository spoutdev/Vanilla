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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.spout.api.inventory.ItemStack;

import org.spout.nbt.CompoundMap;
import org.spout.nbt.CompoundTag;
import org.spout.nbt.ListTag;
import org.spout.nbt.ShortTag;
import org.spout.nbt.Tag;

import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.enchantment.Enchantment;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.Enchantable;

public class EnchantmentUtil {
	private EnchantmentUtil() {
	}

	/**
	 * Adds the given {@link Enchantment} to the given item
	 * @param item ItemStack to add the enchantment to
	 * @param enchantment Enchantment to add to the item
	 * @return Whether the enchantment was able to be added to the item
	 */
	public static boolean addEnchantment(ItemStack item, Enchantment enchantment) {
		return addEnchantment(item, enchantment, false);
	}

	/**
	 * Adds the given {@link Enchantment} to the given item
	 * @param item ItemStack to add the enchantment to
	 * @param enchantment Enchantment to add to the item
	 * @param force Whether the enchantment should be forced on the item
	 * @return Whether the enchantment was able to be added to the item
	 */
	public static boolean addEnchantment(ItemStack item, Enchantment enchantment, boolean force) {
		return addEnchantment(item, enchantment, 1, force);
	}

	/**
	 * Adds the given {@link Enchantment} with the given level to the given item
	 * @param item ItemStack to add the enchantment to
	 * @param enchantment Enchantment to add to the item
	 * @param level Power level of the enchantment
	 * @param force Whether the enchantment should be forced on the item
	 * @return Whether the enchantment was able to be added to the item
	 */
	@SuppressWarnings("unchecked")
	public static boolean addEnchantment(ItemStack item, Enchantment enchantment, int level, boolean force) {
		VanillaMaterial material = (VanillaMaterial) item.getMaterial();
		if (!hasEnchantment(item, enchantment)) {
			if (!force) {
				if (enchantment.canEnchant(material)) {
					// Check for conflicts
					for (Enchantment conflict : getEnchantments(item).keySet()) {
						if (!conflict.compatibleWith(enchantment, material)) {
							return false;
						}
					}
				} else {
					return false;
				}
			}

			CompoundMap nbtData = item.getNBTData();
			if (nbtData == null)
				nbtData = new CompoundMap();
			List<CompoundTag> enchantments = new ArrayList<CompoundTag>();
			if (nbtData.get("ench") instanceof ListTag<?>)
				enchantments = new ArrayList<CompoundTag>(((ListTag<CompoundTag>) nbtData.get("ench")).getValue());
			CompoundMap map = new CompoundMap();
			map.put(new ShortTag("id", (short) enchantment.getId()));
			map.put(new ShortTag("lvl", (short) level));
			enchantments.add(new CompoundTag(null, map));
			nbtData.put(new ListTag<CompoundTag>("ench", CompoundTag.class, enchantments));
			item.setNBTData(nbtData);
		}

		return true;
	}

	/**
	 * Returns the level of the given {@link Enchantment}
	 * @param item Item containing the enchantment
	 * @param enchantment Enchantment to check
	 * @return Power level of the enchantment, or 0 if the item does not contain the enchantment
	 */
	public static int getEnchantmentLevel(ItemStack item, Enchantment enchantment) {
		if (item.getNBTData() == null)
			return 0;
		Tag<?> enchTag = item.getNBTData().get("ench");
		if (!(enchTag instanceof ListTag<?>))
			return 0;
		@SuppressWarnings("unchecked")
		List<CompoundTag> enchantmentList = ((ListTag<CompoundTag>) enchTag).getValue();
		for (CompoundTag tag : enchantmentList) {
			Tag<?> idTag = tag.getValue().get("id");
			Tag<?> lvlTag = tag.getValue().get("lvl");
			if (idTag instanceof ShortTag && lvlTag instanceof ShortTag && ((ShortTag) idTag).getValue() == enchantment.getId()) {
				return (int) ((ShortTag) lvlTag).getValue();
			}
		}
		return 0;
	}

	/**
	 * Returns all {@link Enchantment}s attached to the given item
	 * @param item Item to check
	 * @return Map of the item's enchantments along with their power level
	 */
	public static Map<Enchantment, Integer> getEnchantments(ItemStack item) {
		Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
		if (item.getNBTData() == null)
			return enchantments;
		Tag<?> enchTag = item.getNBTData().get("ench");
		if (!(enchTag instanceof ListTag<?>))
			return enchantments;
		@SuppressWarnings("unchecked")
		List<CompoundTag> enchantmentList = ((ListTag<CompoundTag>) enchTag).getValue();
		for (CompoundTag tag : enchantmentList) {
			Tag<?> idTag = tag.getValue().get("id");
			Tag<?> lvlTag = tag.getValue().get("lvl");
			if (idTag instanceof ShortTag && lvlTag instanceof ShortTag) {
				enchantments.put(Enchantments.getById(((ShortTag) idTag).getValue()), (int) ((ShortTag) lvlTag).getValue());
			}
		}
		return enchantments;
	}

	/**
	 * Whether the given item contains the given {@link Enchantment}
	 * @param item Item to check
	 * @param enchantment Enchantment to check
	 * @return true if the item contains the enchantment
	 */
	public static boolean hasEnchantment(ItemStack item, Enchantment enchantment) {
		return getEnchantmentLevel(item, enchantment) > 0;
	}

	/**
	 * Generates a random enchantment level for use in the enchantment interface
	 * @param random The random number generator to be used
	 * @param slotNumber The slot number (From 0 to 2) of the slot in the enchantment interface
	 * @param bookshelves The number of bookshelves connected to the enchantment table
	 * @return The enchantment level
	 */
	public static int getRandomEnchantmentLevel(Random random, int slotNumber, int bookshelves) {
		if (bookshelves > 15)
			bookshelves = 15;
		int baseLevel = random.nextInt(8) + 1 + (bookshelves >> 1) + random.nextInt(bookshelves + 1);
		switch (slotNumber) {
		case 0:
			return Math.max(baseLevel / 3, 1);
		case 1:
			return baseLevel * 2 / 3 + 1;
		default:
			return Math.max(baseLevel, bookshelves * 2);
		}
	}

	/**
	 * An object holding both the type of {@link Enchantment} as well as its power level (I, II, III, IV, V)
	 */
	private final static class EnchantmentData {
		public final Enchantment enchantment;
		public final int powerLevel;

		EnchantmentData(Enchantment enchantment, int powerLevel) {
			this.enchantment = enchantment;
			this.powerLevel = powerLevel;
		}
	}

	/**
	 * Tries to add random enchantments to an item stack
	 * @param random The random number generator to be used
	 * @param itemStack The item stack to enchant. No enchantments will be added if the material doesn't implement {@link Enchantable}
	 * @param level The level of enchantment
	 * @return Whether enchantments were successfully added
	 * 
	 */
	public static boolean addRandomEnchantments(Random random, ItemStack itemStack, int level) {
		VanillaMaterial material = (VanillaMaterial) itemStack.getMaterial();
		if (!(material instanceof Enchantable))
			return false;
		int enchantibility = ((Enchantable) material).getEnchantability();
		// modify level depending on item enchantibility
		level += 1 + random.nextInt(enchantibility / 4 + 1) + random.nextInt(enchantibility / 4 + 1);
		// modify level by a random multiplier from 0.85 to 1.15 (triangular
		// distribution)
		float multiplier = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F + 1.0F;
		level = Math.max((int) ((float) level * multiplier + 0.5F), 1);

		Map<EnchantmentData, Integer> enchantmentList = makeEnchantmentList(level, material);
		boolean succeeded = false;
		while (enchantmentList != null && !enchantmentList.isEmpty()) {
			EnchantmentData enchantmentData = VanillaMathHelper.chooseWeightedRandom(random, enchantmentList);
			if (enchantmentData != null) {
				succeeded |= addEnchantment(itemStack, enchantmentData.enchantment, enchantmentData.powerLevel, false);
				// remove any enchantments from the list which aren't compatible
				// with the one we just added
				for (Iterator<EnchantmentData> i = enchantmentList.keySet().iterator(); i.hasNext();) {
					Enchantment conflict = i.next().enchantment;
					if (!conflict.compatibleWith(enchantmentData.enchantment, material))
						i.remove();
				}
			}
			// Decide whether to add more enchantments
			if (random.nextInt(50) > level)
				break;
			level /= 2;
		}
		return succeeded;
	}

	/**
	 * Generates a list of allowed {@link EnchantmentData} for the given item, together with their probability weights
	 * @param level The modified enchantment level
	 * @param material The material of the {@link ItemStack} to be enchanted
	 * @return A map from the allowed {@link EnchantmentData} to their probability weights
	 */
	private static Map<EnchantmentData, Integer> makeEnchantmentList(int level, VanillaMaterial material) {
		Map<EnchantmentData, Integer> output = new HashMap<EnchantmentData, Integer>();
		Enchantment[] enchantmentList = Enchantments.values();
		for (Enchantment enchantment : enchantmentList) {
			if (!enchantment.canEnchant(material))
				continue;
			for (int powerLevel = 1; powerLevel <= enchantment.getMaximumPowerLevel(); ++powerLevel) {
				if (level >= enchantment.getMinimumLevel(powerLevel) && level <= enchantment.getMaximumLevel(powerLevel)) {
					output.put(new EnchantmentData(enchantment, powerLevel), enchantment.getWeight());
				}
			}
		}
		return output;
	}
}
