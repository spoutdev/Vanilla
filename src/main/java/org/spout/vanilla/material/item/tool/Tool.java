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
package org.spout.vanilla.material.item.tool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.controller.VanillaEntityController;
import org.spout.vanilla.controller.living.creature.hostile.Silverfish;
import org.spout.vanilla.controller.living.creature.hostile.Skeleton;
import org.spout.vanilla.controller.living.creature.hostile.Spider;
import org.spout.vanilla.controller.living.creature.hostile.Zombie;
import org.spout.vanilla.data.drops.flag.DropFlagSingle;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.Enchantable;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.util.EnchantmentUtil;

public abstract class Tool extends VanillaItemMaterial implements Enchantable {
	private final Random rand = new Random();
	private short durability;
	private int enchantability;
	private Map<BlockMaterial, Float> strengthModifiers = new HashMap<BlockMaterial, Float>();
	private int miningLevel;
	private final HashSet<DropFlagSingle> dropFlags = new HashSet<DropFlagSingle>();

	public Tool(String name, int id, short durability) {
		super(name, id);
		this.durability = durability;
	}

	public short getDurabilityPenalty(Mineable mineable, ItemStack item) {
		short penalty = mineable.getDurabilityPenalty(this);
		if (EnchantmentUtil.hasEnchantment(item, Enchantments.UNBREAKING)) {
			// Level 1 = 50%, Level 2 = 67%, Level 3 = 75% chance to not consume durability
			if (100 - (100 / (EnchantmentUtil.getEnchantmentLevel(item, Enchantments.UNBREAKING) + 1)) > rand.nextInt(100)) {
				penalty = 0;
			}
		}
		return penalty;
	}

	public short getMaxDurability() {
		return durability;
	}

	public Tool setMaxDurability(short durability) {
		this.durability = durability;
		return this;
	}

	public float getStrengthModifier(BlockMaterial block) {
		if (!(strengthModifiers.containsKey(block))) {
			return (float) 1.0;
		}
		return strengthModifiers.get(block);
	}

	public Tool setStrengthModifier(BlockMaterial block, float modifier) {
		strengthModifiers.put(block, modifier);
		return this;
	}

	public Set<BlockMaterial> getStrengthModifiedBlocks() {
		return strengthModifiers.keySet();
	}

	public int getMiningLevel() {
		return miningLevel;
	}

	public Tool setMiningLevel(int miningLevel) {
		this.miningLevel = miningLevel;
		return this;
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public void setEnchantability(int enchantability) {
		this.enchantability = enchantability;
	}

	@Override
	public boolean hasNBTData() {
		return true;
	}

	/**
	 * Gets all the drop flags this tool adds
	 * 
	 * @return tool flags
	 */
	public Set<DropFlagSingle> getDropFlags() {
		return this.dropFlags;
	}

	/**
	 * Adds all the drop flags to this tool
	 * 
	 * @param dropFlags to add
	 * @return this Tool
	 */
	public Tool addDropFlags(DropFlagSingle... dropFlags) {
		this.dropFlags.addAll(Arrays.asList(dropFlags));
		return this;
	}

	public int getDamageBonus(VanillaEntityController damaged, ItemStack heldItem) {
		// These enchantments conflict with each other, so only one is possible per item
		int damage = 0;
		if (EnchantmentUtil.hasEnchantment(heldItem, Enchantments.BANE_OF_ARTHROPODS)) {
			if (damaged instanceof Spider || damaged instanceof Silverfish) {
				damage = rand.nextInt(EnchantmentUtil.getEnchantmentLevel(heldItem, Enchantments.BANE_OF_ARTHROPODS) * 4);
			}
		} else if (EnchantmentUtil.hasEnchantment(heldItem, Enchantments.SHARPNESS)) {
			damage = rand.nextInt(EnchantmentUtil.getEnchantmentLevel(heldItem, Enchantments.SHARPNESS) * 3);
		} else if (EnchantmentUtil.hasEnchantment(heldItem, Enchantments.SMITE)) {
			if (damaged instanceof Skeleton || damaged instanceof Zombie) {
				damage = rand.nextInt(EnchantmentUtil.getEnchantmentLevel(heldItem, Enchantments.SMITE) * 4);
			}
		}

		// These enchantments must give at least one health point of extra damage
		if (damage == 0) {
			damage = 1;
		}
		return damage;
	}
}
