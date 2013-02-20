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
package org.spout.vanilla.component.living.passive;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.Passive;
import org.spout.vanilla.component.living.Living;
import org.spout.vanilla.component.misc.EntityDropComponent;
import org.spout.vanilla.component.misc.HealthComponent;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.creature.PigEntityProtocol;

/**
 * A component that identifies the entity as a Pig.
 */
public class Pig extends Living implements Passive {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new PigEntityProtocol());
		EntityDropComponent dropComponent = getOwner().add(EntityDropComponent.class);
		dropComponent.addDrop(new ItemStack(VanillaMaterials.RAW_PORKCHOP, getRandom().nextInt(2) + 1));
		dropComponent.addXpDrop((short) (getRandom().nextInt(3) + 1));

		if (getAttachedCount() == 1) {
			getOwner().add(HealthComponent.class).setSpawnHealth(10);
		}

	}

	public boolean isSaddled() {
		return getOwner().getData().get(VanillaData.SADDLED);
	}

	public void setSaddled(boolean saddled) {
		getOwner().getData().put(VanillaData.SADDLED, saddled);
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, saddled ? (byte) 1 : 0));
	}
}
