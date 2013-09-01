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
package org.spout.vanilla.material.item.armor;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;

import org.spout.math.vector.Vector2;
import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.event.cause.DamageCause;
import org.spout.vanilla.event.cause.DamageCause.DamageType;
import org.spout.vanilla.material.enchantment.Enchantment;
import org.spout.vanilla.material.enchantment.VanillaEnchantments;
import org.spout.vanilla.material.item.VanillaItemMaterial;

public abstract class Armor extends VanillaItemMaterial {
	private int protection;
	private short durability;

	protected Armor(String name, int id, short durability, Vector2 pos) {
		super(name, id, pos);
		this.durability = durability;
		this.setMaxStackSize(1);
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

	public int getProtection(ItemStack item, DamageCause<?> cause) {
		DamageType type = cause.getType();
		int amount = 0;
		int level = 0;
		// Armor can only have one of these protection enchantments
		if (Enchantment.hasEnchantment(item, VanillaEnchantments.PROTECTION) && (type != DamageType.COMMAND && type != DamageType.VOID)) {
			level = Enchantment.getEnchantmentLevel(item, VanillaEnchantments.PROTECTION);
		} else if (Enchantment.hasEnchantment(item, VanillaEnchantments.BLAST_PROTECTION) && type.equals(DamageType.EXPLOSION)) {
			level = Enchantment.getEnchantmentLevel(item, VanillaEnchantments.BLAST_PROTECTION);
		} else if (Enchantment.hasEnchantment(item, VanillaEnchantments.FIRE_PROTECTION) && (type == DamageType.BURN && type == DamageType.FIRE_SOURCE)) {
			level = Enchantment.getEnchantmentLevel(item, VanillaEnchantments.FIRE_PROTECTION);
		} else if (Enchantment.hasEnchantment(item, VanillaEnchantments.PROJECTILE_PROTECTION) && (type == DamageType.PROJECTILE && type == DamageType.FIREBALL)) {
			level = Enchantment.getEnchantmentLevel(item, VanillaEnchantments.PROJECTILE_PROTECTION);
		}
		amount += level * 3;

		// Feather Falling is compatible with all of the above enchantments
		if (Enchantment.hasEnchantment(item, VanillaEnchantments.FEATHER_FALLING) && type.equals(DamageType.FALL)) {
			amount += Enchantment.getEnchantmentLevel(item, VanillaEnchantments.FEATHER_FALLING);
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

	/**
	 * The index of the slot that this armor will equip in an {@link org.spout.vanilla.inventory.entity.ArmorInventory}
	 *
	 * @return index
	 */
	public abstract int getEquipableSlot();

	@Override
	public void onInteract(Entity entity, Block block, Action type, BlockFace clickedface) {
		PlayerInventory inv = entity.get(PlayerInventory.class);
		if (inv != null) {
			switch (type) {
				case RIGHT_CLICK:
					if (inv.getArmor().get(getEquipableSlot()) == null) {
						inv.getArmor().set(getEquipableSlot(), inv.getHeldItem());
						inv.getQuickbar().getSelectedSlot().set(null);
					}
			}
		}
	}
}
