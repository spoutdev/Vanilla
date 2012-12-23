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
package org.spout.vanilla.material.item;

import org.spout.api.entity.Entity;
import org.spout.api.math.Vector2;

import org.spout.vanilla.component.inventory.PlayerInventory;
import org.spout.vanilla.component.misc.HungerComponent;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;

public class Food extends VanillaItemMaterial {
	private final FoodEffect[] effects;

	public Food(String name, int id, Vector2 pos, FoodEffect... effects) {
		super(name, id, pos);
		this.effects = effects;
	}

	public FoodEffect[] getEffectType() {
		return effects;
	}

	public void onEat(Entity entity, int slot) {
		if (entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.SURVIVAL)) {
			HungerComponent hunger = entity.get(HungerComponent.class);
			for (FoodEffect effect : getEffectType()) {
				switch(effect.getEffect()) {
					case HEALTH_REGENERATION:
						//TODO: Need potions implemented to work.
						break;
					case HUNGER:
						hunger.setHunger((int) (hunger.getHunger() + effect.getChange()));
						break;
					case POISON:
						//TODO: Need potions to apply that.
						break;
					case SATURATION:
						hunger.setFoodSaturation((float) (hunger.getFoodSaturation() + effect.getChange()));
						break;
				}
			}
			entity.get(PlayerInventory.class).getQuickbar().addAmount(slot, -1);
		}
	}
}
