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
package org.spout.vanilla.plugin.component.misc;

import java.util.List;
import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.inventory.VanillaEntityInventory;
import org.spout.vanilla.plugin.component.substance.object.Item;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.inventory.entity.EntityArmorInventory;
import org.spout.vanilla.plugin.inventory.entity.EntityQuickbarInventory;
import org.spout.vanilla.plugin.material.item.VanillaItemMaterial;
import org.spout.vanilla.plugin.material.item.armor.Armor;
import org.spout.vanilla.plugin.material.item.tool.Tool;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;

import org.spout.vanilla.api.event.entity.EntityCollectItemEvent;
import org.spout.vanilla.api.inventory.entity.ArmorInventory;

/**
 * Component that adds a detector to resources.entities to scan for and pickup items.
 */
public class EntityPickupItemComponent extends EntityComponent {
	private final int DISTANCE = VanillaConfiguration.ITEM_PICKUP_RANGE.getInt();
	private List<Entity> nearbyEntities;
	private final int WAIT_RESET = 30;
	private int wait = 0;

	@Override
	public boolean canTick() {
		HealthComponent healthComponent = getOwner().get(HealthComponent.class);
		if (healthComponent != null) {
			if (!healthComponent.isDead() && wait != 0) {
				wait--;
				return false;
			}
			if (healthComponent.isDead()) {
				wait = WAIT_RESET;
				return false;
			}
		}
		nearbyEntities = getOwner().getWorld().getNearbyEntities(getOwner(), DISTANCE);
		return !nearbyEntities.isEmpty();
	}

	@Override
	public void onTick(float dt) {
		for (Entity entity : nearbyEntities) {
			Item item = entity.get(Item.class);
			VanillaEntityInventory inv = getOwner().get(VanillaEntityInventory.class);
			ArmorInventory armorInv = inv.getArmor();
			// Check if this item is equipable armor and has more protection than the currently equipped item
			boolean equip = false;
			if (item.getItemStack().getMaterial() instanceof Armor) {
				Armor armor = (Armor) item.getItemStack().getMaterial();
				for (int i = 0; i < armorInv.size(); i++) {
					if (armorInv.canSet(i, item.getItemStack())) {
						ItemStack slot = armorInv.get(i);
						if (slot != null && !(slot.getMaterial() instanceof Armor)) {
							continue;
						} else if (slot == null || ((Armor) slot.getMaterial()).getBaseProtection() < armor.getBaseProtection()) {
							getOwner().getNetwork().callProtocolEvent(new EntityCollectItemEvent(getOwner(), entity));
							if (slot != null) {
								Item.drop(getOwner().getScene().getPosition(), slot, Vector3.ZERO);
							}
							armorInv.set(i, item.getItemStack(), true);
							entity.remove();
							equip = true;
							break;
						}
					}
				}
			}
			// Check if this weapon deals more damage
			if (!equip && (item.getItemStack().getMaterial() instanceof VanillaItemMaterial || inv.getHeldItem() == null)) {
				if (inv.getHeldItem() == null) {
					equip = true;
				} else if (inv.getHeldItem().getMaterial() instanceof VanillaItemMaterial) {
					VanillaItemMaterial itemMaterial = (VanillaItemMaterial) item.getItemStack().getMaterial();
					VanillaItemMaterial equipMaterial = (VanillaItemMaterial) inv.getHeldItem().getMaterial();
					if (equipMaterial.getDamage() < itemMaterial.getDamage()) {
						equip = true;
					} else if (itemMaterial.getDamage() == equipMaterial.getDamage() && itemMaterial instanceof Tool && equipMaterial instanceof Tool
							&& inv.getHeldItem().getData() > item.getItemStack().getData()) {
						equip = true;
					}
				}
				if (equip) {
					getOwner().getNetwork().callProtocolEvent(new EntityCollectItemEvent(getOwner(), entity));
					if (inv.getHeldItem() != null) {
						Item.drop(getOwner().getScene().getPosition(), inv.getHeldItem(), Vector3.ZERO);
					}
					inv.getQuickbar().set(EntityQuickbarInventory.HELD_SLOT, item.getItemStack(), true);
					entity.remove();
					break;
				}
			}
		}
	}

	protected List<Entity> getNearbyEntities() {
		return nearbyEntities;
	}
}
