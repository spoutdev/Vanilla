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
package org.spout.vanilla.component.entity.living;

import org.spout.api.entity.Entity;
import org.spout.api.geo.LoadOption;

import org.spout.vanilla.data.VanillaData;

public abstract class Animal extends Ageable {
	/**
	 * Returns true if the animal is in 'love mode'
	 * @return true if animal is in love mode
	 */
	public boolean isInLove() {
		return getDatatable().get(VanillaData.IN_LOVE);
	}

	/**
	 * Sets whether the animal is in 'love mode'
	 * @param inLove if the animal is in love mode
	 */
	public void setInLove(boolean inLove) {
		getDatatable().put(VanillaData.IN_LOVE, inLove);
	}

	/**
	 * Returns true if the specified animal can breed with this animal.
	 * Specified animal must not be this, must be of the same class, and must
	 * both be in 'love mode' specified by {@link #isInLove()}.
	 * @param animal to breed with
	 * @return whether the animal is in love
	 */
	public boolean canBreedWith(Animal animal) {
		return animal != null && animal != this && animal.getClass() == getClass() && isInLove() && animal.isInLove();
	}

	/**
	 * Creates a child with this and the specified animal as the parents. If
	 * allowed by {@link #canBreedWith(Animal)}.
	 * @param animal to breed with
	 */
	@SuppressWarnings("unchecked")
	public void breed(Animal animal) {
		Entity owner = getOwner();
		if (canBreedWith(animal)) {
			Animal child = owner.getWorld().createAndSpawnEntity(owner.getScene().getPosition(), LoadOption.LOAD_ONLY, animal.getClass()).get(Animal.class);
			setAge(MAX_AGE);
			animal.setAge(MAX_AGE);
			setInLove(false);
			animal.setInLove(false);
			child.setAge(MIN_AGE);
		}
	}
}
