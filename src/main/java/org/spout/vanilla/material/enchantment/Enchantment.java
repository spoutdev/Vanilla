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
package org.spout.vanilla.material.enchantment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.spout.api.inventory.ItemStack;
import org.spout.api.math.GenericMath;

import org.spout.nbt.CompoundMap;
import org.spout.nbt.CompoundTag;
import org.spout.nbt.ListTag;
import org.spout.nbt.ShortTag;
import org.spout.nbt.Tag;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.util.MathHelper;

/**
 * Represents an enchantment that can be applied to an item
 */
public abstract class Enchantment {
	private final String name;
	private final int id;
	private int maxPowerLevel;
	private int weight;
	/**
	 * Used to compute {@link #getMinimumLevel(int)} and {@link #getMaximumLevel(int)}
	 */
	protected final int baseEnchantmentLevel, deltaEnchantmentLevel, enchantmentLevelRange;

	protected Enchantment(String name, int id, int baseEnchantmentLevel, int deltaEnchantmentLevel, int enchantmentLevelRange) {
		this.name = name;
		this.id = id;
		this.baseEnchantmentLevel = baseEnchantmentLevel;
		this.deltaEnchantmentLevel = deltaEnchantmentLevel;
		this.enchantmentLevelRange = enchantmentLevelRange;
	}

	/**
	 * Whether this enchantment can enchant the given material
	 * @param material Material to check
	 */
	public abstract boolean canEnchant(VanillaMaterial material);

	/**
	 * Gets the maximum power level that this enchantment can be
	 * @return maximum power level
	 */
	public final int getMaximumPowerLevel() {
		return maxPowerLevel;
	}

	/**
	 * Sets the maximum power level that this enchantment can be
	 * @param maxPowerLevel Level to set as the maximum
	 */
	public Enchantment setMaximumLevel(int maxPowerLevel) {
		this.maxPowerLevel = maxPowerLevel;
		return this;
	}

	/**
	 * Gets the minimum modified enchantment level allowed to produce this
	 * enchantment with a given power level
	 * @param powerLevel The desired power level of the enchantment
	 * @return Minimum level
	 */
	public int getMinimumLevel(int powerLevel) {
		return baseEnchantmentLevel + (powerLevel - 1) * deltaEnchantmentLevel;
	}

	/**
	 * Gets the maximum modified enchantment level allowed to produce this
	 * enchantment with a given power level
	 * @param powerLevel The desired power level of the enchantment
	 * @return Maximum level
	 */
	public int getMaximumLevel(int powerLevel) {
		return getMinimumLevel(powerLevel) + enchantmentLevelRange;
	}

	/**
	 * Gets the weight of this enchantment, enchantments with higher weights
	 * have a greater chance of being selected during the enchantment process
	 * @return Weight of this enchantment
	 */
	public final int getWeight() {
		return weight;
	}

	/**
	 * Sets the weight of this enchantment
	 * @param weight Weight to set
	 */
	public Enchantment setWeight(int weight) {
		this.weight = weight;
		return this;
	}

	/**
	 * Whether this enchantment is compatible with the given enchantment while
	 * attached to the given material
	 * @param enchantment Enchantment to check
	 * @param material Material that this enchantment is attached to
	 * @return true if this enchantment is compatible with the given enchantment
	 */
	public boolean compatibleWith(Enchantment enchantment, VanillaMaterial material) {
		return true;
	}

