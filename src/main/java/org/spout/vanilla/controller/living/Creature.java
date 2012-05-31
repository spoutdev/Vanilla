/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.geo.cuboid.Block;
import org.spout.api.util.Parameter;

import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.EntityMetadataMessage;
import org.spout.vanilla.world.Difficulty;

import static org.spout.vanilla.protocol.VanillaNetworkSynchronizer.broadcastPacket;

public abstract class Creature extends Living {
	private long timeUntilAdult = 0;
	private int[] meleeDamage = new int[Difficulty.values().length];

	protected Creature(VanillaControllerType type) {
		super(type);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		// Keep track of growth
		if (timeUntilAdult < 0) {
			timeUntilAdult++;
			if (timeUntilAdult >= 0) {
				List<Parameter<?>> parameters = new ArrayList<Parameter<?>>(1);
				parameters.add(EntityMetadataMessage.Parameters.META_BABYANIMALSTAGE.get());
				broadcastPacket(new EntityMetadataMessage(getParent().getId(), parameters));
			}
		}

		// Handle drowning and suffocation damage
		Block head = getParent().getWorld().getBlock(getHeadPosition());
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

	/**
	 * Whether or not the creature is a baby.
	 * @return true if a baby
	 */
	public boolean isBaby() {
		return timeUntilAdult < 0;
	}

	/**
	 * Returns the amount of time until the baby is an adult. Fully grown is 0 and not grown is 23999.
	 * @return time until adult
	 */
	public long getTimeUntilAdult() {
		return Math.abs(timeUntilAdult);
	}

	/**
	 * Sets the time until the entity is an adult. Fully grown is 0 and not grown is 23999.
	 * @param timeUntilAdult
	 */
	public void setTimeUntilAdult(long timeUntilAdult) {
		this.timeUntilAdult -= timeUntilAdult;
	}

	/**
	 * Returns the amount of melee damage that this creature deals on the given {@link Difficulty}.
	 * @param difficulty Difficulty of the world that this creature is in
	 * @return Amount of melee damage based on world difficulty
	 */
	public int getMeleeDamage(Difficulty difficulty) {
		return meleeDamage[difficulty.getId()];
	}

	/**
	 * Sets the amount of melee damage that this creature deals on all {@link Difficulty}s.
	 * @param damage Amount of melee damage
	 */
	public Creature setMeleeDamage(int damage) {
		for (Difficulty difficulty : Difficulty.values()) {
			setMeleeDamage(difficulty, damage);
		}

		return this;
	}

	/**
	 * Sets the amount of melee damage that this creature deals on the given {@link Difficulty}.
	 * @param difficulty Difficulty to set the melee damage for
	 * @param damage Amount of melee damage
	 */
	public Creature setMeleeDamage(Difficulty difficulty, int damage) {
		meleeDamage[difficulty.getId()] = damage;
		return this;
	}
}
