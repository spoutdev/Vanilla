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

import java.util.Random;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

import org.spout.vanilla.api.component.Hostile;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.entity.creature.GhastEntityProtocol;

/**
 * A component that identifies the entity as a Ghast.
 */
public class Ghast extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new GhastEntityProtocol());
		DropComponent dropComponent = getOwner().add(DropComponent.class);
		Random random = getRandom();
		dropComponent.addDrop(new ItemStack(VanillaMaterials.GUNPOWDER, random.nextInt(2)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.GHAST_TEAR, random.nextInt(1)));
		dropComponent.addXpDrop((short) 5);
		if (getAttachedCount() == 1) {
			getOwner().add(HealthComponent.class).setSpawnHealth(10);
		}

		//TODO: Fireball damage
	}

	public boolean hasRedEyes() {
		return getData().get(VanillaData.RED_EYES);
	}

	public void setRedEyes(boolean redEyes) {
		getData().put(VanillaData.RED_EYES, redEyes);
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, redEyes ? (byte) 1 : 0));
	}
}
