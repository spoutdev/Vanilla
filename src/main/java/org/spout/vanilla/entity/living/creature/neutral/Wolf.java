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
package org.spout.vanilla.entity.living.creature.neutral;

import org.spout.api.entity.component.Controller;

import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.living.Creature;
import org.spout.vanilla.entity.living.creature.Neutral;
import org.spout.vanilla.entity.living.creature.Tameable;
import org.spout.vanilla.data.effect.store.SoundEffects;

public class Wolf extends Creature implements Tameable, Neutral {
	private Controller master;

	public Wolf() {
		super(VanillaControllerTypes.WOLF);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		// master = data().get("controlling_entity", master);
		if (master != null) {
			getHealth().setSpawnHealth(20);
		} else {
			getHealth().setSpawnHealth(8);
		}
		getHealth().setHurtEffect(SoundEffects.MOB_WOLF_HURT);
	}

	@Override
	public void onSave() {
		super.onSave();
		// data().put("controlling_entity", master);
	}

	@Override
	public void controlledBy(Controller master) {
		this.master = master;
	}

	@Override
	public boolean isControlled() {
		return master != null;
	}
}
