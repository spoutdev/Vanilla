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
package org.spout.vanilla.entity.component.basic;

import org.spout.api.tickable.LogicPriority;

import org.spout.vanilla.entity.living.Creature;
import org.spout.vanilla.data.effect.SoundEffect;
import org.spout.vanilla.data.effect.store.SoundEffects;

public class CreatureHealthComponent extends HealthComponent {
	private SoundEffect babyHurtEffect = SoundEffects.NONE;
	private SoundEffect adultHurtEffect = SoundEffects.NONE;

	public SoundEffect getAdultHurtEffect() {
		return adultHurtEffect;
	}

	public SoundEffect getBabyHurtEffect() {
		return babyHurtEffect;
	}

	public void setAdultHurtEffect(SoundEffect effect) {
		adultHurtEffect = effect;
	}

	public void setBabyHurtEffect(SoundEffect effect) {
		babyHurtEffect = effect;
	}

	@Override
	public void setHurtEffect(SoundEffect effect) {
		adultHurtEffect = effect.randomPitch(0.2f);
		babyHurtEffect = effect.adjust(effect.getDefaultVolume(), effect.getDefaultPitch() + 0.5f).randomPitch(0.2f);
	}

	@Override
	public SoundEffect getHurtEffect() {
		return getParent().getGrowing().isBaby() ? babyHurtEffect : adultHurtEffect;
	}

	public CreatureHealthComponent(Creature parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public Creature getParent() {
		return (Creature) super.getParent();
	}
}
