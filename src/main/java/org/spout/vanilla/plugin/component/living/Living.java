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
package org.spout.vanilla.plugin.component.living;

import org.spout.api.ai.goap.GoapAIComponent;
import org.spout.api.component.impl.NavigationComponent;
import org.spout.api.entity.Entity;

import org.spout.vanilla.api.component.living.LivingComponent;
import org.spout.vanilla.api.component.misc.DrowningComponent;
import org.spout.vanilla.api.component.misc.HeadComponent;
import org.spout.vanilla.api.component.misc.HealthComponent;
import org.spout.vanilla.api.data.effect.StatusEffect;

import org.spout.vanilla.plugin.ai.examiner.VanillaBlockExaminer;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.misc.Burn;
import org.spout.vanilla.plugin.component.misc.Drowning;
import org.spout.vanilla.plugin.component.misc.EffectsComponent;
import org.spout.vanilla.plugin.component.misc.Health;

public abstract class Living extends LivingComponent {
	private HeadComponent head;
	private HealthComponent health;
	private DrowningComponent drowning;
	private NavigationComponent navigation;
	private GoapAIComponent ai;

	@Override
	public void onAttached() {
		super.onAttached();
		Entity holder = getOwner();
		head = holder.add(HeadComponent.class);
		health = holder.add(Health.class);
		drowning = holder.add(Drowning.class);
		navigation = holder.add(NavigationComponent.class);
		navigation.setDefaultExaminers(new VanillaBlockExaminer());
		ai = holder.add(GoapAIComponent.class);
		holder.add(Burn.class);
		holder.setSavable(true);
	}

	@Override
	public HeadComponent getHead() {
		return head;
	}

	@Override
	public HealthComponent getHealth() {
		return health;
	}

	@Override
	public DrowningComponent getDrowning() {
		return drowning;
	}

	@Override
	public NavigationComponent getNavigation() {
		return navigation;
	}

	@Override
	public GoapAIComponent getAI() {
		return ai;
	}

	@Override
	protected byte getCommonMetadata() {
		byte value = 0;
		if (getOwner().has(Burn.class)) {
			value = (byte) (value | ((getOwner().get(Burn.class).isOnFire() ? 1 : 0) << 0));
		}

		value = (byte) (value | ((isSneaking() ? 1 : 0) << 1));
		value = (byte) (value | ((isRiding() ? 1 : 0) << 2));

		if (getOwner().has(Human.class)) {
			value = (byte) (value | ((getOwner().get(Human.class).isSprinting() ? 1 : 0) << 3));
		}

		value = (byte) (value | ((isEatingBlocking() ? 1 : 0) << 4));

		if (getOwner().has(EffectsComponent.class)) {
			value = (byte) (value | ((getOwner().get(EffectsComponent.class).containsEffect(StatusEffect.INVISIBILITY) ? 1 : 0) << 5));
		}

		return value;
	}
}
