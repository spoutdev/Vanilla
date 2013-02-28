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
import org.spout.api.util.Parameter;

import org.spout.vanilla.data.VanillaData;

public abstract class Animal extends Living {
	public static final float MIN_AGE = -1200;
	public static final float MAX_AGE = 300f;

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		float age = getAge();
		if (age < 0) {
			setAge(age + dt);
		} else if (age > 0) {
			setAge(age - dt);
		}
	}

	/**
	 * How many seconds the animal has until it can procreate again if
	 * positive or how many seconds until the animal is fully grown if
	 * negative.
	 *
	 * @return age identifier
	 */
	public float getAge() {
		return getData().get(VanillaData.AGE);
	}

	/**
	 * Sets the age of the animal. Zero denotes a fully grown animal that is
	 * able to procreate, anything under 0 denotes the animal is a child.
	 *
	 * @param age of animal
	 */
	public void setAge(float age) {
		System.out.println("Age: " + age);
		getData().put(VanillaData.AGE, Math.max(MIN_AGE, Math.min(age, MAX_AGE)));
		setMetadata(new Parameter<Integer>(Parameter.TYPE_INT, 12, (int) (age * 20)));
	}

	/**
	 * Returns true if the animal is in 'love mode'
	 *
	 * @return true if animal is in love mode
	 */
	public boolean isInLove() {
		return getData().get(VanillaData.IN_LOVE);
	}

	/**
	 * Sets whether the animal is in 'love mode'
	 *
	 * @param inLove if the animal is in love mode
	 */
	public void setInLove(boolean inLove) {
		getData().put(VanillaData.IN_LOVE, inLove);
	}

	/**
	 * Returns true if the specified animal can breed with this animal.
	 * Specified animal must not be this, must be of the same class, and must
	 * both be in 'love mode' specified by {@link #isInLove()}.
	 *
	 * @param animal to breed with
	 * @return whether the animal is in love
	 */
	public boolean canBreedWith(Animal animal) {
		return animal != null && animal != this && animal.getClass() == getClass() && isInLove() && animal.isInLove();
	}

	/**
	 * Creates a child with this and the specified animal as the parents. If
	 * allowed by {@link #canBreedWith(Animal)}.
	 *
	 * @param animal to breed with
	 */
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
