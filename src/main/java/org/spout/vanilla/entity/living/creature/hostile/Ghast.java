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
package org.spout.vanilla.entity.living.creature.hostile;

import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.living.Creature;
import org.spout.vanilla.entity.living.creature.Hostile;
import org.spout.vanilla.material.VanillaMaterials;

public class Ghast extends Creature implements Hostile {
	public Ghast() {
		super(VanillaControllerTypes.GHAST);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getHealth().setSpawnHealth(10);
		getHealth().setHurtEffect(SoundEffects.MOB_GHAST_SCREAM);
		getDrops().addRange(VanillaMaterials.GUNPOWDER, 2);
		getDrops().addRange(VanillaMaterials.GHAST_TEAR, 1);
	}

	/**
	 * Whether or not the ghast has red eyes.
	 * @return true if it has red eyes
	 */
	public boolean hasRedEyes() {
		return getDataMap().containsKey("red_eyes");
	}

	/**
	 * Sets whether or not the ghast has red eyes.
	 * @param redEyes
	 */
	public void setRedEyes(boolean redEyes) {
		getDataMap().put("red_eyes", redEyes);
	}
}
