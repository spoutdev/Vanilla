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
package org.spout.vanilla.material.item;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.Slot;
import org.spout.api.material.Material;
import org.spout.api.math.Vector2;
import org.spout.api.render.RenderMaterial;

import org.spout.vanilla.data.VanillaRenderMaterials;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.util.PlayerUtil;

public class VanillaItemMaterial extends Material implements VanillaMaterial {
	private final int minecraftId;
	private int meleeDamage = 1;
	private final Vector2 pos;
	private int enchantability;

	public VanillaItemMaterial(String name, int id, Vector2 pos) {
		super(name);
		this.minecraftId = id;
		this.pos = pos;
	}

	public VanillaItemMaterial(short dataMask, String name, int id, Vector2 pos) {
		super(dataMask, name);
		this.minecraftId = id;
		this.pos = pos;
	}

	public VanillaItemMaterial(String name, int id, int data, Material parent, Vector2 pos) {
		super(name, data, parent);
		this.minecraftId = id;
		this.pos = pos;
	}

	/**
	 * Gets the enchantability of this item material to use in the process of enchanting
	 *
	 * @return Enchantability level of this item material
	 */
	public int getEnchantability() {
		return enchantability;
	}

	/**
	 * Gets whether this item material is enchantable (has an enchantability greater than 0)
	 *
	 * @return true if this item material's enchantability is greater than 0
	 */
	public boolean isEnchantable() {
		return enchantability > 0;
	}

	/**
	 * Sets the enchantability of this item material
	 *
	 * @param enchantability Enchantability to set
	 */
	public void setEnchantability(int enchantability) {
		this.enchantability = enchantability;
	}

	@Override
	public final int getMinecraftId() {
		return minecraftId;
	}

	@Override
	public short getMinecraftData(short data) {
		return data;
	}

	@Override
	public RenderMaterial getRenderMaterial() {
		return VanillaRenderMaterials.ITEMS_MATERIAL;
	}

	@Override
	public Vector2 getSpritePosition() {
		return pos;
	}

	public boolean hasNBTData() {
		return false;
	}

	@Override
	public int getDamage() {
		return this.meleeDamage;
	}

	@Override
	public VanillaItemMaterial setDamage(int damage) {
		this.meleeDamage = damage;
		return this;
	}

	/**
	 * Handles the removal of this Item Material from the selected slot of an Entity. If this is suppressed by the Entity (for example, is in creative) nothing happens. If the selected item is not this
	 * material, no item is subtracted.<br><br>
	 *
	 * @param entity to handle the selected item removal of
	 */
	public void handleSelectionRemove(Entity entity) {
		if (PlayerUtil.isCostSuppressed(entity)) {
			return;
		}
		Slot slot = PlayerUtil.getHeldSlot(entity);
		if (slot == null || slot.get() == null || !slot.get().isMaterial(this)) {
			return;
		}
		slot.addAmount(-1);
	}
}
