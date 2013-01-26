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
package org.spout.vanilla.plugin.material.item.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector2;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.api.enchantment.Enchantment;

import org.spout.vanilla.plugin.component.living.hostile.Silverfish;
import org.spout.vanilla.plugin.component.living.hostile.Skeleton;
import org.spout.vanilla.plugin.component.living.hostile.Spider;
import org.spout.vanilla.plugin.component.living.hostile.Zombie;
import org.spout.vanilla.plugin.data.drops.flag.ToolEnchantFlags;
import org.spout.vanilla.plugin.data.tool.ToolType;
import org.spout.vanilla.plugin.enchantment.VanillaEnchantments;
import org.spout.vanilla.plugin.material.item.VanillaItemMaterial;

public abstract class Tool extends VanillaItemMaterial {
	private short durability;
	private Map<BlockMaterial, Float> strengthModifiers = new HashMap<BlockMaterial, Float>();
	private ToolType toolType;

	public Tool(String name, int id, short durability, ToolType toolType, Vector2 pos) {
		super(name, id, pos);
		this.durability = durability;
		this.toolType = toolType;
		this.setMaxStackSize(1);
	}

	public short getDurabilityPenalty(ItemStack item) {
		if (Enchantment.hasEnchantment(item, VanillaEnchantments.UNBREAKING)) {
			// Level 1 = 50%, Level 2 = 67%, Level 3 = 75% chance to not consume durability
			if (100 - (100 / (Enchantment.getEnchantmentLevel(item, VanillaEnchantments.UNBREAKING) + 1)) > MathHelper.getRandom().nextInt(100)) {
				return (short) 0;
			}
		}
		return (short) 1;
	}

	/**
	 * Gets the type of tool
	 * @return tool type
	 */
	public ToolType getToolType() {
		return this.toolType;
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

	@Override
	public boolean hasNBTData() {
		return true;
	}

	@Override
	public void getItemFlags(ItemStack item, Set<Flag> flags) {
		super.getItemFlags(item, flags);
		flags.add(this.toolType.getToolFlag());
		if (Enchantment.hasEnchantment(item, VanillaEnchantments.SILK_TOUCH)) {
			flags.add(ToolEnchantFlags.SILK_TOUCH);
		}
	}

	public int getDamageBonus(Entity damaged, ItemStack heldItem) {
		final Random rand = MathHelper.getRandom();
		// These enchantments conflict with each other, so only one is possible per item
		int damage = 0;
		if (Enchantment.hasEnchantment(heldItem, VanillaEnchantments.BANE_OF_ARTHROPODS)) {
			if (damaged.has(Spider.class) || damaged.has(Silverfish.class)) {
				damage = rand.nextInt(Enchantment.getEnchantmentLevel(heldItem, VanillaEnchantments.BANE_OF_ARTHROPODS) * 4);
			}
		} else if (Enchantment.hasEnchantment(heldItem, VanillaEnchantments.SHARPNESS)) {
			damage = rand.nextInt(Enchantment.getEnchantmentLevel(heldItem, VanillaEnchantments.SHARPNESS) * 3);
		} else if (Enchantment.hasEnchantment(heldItem, VanillaEnchantments.SMITE)) {
			if (damaged.has(Skeleton.class) || damaged.has(Zombie.class)) {
				damage = rand.nextInt(Enchantment.getEnchantmentLevel(heldItem, VanillaEnchantments.SMITE) * 4);
			}
		}

		// These enchantments must give at least one health point of extra damage
		if (damage == 0) {
			damage = 1;
		}
		return damage;
	}
}
