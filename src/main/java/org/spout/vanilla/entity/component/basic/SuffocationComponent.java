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

import org.spout.api.entity.BasicComponent;
import org.spout.api.entity.Entity;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.entity.living.Living;
import org.spout.vanilla.entity.source.DamageCause;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;

public class SuffocationComponent extends BasicComponent<Living> {
	private int airTicks = 0;
	private int maxAirTicks = 300;

	public SuffocationComponent(TickPriority priority) {
		super(priority);
	}

	@Override
	public void onAttached() {
		airTicks = getParent().getDataMap().get(VanillaData.AIR_TICKS);
	}

	@Override
	public void onDetached() {
		getParent().getDataMap().put(VanillaData.AIR_TICKS, airTicks);
	}

	public void setMaxAirTicks(int maxAirTicks) {
		this.maxAirTicks = maxAirTicks;
	}

	public int getMaxAirTicks() {
		return maxAirTicks;
	}

	public int getAirTicks() {
		return airTicks;
	}

	public void setAirTicks(int ticks) {
		this.airTicks = ticks;
	}

	@Override
	public boolean canTick() {
		return false; //disabled for now
	}

	@Override
	public void onTick(float dt) {
		// Handle drowning and suffocation damage
		Entity e = getParent().getParent();
		int airTicks = getAirTicks();
		Point headPos = getParent().getHeadPosition();
		if (e.isObserver() || headPos.getWorld().getChunkFromBlock(headPos, LoadOption.NO_LOAD) != null) {
			Block head = e.getWorld().getBlock(headPos, e);
			if (head.isMaterial(VanillaMaterials.GRAVEL, VanillaMaterials.SAND, VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
				airTicks++;
				if (head.isMaterial(VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
					// Drowning
					if (airTicks >= getMaxAirTicks() && airTicks % 20 == 0) {
						getParent().getHealth().damage(2, DamageCause.DROWN);
					}
				} else {
					// Suffocation
					if (airTicks % 10 == 0) {
						getParent().getHealth().damage(1, DamageCause.SUFFOCATE);
					}
				}
			} else {
				airTicks = 0;
			}
		}
		setAirTicks(airTicks);
	}
}
