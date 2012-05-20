/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.controller.living.creature.hostile;

import java.util.HashSet;
import java.util.Set;

import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Hostile;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.inventory.VanillaItemStack;
import org.spout.vanilla.material.VanillaMaterials;

public class Zombie extends Creature implements Hostile {
	public Zombie() {
		super(VanillaControllerTypes.ZOMBIE);
	}

	protected Zombie(VanillaControllerType type) {
		super(type);
	}

	@Override
	public void onAttached() {
		setHealth(16, new HealthChangeReason(HealthChangeReason.Type.SPAWN));
		setMaxHealth(20);
		super.onAttached();
	}

	@Override
	public Set<VanillaItemStack> getDrops() {
		Set<VanillaItemStack> drops = new HashSet<VanillaItemStack>();

		int count = getRandom().nextInt(3);
		if (count > 0) {
			drops.add(new VanillaItemStack(VanillaMaterials.ROTTEN_FLESH, count));
		}

		if (getRandom().nextInt(25) == 0) {
			drops.add(new VanillaItemStack(VanillaMaterials.IRON_INGOT, 1));
		}

		// TODO: Enchantments
		if (getRandom().nextInt(50) == 0) {
			drops.add(new VanillaItemStack(VanillaMaterials.IRON_HELMET, 1));
		}

		if (getRandom().nextInt(75) == 0) {
			drops.add(new VanillaItemStack(VanillaMaterials.IRON_SPADE, 1));
		}

		if (getRandom().nextInt(100) == 0) {
			drops.add(new VanillaItemStack(VanillaMaterials.IRON_SWORD, 1));
		}

		return drops;
	}
}
