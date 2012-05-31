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
package org.spout.vanilla.controller.living;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.Parameter;

import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.EntityMetadataMessage;
import org.spout.vanilla.util.VanillaNetworkUtil;

public abstract class Creature extends Living {
	private long growthTicks = 0;
	private int lineOfSight = 1;

	protected Creature(VanillaControllerType type) {
		super(type);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		growthTicks = data().get(VanillaData.GROWTH_TICKS);
		lineOfSight = data().get(VanillaData.LINE_OF_SIGHT);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		// Keep track of growth
		if (growthTicks < 0) {
			growthTicks++;
			if (growthTicks >= 0) {
				List<Parameter<?>> parameters = new ArrayList<Parameter<?>>(1);
				parameters.add(EntityMetadataMessage.Parameters.META_BABYANIMALSTAGE.get());
				VanillaNetworkUtil.broadcastPacket(new EntityMetadataMessage(getParent().getId(), parameters));
			}
		}

		// Handle drowning and suffocation damage
		Point headPos = getHeadPosition();
		if (getParent().isObserver() || headPos.getWorld().getChunkFromBlock(headPos, LoadOption.NO_LOAD) != null) {
			Block head = getParent().getWorld().getBlock(headPos);
			if (head.getMaterial().equals(VanillaMaterials.GRAVEL, VanillaMaterials.SAND, VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
				airTicks++;
				if (head.getMaterial().equals(VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
					// Drowning
					if (airTicks >= 300 && airTicks % 20 == 0) {
						damage(4, DamageCause.DROWN);
					}
				} else {
					// Suffocation
					if (airTicks % 10 == 0) {
						damage(1, DamageCause.SUFFOCATE);
					}
				}
			} else {
				airTicks = 0;
			}
		}
	}

	@Override
	public void onSave() {
		super.onSave();
		data().put(VanillaData.GROWTH_TICKS, growthTicks);
		data().put(VanillaData.LINE_OF_SIGHT, lineOfSight);
	}

	/**
	 * Whether or not the creature is a baby.
	 * @return true if a baby
	 */
	public boolean isBaby() {
		return growthTicks < 0;
	}

	/**
	 * Returns the amount of time until the baby is an adult. Fully grown is 0 and not grown is 23999.
	 * @return time until adult
	 */
	public long getTimeUntilAdult() {
		return Math.abs(growthTicks);
	}

	/**
	 * Sets the time until the entity is an adult. Fully grown is 0 and not grown is 23999.
	 * @param timeUntilAdult
	 */
	public void setTimeUntilAdult(long timeUntilAdult) {
		this.growthTicks -= timeUntilAdult;
	}

	/**
	 * Gets the line of sight this creature has.
	 * @return
	 */
	public int getLineOfSight() {
		return lineOfSight;
	}

	/**
	 * Sets the line of sight this creature has
	 * @param lineOfSight
	 */
	public void setLineOfSight(int lineOfSight) {
		this.lineOfSight = lineOfSight;
	}
}
