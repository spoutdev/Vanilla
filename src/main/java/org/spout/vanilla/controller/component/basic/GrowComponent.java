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
package org.spout.vanilla.controller.component.basic;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;
import org.spout.api.util.Parameter;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.util.VanillaNetworkUtil;

/**
 * Provides a baby state and allows the entity to grow over time
 */
public class GrowComponent extends LogicRunnable<Creature> {
	private long growthTicks = 0;

	public GrowComponent(Creature parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public void onRegistration() {
		growthTicks = getParent().data().get(VanillaData.GROWTH_TICKS);
	}

	@Override
	public void onUnregistration() {
		getParent().data().put(VanillaData.GROWTH_TICKS, growthTicks);
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

	@Override
	public void run() {
		growthTicks++;
		if (growthTicks >= 0) {
			List<Parameter<?>> parameters = new ArrayList<Parameter<?>>(1);
			parameters.add(EntityMetadataMessage.Parameters.META_BABYANIMALSTAGE.get());
			VanillaNetworkUtil.broadcastPacket(new EntityMetadataMessage(getParent().getParent().getId(), parameters));
		}
	}

	@Override
	public boolean shouldRun(float dt) {
		return isBaby();
	}
}
