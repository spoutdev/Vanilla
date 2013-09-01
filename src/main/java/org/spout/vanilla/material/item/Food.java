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
package org.spout.vanilla.material.item;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;

import org.spout.math.GenericMath;
import org.spout.math.vector.Vector2;
import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.component.entity.misc.Effects;
import org.spout.vanilla.component.entity.misc.Hunger;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.EntityEffect;
import org.spout.vanilla.data.effect.EntityEffectType;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.food.FoodEffect;

public class Food extends VanillaItemMaterial {
	private final FoodEffect[] effects;

	public Food(String name, int id, Vector2 pos, FoodEffect... effects) {
		super(name, id, pos);
		this.effects = effects;
	}

	public FoodEffect[] getEffectType() {
		return effects;
	}

	public void onEat(Entity entity, Slot slot) {
		entity.get(Living.class).setEatingBlocking(false);
		if (entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.SURVIVAL)) {
			Hunger hunger = entity.get(Hunger.class);
			for (FoodEffect effect : getEffectType()) {
				switch (effect.getEffect()) {
					case HEALTH_REGENERATION:
						entity.add(Effects.class).add(new EntityEffect(EntityEffectType.REGENERATION, effect.getChange()));
						break;
					case HUNGER:
						hunger.setHunger((int) (hunger.getHunger() + effect.getChange()));
						break;
					case POISON:
						if (GenericMath.getRandom().nextInt(101) < effect.getChange()) {
							entity.add(Effects.class).add(new EntityEffect(EntityEffectType.HUNGER, 30));
						}
						break;
					case SATURATION:
						hunger.setFoodSaturation(hunger.getFoodSaturation() + effect.getChange());
						break;
				}
			}
			slot.addAmount(-1);
			if (slot.get().getMaterial().equals(VanillaMaterials.MUSHROOM_STEW)) {
				slot.add(new ItemStack(VanillaMaterials.BOWL, 1));
			}
		}
	}
}
