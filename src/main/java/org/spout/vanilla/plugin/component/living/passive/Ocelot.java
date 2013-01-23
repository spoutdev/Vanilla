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
package org.spout.vanilla.plugin.component.living.passive;

import org.spout.vanilla.api.component.Passive;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.protocol.entity.creature.OcelotEntityProtocol;

/**
 * A component that identifies the entity as a Ocelot.
 */
public class Ocelot extends Living implements Passive {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new OcelotEntityProtocol());

		if (getAttachedCount() == 1) {
			getOwner().add(HealthComponent.class).setSpawnHealth(10);
		}
		getOwner().add(DropComponent.class).addXpDrop((short) (getRandom().nextInt(3) + 1));
	}

	public boolean isTamed() {
		return getOwner().getData().get(VanillaData.TAMED);
	}

	public void setTamed(boolean tamed) {
		getOwner().getData().put(VanillaData.TAMED, tamed);
	}

	public String getOwnerName() {
		return getOwner().getData().get(VanillaData.OWNER);
	}

	public void setOwnerName(String owner) {
		if (isTamed()) {
			getOwner().getData().put(VanillaData.OWNER, owner);
		}
	}

	public byte getSkinId() {
		System.out.println(getOwner().getData().get(VanillaData.OCELOT_SKIN));
		return getOwner().getData().get(VanillaData.OCELOT_SKIN);
	}

	public void setSkinId(byte skinId) {
		getOwner().getData().put(VanillaData.OCELOT_SKIN, skinId);
	}

	public boolean isSitting() {
		return getOwner().getData().get(VanillaData.SITTING);
	}

	public void setSitting(boolean sitting) {
		getOwner().getData().put(VanillaData.SITTING, sitting);
	}
}
