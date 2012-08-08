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
package org.spout.vanilla.controller.living.creature.passive;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.Source;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.VanillaEntityController;
import org.spout.vanilla.controller.component.ai.other.TimedDropItemComponent;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Passive;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.material.VanillaMaterials;

public class Chicken extends Creature implements Passive {
	public static final int MINIMUM_EGG_BREEDING_TIME = 6000;
	public static final int MAXIMUM_EGG_BREEDING_TIME = 12000;
	public static final int NEVER = -1;
	private boolean layingEggsEnabled = true;
	private TimedDropItemComponent dropItemComponent;

	public Chicken() {
		super(VanillaControllerTypes.CHICKEN);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getHealth().setSpawnHealth(4);
		dropItemComponent = new TimedDropItemComponent(this, VanillaMaterials.EGG, 1, MINIMUM_EGG_BREEDING_TIME, MAXIMUM_EGG_BREEDING_TIME);
		registerProcess(dropItemComponent);
	}

	@Override
	public Set<ItemStack> getDrops(Source source, VanillaEntityController lastDamager) {
		Set<ItemStack> drops = new HashSet<ItemStack>();
		int count = getRandom().nextInt(3);
		if (count > 0) {
			drops.add(new ItemStack(VanillaMaterials.FEATHER, count));
		}

		count = getRandom().nextInt(2);
		if (count > 0) {
			if (source == DamageCause.BURN) {
				drops.add(new ItemStack(VanillaMaterials.COOKED_CHICKEN, count));
			} else {
				drops.add(new ItemStack(VanillaMaterials.RAW_CHICKEN, count));
			}
		}

		return drops;
	}

	/**
	 * Check if laying Eggs is enabled.
	 * @return <b>true</b>, if laying eggs is enabled. <b>false</b> otherwise.
	 */
	public boolean isLayingEggsEnabled() {
		return layingEggsEnabled;
	}

	/**
	 * Enable/Disable laying of eggs.
	 * @param layingEggsEnabled
	 */
	public void setLayingEggsEnabled(boolean layingEggsEnabled) {
		if (this.layingEggsEnabled && dropItemComponent != null) {
			unregisterProcess(dropItemComponent);
			this.layingEggsEnabled = false;
		} else {
			registerProcess(dropItemComponent);
			this.layingEggsEnabled = layingEggsEnabled;
		}
	}
}
