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
package org.spout.vanilla.plugin.material.item.armor.chain;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.Inventory;
import org.spout.vanilla.api.inventory.entity.ArmorInventory;
import org.spout.vanilla.api.material.item.armor.Leggings;

public class ChainLeggings extends ChainArmor implements Leggings {
	public ChainLeggings(String name, int id, short durability) {
		super(name, id, durability, null);
		this.setBaseProtection(4);
	}

	@Override
	public boolean canEquip(Entity entity, Inventory inventory, int slot) {
		return inventory instanceof ArmorInventory && slot == ArmorInventory.LEGGINGS_SLOT;
	}
}
