/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.entity.living.creature.passive;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.EntityProtocolStore;

import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.entity.Entity;
import org.spout.vanilla.entity.living.Creature;
import org.spout.vanilla.entity.living.creature.Passive;

public class Chicken extends Creature implements Passive {
	private static EntityProtocolStore entityProtocolStore = new EntityProtocolStore();

	@Override
	public void onAttached() {
		super.onAttached();
		getParent().setData(Entity.KEY, Entity.Chicken.id);
	}

	@Override
	public EntityProtocol getEntityProtocol(int protocolId) {
		return entityProtocolStore.getEntityProtocol(protocolId);
	}

	public static void setEntityProtocol(int protocolId, EntityProtocol protocol) {
		entityProtocolStore.setEntityProtocol(protocolId, protocol);
	}

	@Override
	public Set<ItemStack> getDeathDrops() {
		Set<ItemStack> drops = new HashSet<ItemStack>();

		int count = getRandom().nextInt(3);
		if (count > 0) {
			drops.add(new ItemStack(VanillaMaterials.FEATHER, count));
		}

		// TODO: Check if killed by fire
		count = getRandom().nextInt(2);
		if (count > 0) {
			drops.add(new ItemStack(VanillaMaterials.RAW_CHICKEN, count));
		}

		return drops;
	}
}
