/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.component.entity.living.passive;

import org.spout.vanilla.component.entity.living.Animal;
import org.spout.vanilla.component.entity.living.Passive;
import org.spout.vanilla.component.entity.living.Tameable;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.entity.creature.CreatureProtocol;
import org.spout.vanilla.protocol.entity.creature.CreatureType;

/**
 * A component that identifies the entity as a Ocelot.
 */
public class Ocelot extends Animal implements Passive, Tameable {
	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new CreatureProtocol(CreatureType.OCELOT));

		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(10);
		}
		getOwner().add(DeathDrops.class).addXpDrop((short) (getRandom().nextInt(3) + 1));

		// Add ocelot metadata
		MetadataComponent metadata = getOwner().add(MetadataComponent.class);
		metadata.addMeta(new Metadata<Byte>(Metadata.TYPE_BYTE, 16) {
			@Override
			public Byte getValue() {
				byte data = 0;
				if (isSitting()) {
					data |= 0x01;
				}
				if (isTamed()) {
					data |= 0x04;
				}
				return data;
			}

			@Override
			public void setValue(Byte value) {
				int data = value.intValue();
				setSitting((data & 0x01) == 0x01);
				setTamed((data & 0x04) == 0x04);
			}
		});
		metadata.addMeta(Metadata.TYPE_STRING, 17, VanillaData.OWNER);
		metadata.addMeta(Metadata.TYPE_BYTE, 18, VanillaData.OCELOT_SKIN);
	}

	@Override
	public boolean canBeTamed() {
		return true;
	}

	@Override
	public boolean isTamed() {
		return getOwner().getData().get(VanillaData.TAMED);
	}

	@Override
	public void setTamed(boolean tamed) {
		getOwner().getData().put(VanillaData.TAMED, tamed);
	}

	@Override
	public String getOwnerName() {
		return getOwner().getData().get(VanillaData.OWNER);
	}

	@Override
	public void setOwnerName(String owner) {
		if (isTamed()) {
			getOwner().getData().put(VanillaData.OWNER, owner);
		}
	}

	public byte getSkinId() {
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
