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
package org.spout.vanilla.material.item.armor;

import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Vector2;

import org.spout.vanilla.event.cause.DamageCause;
import org.spout.vanilla.event.cause.DamageCause.DamageType;
import org.spout.vanilla.material.enchantment.Enchantment;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.Enchantable;
import org.spout.vanilla.material.item.VanillaItemMaterial;

public abstract class Armor extends VanillaItemMaterial implements Enchantable {
	private int protection;
	private int enchantability;
	private short durability;

	protected Armor(String name, int id, short durability, Vector2 pos) {
		super(name, id, pos);
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
		DamageType type = cause.getType();
		int amount = 0;
		int level = 0;
		// Armor can only have one of these protection enchantments
		if (Enchantment.hasEnchantment(item, Enchantments.PROTECTION) && !type.equals(DamageType.COMMAND, DamageType.VOID)) {
			level = Enchantment.getEnchantmentLevel(item, Enchantments.PROTECTION);
		} else if (Enchantment.hasEnchantment(item, Enchantments.BLAST_PROTECTION) && type.equals(DamageType.EXPLOSION)) {
			level = Enchantment.getEnchantmentLevel(item, Enchantments.BLAST_PROTECTION);
		} else if (Enchantment.hasEnchantment(item, Enchantments.FIRE_PROTECTION) && type.equals(DamageType.BURN, DamageType.FIRE_SOURCE)) {
			level = Enchantment.getEnchantmentLevel(item, Enchantments.FIRE_PROTECTION);
		} else if (Enchantment.hasEnchantment(item, Enchantments.PROJECTILE_PROTECTION) && type.equals(DamageType.PROJECTILE, DamageType.FIREBALL)) {
			level = Enchantment.getEnchantmentLevel(item, Enchantments.PROJECTILE_PROTECTION);
		}
		amount += level * 3;

		// Feather Falling is compatible with all of the above enchantments
		if (Enchantment.hasEnchantment(item, Enchantments.FEATHER_FALLING) && type.equals(DamageType.FALL)) {
			amount += Enchantment.getEnchantmentLevel(item, Enchantments.FEATHER_FALLING);
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