	/**
	 * Gets the id of this enchantment
	 * @return id of this enchantment
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the name of this enchantment
	 * @return name of this enchantment
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Whether this enchantment equals any of the given enchantments
	 * @param enchantments Enchantments to check
	 * @return true if this enchantment matches any a given enchantment
	 */
	public final boolean equals(Enchantment... enchantments) {
		for (Enchantment enchantment : enchantments) {
			if (equals(enchantment)) {
				return true;
			}
		}

		return false;
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
	 * @param powerLevel Power level of the enchantment
	 * @param force Whether the enchantment should be forced on the item
	 * @return Whether the enchantment was able to be added to the item
	 */
	@SuppressWarnings("unchecked")
	public static boolean addEnchantment(ItemStack item, Enchantment enchantment, int powerLevel, boolean force) {
		VanillaMaterial material = (VanillaMaterial) item.getMaterial();
		if (hasEnchantment(item, enchantment)) {
			return false;
		}

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
		if (nbtData == null) {
			nbtData = new CompoundMap();
		}
		List<CompoundTag> enchantments = new ArrayList<CompoundTag>();
		if (nbtData.get("ench") instanceof ListTag<?>) {
			enchantments = new ArrayList<CompoundTag>(((ListTag<CompoundTag>) nbtData.get("ench")).getValue());
		}
		CompoundMap map = new CompoundMap();
		map.put(new ShortTag("id", (short) enchantment.getId()));
		map.put(new ShortTag("lvl", (short) powerLevel));
		enchantments.add(new CompoundTag(null, map));
		nbtData.put(new ListTag<CompoundTag>("ench", CompoundTag.class, enchantments));
		item.setNBTData(nbtData);
		return true;
	}

	/**
	 * Returns the power level of the given {@link Enchantment}
	 * @param item Item containing the enchantment
	 * @param enchantment Enchantment to check
	 * @return Power level of the enchantment, or 0 if the item does not contain
	 *         the enchantment
	 */
	public static int getEnchantmentLevel(ItemStack item, Enchantment enchantment) {
		if (!isEnchanted(item)) {
			return 0;
		}
		@SuppressWarnings("unchecked")
		List<CompoundTag> enchantmentList = ((ListTag<CompoundTag>) item.getNBTData().get("ench")).getValue();
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
	public static TObjectIntMap<Enchantment> getEnchantments(ItemStack item) {
		TObjectIntMap<Enchantment> enchantments = new TObjectIntHashMap<Enchantment>();
		if (!isEnchanted(item)) {
			return enchantments;
		}
		@SuppressWarnings("unchecked")
		List<CompoundTag> enchantmentList = ((ListTag<CompoundTag>) item.getNBTData().get("ench")).getValue();
		for (CompoundTag tag : enchantmentList) {
			Tag<?> idTag = tag.getValue().get("id");
			Tag<?> lvlTag = tag.getValue().get("lvl");
			if (idTag instanceof ShortTag && lvlTag instanceof ShortTag) {
				enchantments.put(EnchantmentRegistry.getById(((ShortTag) idTag).getValue()), (int) ((ShortTag) lvlTag).getValue());
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
	 * Whether the given item has any {@link Enchantment}s attached to it
	 * @param item Item to check
	 * @return true if the item contains any enchantment
	 */
	public static boolean isEnchanted(ItemStack item) {
		CompoundMap nbtData = item.getNBTData();
		return nbtData != null && nbtData.containsKey("ench") && nbtData.get("ench") instanceof ListTag<?>;
	}

	/**
	 * An object holding both the type of {@link Enchantment} as well as its
	 * power level (I, II, III, IV, V)
	 */
	private final static class EnchantmentData {
		public final Enchantment enchantment;
		public final int powerLevel;

		EnchantmentData(Enchantment enchantment, int powerLevel) {
			this.enchantment = enchantment;
			this.powerLevel = powerLevel;
		}

		@Override
		public String toString() {
			return "EnchantmentData{" + enchantment.name + ", " + powerLevel + "}";
		}
	}

	/**
	 * Tries to add random enchantments to an item stack
	 * @param itemStack The item stack to enchant
	 * @param level The level of enchantment
	 * @return Whether enchantments were successfully added
	 */
	public static boolean addRandomEnchantments(ItemStack itemStack, int level) {
		VanillaMaterial material = (VanillaMaterial) itemStack.getMaterial();
		Random random = GenericMath.getRandom();
		if (!(material instanceof VanillaItemMaterial)) {
			return false;
		}
		int enchantibility = ((VanillaItemMaterial) material).getEnchantability();
		// modify level depending on item enchantibility
		level += 1 + random.nextInt(enchantibility / 4 + 1) + random.nextInt(enchantibility / 4 + 1);
		// modify level by a random multiplier from 0.85 to 1.15 (triangular
		// distribution)
		float multiplier = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F + 1.0F;
		level = Math.max((int) ((float) level * multiplier + 0.5F), 1);

		TObjectIntMap<EnchantmentData> enchantmentList = makeEnchantmentList(level, material);
		boolean succeeded = false;
		while (enchantmentList != null && !enchantmentList.isEmpty()) {
			EnchantmentData enchantmentData = MathHelper.chooseWeightedRandom(random, enchantmentList);
			if (enchantmentData != null) {
				succeeded |= addEnchantment(itemStack, enchantmentData.enchantment, enchantmentData.powerLevel, false);
				// remove any enchantments from the list which aren't compatible
				// with the one we just added
				for (Iterator<EnchantmentData> i = enchantmentList.keySet().iterator(); i.hasNext(); ) {
					Enchantment conflict = i.next().enchantment;
					if (!conflict.compatibleWith(enchantmentData.enchantment, material)) {
						i.remove();
					}
				}
			}
			// Decide whether to add more enchantments
			if (random.nextInt(50) > level) {
				break;
			}
			level /= 2;
		}
		return succeeded;
	}

	/**
	 * Generates a list of allowed {@link EnchantmentData} for the given item,
	 * together with their probability weights
	 * @param level The modified enchantment level
	 * @param material The material of the {@link ItemStack} to be enchanted
	 * @return A map from the allowed {@link EnchantmentData} to their
	 *         probability weights
	 */
	private static TObjectIntMap<EnchantmentData> makeEnchantmentList(int level, VanillaMaterial material) {
		TObjectIntMap<EnchantmentData> output = new TObjectIntHashMap<EnchantmentData>();
		Enchantment[] enchantmentList = EnchantmentRegistry.values();
		for (Enchantment enchantment : enchantmentList) {
			if (!enchantment.canEnchant(material)) {
				continue;
			}
			for (int powerLevel = 1; powerLevel <= enchantment.getMaximumPowerLevel(); ++powerLevel) {
				if (level >= enchantment.getMinimumLevel(powerLevel) && level <= enchantment.getMaximumLevel(powerLevel)) {
					output.put(new EnchantmentData(enchantment, powerLevel), enchantment.getWeight());
				}
			}
		}
		return output;
	}
}
