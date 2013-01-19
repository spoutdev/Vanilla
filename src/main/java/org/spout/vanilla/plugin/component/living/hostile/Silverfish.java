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
package org.spout.vanilla.plugin.component.living.hostile;

import org.spout.vanilla.api.component.Hostile;
import org.spout.vanilla.api.data.Difficulty;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.misc.DamageComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.protocol.entity.creature.CreatureProtocol;
import org.spout.vanilla.plugin.protocol.entity.creature.CreatureType;

/**
 * A component that identifies the entity as a Silverfish.
 */
public class Silverfish extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new CreatureProtocol(CreatureType.SILVERFISH));
		if (getAttachedCount() == 1) {
			getOwner().add(HealthComponent.class).setSpawnHealth(8);
		}

		DamageComponent damage = getOwner().add(DamageComponent.class);
		damage.getDamageLevel(Difficulty.EASY).setAmount(1);
		damage.getDamageLevel(Difficulty.NORMAL).setAmount(damage.getDamageLevel(Difficulty.EASY).getAmount());
		damage.getDamageLevel(Difficulty.HARD).setAmount(damage.getDamageLevel(Difficulty.NORMAL).getAmount());
		damage.getDamageLevel(Difficulty.HARDCORE).setAmount(damage.getDamageLevel(Difficulty.HARD).getAmount());
	}
}
