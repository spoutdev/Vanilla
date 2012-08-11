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
package org.spout.vanilla.material.item;

import java.lang.reflect.InvocationTargetException;

import org.spout.vanilla.data.entityeffect.VanillaEntityFoodEffect;
import org.spout.vanilla.entity.VanillaPlayerController;

public class FoodEffect {
	private final float amount;
	private final Class<? extends VanillaEntityFoodEffect> effect;

	public FoodEffect(float amount, Class<? extends VanillaEntityFoodEffect> effect) {
		this.amount = amount;
		this.effect = effect;
	}

	public float getAmount() {
		return amount;
	}

	public void run(VanillaPlayerController vPlayer) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		vPlayer.addComponent(effect.getConstructor(new Class[]{VanillaPlayerController.class, float.class}).newInstance(vPlayer, amount));
	}
}
