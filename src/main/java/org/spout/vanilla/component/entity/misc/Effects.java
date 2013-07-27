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
package org.spout.vanilla.component.entity.misc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;

import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.data.effect.EntityEffect;
import org.spout.vanilla.data.effect.EntityEffectType;
import org.spout.vanilla.event.entity.network.EntityEffectEvent;
import org.spout.vanilla.event.entity.network.EntityRemoveEffectEvent;

/**
 * Component handling status effects. This includes food poisoning, regeneration, etc.
 */
public class Effects extends VanillaEntityComponent {
	private final Set<EntityEffect> effects = new HashSet<EntityEffect>();
	private Player player;

	/**
	 * Returns the player associated with this component.
	 * @return player attached to this component
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Applies an effect on the player.
	 * @param effect to apply
	 */
	public void add(EntityEffect effect) {
		int amount = 6 << effect.getAmplifier();
		Health health = player.add(Health.class);
		switch (effect.getType()) {
			// don't send these to the client
			case INSTANT_DAMAGE:
				health.damage(amount);
				return;
			case INSTANT_HEALTH:
				health.heal(amount);
				return;
		}
		player.getNetworkSynchronizer().callProtocolEvent(new EntityEffectEvent(player, effect));
		effects.add(effect);
	}

	/**
	 * Removes a player's effect.
	 * @param type type of effect to remove
	 */
	public void remove(EntityEffectType type) {
		if (!contains(type)) {
			return;
		}

		// send the update
		player.getNetworkSynchronizer().callProtocolEvent(new EntityRemoveEffectEvent(player, type));

		// look up the effect with this type and remove it
		Iterator<EntityEffect> i = effects.iterator();
		while (i.hasNext()) {
			if (i.next().getType() == type) {
				i.remove();
				break;
			}
		}
	}

	/**
	 * Returns true if the player has the specified effect type applied.
	 * @param type to check for
	 * @return true if effect is applied
	 */
	public boolean contains(EntityEffectType type) {
		for (EntityEffect effect : effects) {
			if (effect.getType() == type) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onAttached() {
		Entity owner = getOwner();
		if (!(owner instanceof Player)) {
			throw new IllegalStateException("Effects component may only be attached to players.");
		}
		player = (Player) owner;
	}

	@Override
	public boolean canTick() {
		return effects.size() > 0;
	}

	@Override
	public void onTick(float dt) {
		for (EntityEffect effect : effects) {
			effect.tick(dt);
			// time ran out, remove effect
			if (effect.getDuration() <= 0) {
				remove(effect.getType());
				continue;
			}

			if (!player.add(Human.class).isSurvival()) {
				continue;
			}

			// apply server side effects
			int ticks = effect.getTicks();
			Health health = player.add(Health.class);
			switch (effect.getType()) {
				case HUNGER:
					Hunger hunger = player.add(Hunger.class);
					hunger.setExhaustion(hunger.getExhaustion() + 0.025f);
					break;
				case REGENERATION:
					// heal one every 25 ticks
					if (ticks >= 25) {
						effect.resetTicks();
						health.heal(1);
					}
					break;
				case POISON:
					// damage 1 every 25 ticks, but don't kill the player
					if (ticks >= 25 && health.getHealth() > 1) {
						effect.resetTicks();
						health.damage(1);
					}
					break;
				case WITHER:
					// damage 1 every 40 ticks, can kill the player
					if (ticks >= 40) {
						health.damage(1);
					}
					break;
			}
		}
	}
}
