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
package org.spout.vanilla.component.entity.living.neutral;

import org.spout.vanilla.component.entity.living.Aggressive;
import org.spout.vanilla.component.entity.living.Animal;
import org.spout.vanilla.component.entity.living.Neutral;
import org.spout.vanilla.component.entity.living.Tameable;
import org.spout.vanilla.component.entity.misc.Damage;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.entity.creature.CreatureProtocol;
import org.spout.vanilla.protocol.entity.creature.CreatureType;

/**
 * A component that identifies the entity as a Wolf.
 */
public class Wolf extends Animal implements Neutral, Aggressive, Tameable {

	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new CreatureProtocol(CreatureType.WOLF));
		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(8);
		}

		Damage damage = getOwner().add(Damage.class);
		damage.getDamageLevel(Difficulty.EASY).setAmount(2);
		damage.getDamageLevel(Difficulty.NORMAL).setAmount(damage.getDamageLevel(Difficulty.EASY).getAmount());
		damage.getDamageLevel(Difficulty.HARD).setAmount(damage.getDamageLevel(Difficulty.NORMAL).getAmount());
		damage.getDamageLevel(Difficulty.HARDCORE).setAmount(damage.getDamageLevel(Difficulty.HARD).getAmount());

		// Add wolf metadata
		MetadataComponent metadata = getOwner().add(MetadataComponent.class);
		metadata.addMeta(new Metadata<Byte>(Metadata.TYPE_BYTE, 16) {
			@Override
			public Byte getValue() {
				byte data = 0;
				if (isSitting()) {
					data |= 0x01;
				}
				if (isAggressive()) {
					data |= 0x02;
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
				setAggressive((data & 0x02) == 0x02);
				setTamed((data & 0x04) == 0x04);
			}
		});
		metadata.addMeta(Metadata.TYPE_STRING, 17, VanillaData.OWNER);
		metadata.addMeta(Metadata.TYPE_FLOAT, 18, VanillaData.HEALTH);
	}

	@Override
	public boolean isAggressive() {
		return getOwner().getData().get(VanillaData.AGGRESSIVE);
	}

	@Override
	public void setAggressive(boolean aggressive) {
		getOwner().getData().put(VanillaData.AGGRESSIVE, aggressive);
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
		if (tamed) {
			getOwner().get(Health.class).setMaxHealth(20);
		} else {
			getOwner().get(Health.class).setMaxHealth(8);
		}
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

	/**
	 * Gets whether the Wolf is sitting down on command
	 * 
	 * @return True if sitting down, False if not (standing upright)
	 */
	public boolean isSitting() {
		return getOwner().getData().get(VanillaData.SITTING);
	}

	/**
	 * Sets whether the Wolf is sitting down on command
	 * 
	 * @param sitting state to set to
	 */
	public void setSitting(boolean sitting) {
		getOwner().getData().put(VanillaData.SITTING, sitting);
	}
}
