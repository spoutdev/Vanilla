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
package org.spout.vanilla.entity.living.creature.passive;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.Source;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.VanillaEntityController;
import org.spout.vanilla.entity.living.Creature;
import org.spout.vanilla.entity.living.creature.Passive;
import org.spout.vanilla.entity.source.DamageCause;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.material.VanillaMaterials;

public class Pig extends Creature implements Passive {
	public Pig() {
		super(VanillaControllerTypes.PIG);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getHealth().setSpawnHealth(10);
		getHealth().setHurtEffect(SoundEffects.MOB_PIG);
	}

	@Override
	public Set<ItemStack> getDrops(Source source, VanillaEntityController lastDamager) {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		int count = getRandom().nextInt(3);
		if (count > 0) {
			if (source == DamageCause.BURN) {
				drops.add(new ItemStack(VanillaMaterials.COOKED_PORKCHOP, count));
			} else {
				drops.add(new ItemStack(VanillaMaterials.RAW_PORKCHOP, count));
			}
		}

		return drops;
	}
}
