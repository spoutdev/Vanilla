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

import org.spout.api.util.Parameter;

import org.spout.vanilla.data.VanillaData;

public abstract class Ageable extends Living {
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
	 * @return age identifier
	 */
	public float getAge() {
		return getData().get(VanillaData.AGE);
	}

	/**
	 * Sets the age of the animal. Zero denotes a fully grown animal that is
	 * able to procreate, anything under 0 denotes the animal is a child.
	 * @param age of animal
	 */
	public void setAge(float age) {
		getData().put(VanillaData.AGE, Math.max(MIN_AGE, Math.min(age, MAX_AGE)));
		setMetadata(new Parameter<Integer>(Parameter.TYPE_INT, 12, (int) (age * 20)));
	}
}
