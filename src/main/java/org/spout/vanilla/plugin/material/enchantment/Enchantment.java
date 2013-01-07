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
package org.spout.vanilla.plugin.material.enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spout.api.inventory.ItemStack;

import org.spout.nbt.CompoundMap;
import org.spout.nbt.CompoundTag;
import org.spout.nbt.ListTag;
import org.spout.nbt.ShortTag;
import org.spout.nbt.Tag;

import org.spout.vanilla.plugin.material.VanillaMaterial;

/**
 * Represents an enchantment
 */
public abstract class Enchantment {
	private final String name;
	private final int id;

	protected Enchantment(String name, int id) {
		this.name = name;
		this.id = id;
	}

	/**
	 * Whether this enchantment can enchant the given material
	 * @param material Material to check
	 */
	public abstract boolean canEnchant(VanillaMaterial material);

	/**
	 * Gets the maximum level this enchantment can be
	 * @return maximum level
	 */
	public abstract int getMaximumLevel();

	/**
	 * Gets the weight of this enchantment, enchantments with higher weights have a greater chance of being selected during the enchantment process
	 * @return Weight of this enchantment
	 */
	public abstract int getWeight();

	/**
	 * Whether this enchantment is compatible with the given enchantment while attached to the given material
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
	 * @param level Level of the enchantment
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

			List<Tag<?>> enchantments = new ArrayList<Tag<?>>();
			if (item.getNBTData() != null && item.getNBTData().containsKey("ench")) {
				enchantments = ((ListTag) item.getNBTData().get("ench")).getValue();
			}
			CompoundMap map = new CompoundMap();
			map.put(new ShortTag("id", (short) enchantment.getId()));
			map.put(new ShortTag("lvl", (short) level));
			enchantments.add(new CompoundTag("ench", map));
			item.setNBTData(new CompoundMap(enchantments));
		}

		return true;
	}

	/**
	 * Returns the level of the given {@link Enchantment}
	 * @param item Item containing the enchantment
	 * @param enchantment Enchantment to check
	 * @return Level of the enchantment, or 0 if the item does not contain the enchantment
	 */
	public static int getEnchantmentLevel(ItemStack item, Enchantment enchantment) {
		return getEnchantments(item).containsKey(enchantment) ? getEnchantments(item).get(enchantment) : 0;
	}

	/**
	 * Returns all {@link Enchantment}s attached to the given item
	 * @param item Item to check
	 * @return Map of the item's enchantments along with their level
	 */
	public static Map<Enchantment, Integer> getEnchantments(ItemStack item) {
		Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
		if (item.getNBTData() == null || !item.getNBTData().containsKey("ench")) {
			return enchantments;
		}

		List<Short> ids = new ArrayList<Short>();
		List<Short> levels = new ArrayList<Short>();
		for (Tag tag : ((CompoundMap) item.getNBTData().get("ench").getValue()).values()) {
			if (tag.getName().equals("id")) {
				ids.add((Short) tag.getValue());
			} else if (tag.getName().equals("lvl")) {
				levels.add((Short) tag.getValue());
			}
		}
		for (int i = 0; i < ids.size(); i++) {
			enchantments.put(Enchantments.getById(ids.get(i)), (int) levels.get(i));
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
		if (item.getNBTData() == null || !item.getNBTData().containsKey("ench")) {
			return false;
		}

		for (Tag tag : ((CompoundMap) item.getNBTData().get("ench").getValue()).values()) {
			if (tag.getName().equals("id") && ((ShortTag) tag).getValue() == enchantment.getId()) {
				return true;
			}
		}
		return false;
	}
}
