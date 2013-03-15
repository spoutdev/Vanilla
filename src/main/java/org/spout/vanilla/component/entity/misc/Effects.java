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
package org.spout.vanilla.component.entity.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;

import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.data.effect.EntityEffect;
import org.spout.vanilla.data.effect.EntityEffectType;
import org.spout.vanilla.event.entity.network.EntityEffectEvent;
import org.spout.vanilla.event.entity.network.EntityRemoveEffectEvent;
import org.spout.vanilla.material.item.potion.PotionItem;

/**
 * Component handling status effects. This includes food poisoning, regeneration, etc.
 */
public class Effects extends EntityComponent {
	private final List<EntityEffect> list = new ArrayList<EntityEffect>();
	private Health health;

	@Override
	public void onAttached() {
		this.health = getOwner().add(Health.class);
	}

	@Override
	public boolean canTick() {
		return list.size() > 0;
	}

	@Override
	public void onTick(float dt) {
		Iterator<EntityEffect> iterator = list.iterator();
		boolean removed = false;
		while (iterator.hasNext()) {
			removed = false;
			EntityEffect effect = iterator.next();
			effect.setTimer(effect.getTimer() - dt);
			effect.addTick(dt);

			//TODO: Probably spammy. Need to find a better way.
			if (EntityEffectType.INVISIBILITY.equals(effect.getEffect())) {
				getOwner().get(Living.class).sendMetaData();
			}
			if (effect.getTimer() <= 0) {
				iterator.remove();
				removed = true;
			}

			if (!removed) {
				switch (effect.getEffect()) {
					case HUNGER:
						if (getOwner() instanceof Player && !getOwner().get(Human.class).isSurvival()) {
							Hunger hunger = getOwner().add(Hunger.class);
							hunger.setExhaustion(hunger.getExhaustion() + 0.025f);
						}
						break;
					case REGENERATION:
						if (getOwner() instanceof Player && !getOwner().get(Human.class).isSurvival()) {
							break;
						}
						if (effect.getTier() == PotionItem.TIER0 && effect.getTick() >= 1.20f) {
							health.heal(1);
							effect.resetTick();
						} else if (effect.getTier() == PotionItem.TIER2 && effect.getTick() >= 0.55f) {
							health.heal(1);
							effect.resetTick();
						}
						break;
					case POISON:
						if (getOwner() instanceof Player && !getOwner().get(Human.class).isSurvival()) {
							break;
						}
						if (effect.getTier() == PotionItem.TIER0 && effect.getTick() >= 1.25f) {
							if (health.getHealth() > 1) {
								health.damage(1);
							}
							effect.resetTick();
						} else if (effect.getTier() == PotionItem.TIER2 && effect.getTick() >= 0.55f) {
							if (health.getHealth() > 1) {
								health.damage(1);
							}
							effect.resetTick();
						}
						break;
					case WITHER:
						if (getOwner() instanceof Player && !getOwner().get(Human.class).isSurvival()) {
							break;
						}
						if (effect.getTier() == PotionItem.TIER0 && effect.getTick() >= 1.25f) {
							health.damage(1);
							effect.resetTick();
						} else if (effect.getTier() == PotionItem.TIER2 && effect.getTick() >= 0.55f) {
							health.damage(1);
							effect.resetTick();
						}
						break;
					default:
						break;
				}
			}
		}
	}

	/**
	 * Add a effect to the entity.
	 * @param effect The effect to add.
	 */
	public void addEffect(EntityEffect effect) {
		if (EntityEffectType.INSTANT_DAMAGE.equals(effect.getEffect())) {
			if (effect.getTier() == PotionItem.TIER0) {
				health.damage(6);
			} else if (effect.getTier() == PotionItem.TIER2) {
				health.damage(12);
			}
		} else if (EntityEffectType.INSTANT_HEALTH.equals(effect.getEffect())) {
			if (effect.getTier() == PotionItem.TIER0) {
				health.heal(6);
			} else if (effect.getTier() == PotionItem.TIER2) {
				health.heal(12);
			}
		} else {
			getOwner().getNetwork().callProtocolEvent(new EntityEffectEvent(getOwner(), effect));
			list.add(effect);
		}
	}

	/**
	 * Remove a effect from the entity.
	 * @param effect The effect to remove.
	 */
	public void removeEffect(EntityEffectType effect) {
		if (containsEffect(effect)) {
			getOwner().getNetwork().callProtocolEvent(new EntityRemoveEffectEvent(getOwner(), effect));
			list.remove(effect);
		}
	}

	/**
	 * Checks if a effect is currently enabled on the entity.
	 * @param effect The effect to verify.
	 * @return True if the effect is currently enabled, else false.
	 */
	public boolean containsEffect(EntityEffectType effect) {
		boolean result = false;
		for (EntityEffect effectContainer : list) {
			result = effectContainer.equals(effect);
			if (result) {
				break;
			}
		}
		return result;
	}
}
