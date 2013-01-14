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
package org.spout.vanilla.plugin.inventory.player;

import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.vanilla.api.material.item.armor.Boots;
import org.spout.vanilla.api.material.item.armor.Chestplate;
import org.spout.vanilla.api.material.item.armor.Helmet;
import org.spout.vanilla.api.material.item.armor.Leggings;
import org.spout.vanilla.plugin.event.entity.EntityEquipmentEvent;

/**
 * Represents the four armor slots on a {@link org.spout.vanilla.plugin.component.inventory.PlayerInventory}.
 */
public class PlayerArmorInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	public static final int SIZE = 4;
	public static final int BOOT_SLOT = 0, LEGGINGS_SLOT = 1, CHEST_PLATE_SLOT = 2, HELMET_SLOT = 3;

	public PlayerArmorInventory() {
		super(SIZE);
	}

	/**
	 * Informs a player of all the equipment (armor)
	 * 
	 * @param player to inform
	 */
	public void updateSlots(Player player) {
		for (int i = 0; i < this.size(); i++) {
			updateSlot(i, get(i), player);
		}
	}

	/**
	 * Informs a player of a certain equipment (armor) change
	 * 
	 * @param i - item slot index
	 * @param item that the slot got set to
	 * @param player to inform
	 */
	public static void updateSlot(int i, ItemStack item, Player player) {
		final int equip;
		switch (i) {
			case PlayerArmorInventory.BOOT_SLOT :
				equip = 3;
				break;
			case PlayerArmorInventory.CHEST_PLATE_SLOT :
				equip = 1;
				break;
			case PlayerArmorInventory.HELMET_SLOT :
				equip = 0;
				break;
			case PlayerArmorInventory.LEGGINGS_SLOT :
				equip = 2;
				break;
			default :
				return;
		}
		player.getNetwork().callProtocolEvent(new EntityEquipmentEvent(player, equip, item));
	}

	@Override
	public boolean canSet(int i, ItemStack item) {
		if (!super.canSet(i, item)) {
			return false;
		}
		if (item != null) {
			Material material = item.getMaterial();
			switch (i) {
				case BOOT_SLOT:
					return material instanceof Boots;
				case LEGGINGS_SLOT:
					return material instanceof Leggings;
				case CHEST_PLATE_SLOT:
					return material instanceof Chestplate;
				case HELMET_SLOT:
					return material instanceof Helmet;
				default:
					return false;
			}
		}
		return true;
	}

	/**
	 * Returns the {@link ItemStack} in the boots slot.
	 * @return boots
	 */
	public ItemStack getBoots() {
		return get(BOOT_SLOT);
	}

	/**
	 * Sets the {@link ItemStack} in the boots slot.
	 * @param boots
	 */
	public void setBoots(ItemStack boots) {
		set(BOOT_SLOT, boots);
	}

	/**
	 * Returns the {@link ItemStack} in the leggings slot.
	 * @return leggings
	 */
	public ItemStack getLeggings() {
		return get(LEGGINGS_SLOT);
	}

	/**
	 * Sets the {@link ItemStack} in the leggings slot.
	 * @param leggings
	 */
	public void setLeggings(ItemStack leggings) {
		set(LEGGINGS_SLOT, leggings);
	}

	/**
	 * Returns the {@link ItemStack} in the chest plate slot.
	 * @return chest plate
	 */
	public ItemStack getChestPlate() {
		return get(CHEST_PLATE_SLOT);
	}

	/**
	 * Sets the {@link ItemStack} in the chest plate slot.
	 * @param chestPlate
	 */
	public void setChestPlate(ItemStack chestPlate) {
		set(CHEST_PLATE_SLOT, chestPlate);
	}

	/**
	 * Returns the {@link ItemStack} in the helmet slot
	 * @return helmet
	 */
	public ItemStack getHelmet() {
		return get(HELMET_SLOT);
	}

	/**
	 * Sets the {@link ItemStack} in the helmet slot.
	 * @param helmet
	 */
	public void setHelmet(ItemStack helmet) {
		set(HELMET_SLOT, helmet);
	}
}
