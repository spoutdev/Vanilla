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
package org.spout.vanilla.component.entity.misc;

import javax.tools.Tool;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;

import org.spout.math.vector.Vector3f;
import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.component.entity.inventory.EntityInventory;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.event.entity.network.EntityCollectItemEvent;
import org.spout.vanilla.inventory.entity.ArmorInventory;
import org.spout.vanilla.inventory.entity.EntityQuickbarInventory;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.material.item.armor.Armor;

/**
 * Component that adds a detector to resources.entities to scan for and pickup items.
 */
public class EntityItemCollector extends VanillaEntityComponent {
	private final int DISTANCE = VanillaConfiguration.ITEM_PICKUP_RANGE.getInt();
	private List<Entity> nearbyEntities;
	private int wait = 0;

	@Override
	public boolean canTick() {
		Health health = getOwner().get(Health.class);
		if (health != null) {
			if (!health.isDead() && wait != 0) {
				wait--;
				return false;
			}
			if (health.isDead()) {
				int WAIT_RESET = 30;
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
			if (item == null) {
				continue;
			}
			EntityInventory inv = getOwner().get(EntityInventory.class);
			ArmorInventory armorInv = inv.getArmor();
			// Check if this item is equipable armor and has more protection than the currently equipped item
			boolean equip = false;
			if (item.getItemStack().getMaterial() instanceof Armor) {
				Armor armor = (Armor) item.getItemStack().getMaterial();
				for (int i = 0; i < armorInv.size(); i++) {
					if (armorInv.canSet(i, item.getItemStack())) {
						ItemStack slot = armorInv.get(i);
						if (slot == null || (slot.getMaterial() instanceof Armor && ((Armor) slot.getMaterial()).getBaseProtection() < armor.getBaseProtection())) {
							getOwner().getNetwork().callProtocolEvent(new EntityCollectItemEvent(getOwner(), entity));
							if (slot != null) {
								Item.drop(getOwner().getPhysics().getPosition(), slot, Vector3f.ZERO);
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
						Item.drop(getOwner().getPhysics().getPosition(), inv.getHeldItem(), Vector3f.ZERO);
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
