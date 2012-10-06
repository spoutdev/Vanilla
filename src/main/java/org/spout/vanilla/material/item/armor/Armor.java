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
package org.spout.vanilla.material.item.armor;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.Enchantable;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.source.DamageCause;
import org.spout.vanilla.util.EnchantmentUtil;

public abstract class Armor extends VanillaItemMaterial implements Enchantable {
	private int protection;
	private int enchantability;
	private short durability;

	protected Armor(String name, int id, short durability) {
		super(name, id);
		this.durability = durability;
	}

	public short getMaxDurability() {
		return durability;
	}

	public void setMaxDurability(short durability) {
		this.durability = durability;
	}

	public int getBaseProtection() {
		return protection;
	}

	public int getProtection(ItemStack item, DamageCause cause) {
		int amount = 0;
		int level = 0;
		// Armor can only have one of these protection enchantments
		if (EnchantmentUtil.hasEnchantment(item, Enchantments.PROTECTION) && !cause.equals(DamageCause.COMMAND, DamageCause.VOID)) {
			level = EnchantmentUtil.getEnchantmentLevel(item, Enchantments.PROTECTION);
		} else if (EnchantmentUtil.hasEnchantment(item, Enchantments.BLAST_PROTECTION) && cause.equals(DamageCause.EXPLOSION)) {
			level = EnchantmentUtil.getEnchantmentLevel(item, Enchantments.BLAST_PROTECTION);
		} else if (EnchantmentUtil.hasEnchantment(item, Enchantments.FIRE_PROTECTION) && cause.equals(DamageCause.BURN, DamageCause.FIRE_SOURCE)) {
			level = EnchantmentUtil.getEnchantmentLevel(item, Enchantments.FIRE_PROTECTION);
		} else if (EnchantmentUtil.hasEnchantment(item, Enchantments.PROJECTILE_PROTECTION) && cause.equals(DamageCause.PROJECTILE, DamageCause.FIREBALL)) {
			level = EnchantmentUtil.getEnchantmentLevel(item, Enchantments.PROJECTILE_PROTECTION);
		}
		amount += level * 3;

		// Feather Falling is compatible with all of the above enchantments
		if (EnchantmentUtil.hasEnchantment(item, Enchantments.FEATHER_FALLING) && cause.equals(DamageCause.FALL)) {
			amount += EnchantmentUtil.getEnchantmentLevel(item, Enchantments.FEATHER_FALLING);
		}

		return amount;
	}

	public void setBaseProtection(int protection) {
		this.protection = protection;
	}

	@Override
	public boolean hasNBTData() {
		return true;
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public void setEnchantability(int enchantability) {
		this.enchantability = enchantability;
	}
}
