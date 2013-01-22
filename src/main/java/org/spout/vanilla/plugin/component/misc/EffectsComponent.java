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
package org.spout.vanilla.plugin.component.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;

import org.spout.vanilla.api.data.effect.StatusEffect;

import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.data.effect.StatusEffectContainer;
import org.spout.vanilla.plugin.event.entity.network.EntityEffectEvent;
import org.spout.vanilla.plugin.event.entity.network.EntityRemoveEffectEvent;
import org.spout.vanilla.plugin.material.item.misc.Potion;

public class EffectsComponent extends EntityComponent {
	private List<StatusEffectContainer> list = new ArrayList<StatusEffectContainer>();
	private HealthComponent health;

	@Override
	public void onAttached() {
		this.health = getOwner().add(HealthComponent.class);
	}

	@Override
	public boolean canTick() {
		return list.size() > 0;
	}

	@Override
	public void onTick(float dt) {
		Iterator<StatusEffectContainer> iterator = list.iterator();
		boolean removed = false;
		while (iterator.hasNext()) {
			removed = false;
			StatusEffectContainer effect = iterator.next();
			effect.setTimer(effect.getTimer() - dt);
			effect.addTick(dt);

			//TODO: Probably spammy. Need to find a better way.
			if (StatusEffect.INVISIBILITY.equals(effect.getEffect())) {
				getOwner().get(Living.class).sendMetaData();
			}
			if (effect.getTimer() <= 0) {
				iterator.remove();
				getOwner().getNetwork().callProtocolEvent(new EntityRemoveEffectEvent(getOwner(), effect.getEffect()));
				removed = true;
			}

			if (!removed) {
				switch (effect.getEffect()) {
					case HUNGER:
						if (getOwner() instanceof Player && !getOwner().get(Human.class).isSurvival()) {
							HungerComponent hunger = getOwner().add(HungerComponent.class);
							hunger.setExhaustion(hunger.getExhaustion() + 0.025f);
						}
						break;
					case REGENERATION:
						if (getOwner() instanceof Player && !getOwner().get(Human.class).isSurvival()) {
							break;
						}
						if (effect.getTier() == Potion.TIER0 && effect.getTick() >= 1.20f) {
							health.heal(1);
							effect.resetTick();
						} else if (effect.getTier() == Potion.TIER2 && effect.getTick() >= 0.55f) {
							health.heal(1);
							effect.resetTick();
						}
						break;
					case POISON:
						if (getOwner() instanceof Player && !getOwner().get(Human.class).isSurvival()) {
							break;
						}
						if (effect.getTier() == Potion.TIER0 && effect.getTick() >= 1.25f) {
							if (health.getHealth() > 1) {
								health.damage(1);
							}
							effect.resetTick();
						} else if (effect.getTier() == Potion.TIER2 && effect.getTick() >= 0.55f) {
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
						if (effect.getTier() == Potion.TIER0 && effect.getTick() >= 1.25f) {
							health.damage(1);
							effect.resetTick();
						} else if (effect.getTier() == Potion.TIER2 && effect.getTick() >= 0.55f) {
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

	public void addEffect(StatusEffectContainer effect) {
		if (containsEffect(effect.getEffect())) {
			removeEffect(effect.getEffect());
		}
		if (StatusEffect.INSTANT_DAMAGE.equals(effect.getEffect())) {
			if (effect.getTier() == Potion.TIER0) {
				health.damage(6);
			} else if (effect.getTier() == Potion.TIER2) {
				health.damage(12);
			}
		} else if (StatusEffect.INSTANT_HEALTH.equals(effect.getEffect())) {
			if (effect.getTier() == Potion.TIER0) {
				health.heal(6);
			} else if (effect.getTier() == Potion.TIER2) {
				health.heal(12);
			}
		} else {
			getOwner().getNetwork().callProtocolEvent(new EntityEffectEvent(getOwner(), effect));
			list.add(effect);
		}
	}

	public void removeEffect(StatusEffect effect) {
		if (containsEffect(effect)) {
			getOwner().getNetwork().callProtocolEvent(new EntityRemoveEffectEvent(getOwner(), effect));
			list.remove(effect);
		}
	}

	public boolean containsEffect(StatusEffect effect) {
		boolean result = false;
		for (StatusEffectContainer effectContainer : list) {
			result = effectContainer.equals(effect);
			if (result) {
				break;
			}
		}
		return result;
	}
}
