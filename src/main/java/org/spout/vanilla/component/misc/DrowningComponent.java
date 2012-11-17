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
package org.spout.vanilla.component.misc;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Entity;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.source.DamageCause;

/**
 * The drowning component requires a health component and head component
 */
public class DrowningComponent extends EntityComponent{
	private Entity owner;
	private HealthComponent health;
	private HeadComponent head;
	public static final float MAX_AIR = VanillaData.AIR_SECS.getDefaultValue();
	private int damageTimer = 20;

	@Override
	public void onAttached() {
		owner = getOwner();
		health = owner.add(HealthComponent.class);
		head = owner.add(HeadComponent.class);
	}

	@Override
	public void onTick(float dt) {
		if (owner.getWorld().getBlock(head.getPosition()).getMaterial() != VanillaMaterials.WATER) {
			setAir(MAX_AIR);
			return;
		}
		setAir(getAir() - dt);
		if (getAir() < 0) {
			// out of air; damage one heart every second
			if (damageTimer-- < 0) {
				health.damage(2, DamageCause.DROWN);
				damageTimer = 20;
			}
		}
	}

	public float getAir() {
		return getData().get(VanillaData.AIR_SECS);
	}

	public void setAir(float airSecs) {
		getData().put(VanillaData.AIR_SECS, airSecs);
	}
}
