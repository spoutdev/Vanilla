/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.controller.entity.living;

import org.spout.api.entity.Entity;
import org.spout.api.util.Parameter;
import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.protocol.msg.EntityMetadataMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class Creature extends Living {
	private boolean baby = false;
	private long timeUntilAdult = 0;

	protected Creature(VanillaControllerType type) {
		super(type);
	}
	
	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		// Keep track of growth
		if (timeUntilAdult < 0) {
			baby = true;
			timeUntilAdult++;
			if (timeUntilAdult >= 0) {
				baby = false;
				List<Parameter<?>> parameters = new ArrayList<Parameter<?>>(1);
				parameters.add(new Parameter<Integer>(Parameter.TYPE_INT, 12, (int) timeUntilAdult));
				Entity parent = getParent();
				broadcastPacket(new EntityMetadataMessage(parent.getId(), parameters));
			}
		}
	}

	/**
	 * Sets if the entity is a baby.
	 *
	 * @param baby
	 */
	public void setBaby(boolean baby) {
		this.baby = baby;
		this.timeUntilAdult = -23999;
	}

	/**
	 * Whether or not the creature is a baby.
	 *
	 * @return true if a baby
	 */
	public boolean isBaby() {
		return baby;
	}

	/**
	 * Returns the amount of time until the baby is an adult. Fully grown is 0 and not grown is 23999.
	 *
	 * @return time until adult
	 */
	public long getTimeUntilAdult() {
		return Math.abs(timeUntilAdult);
	}

	/**
	 * Sets the time until the entity is an adult. Fully grown is 0 and not grown is 23999.
	 *
	 * @param timeUntilAdult
	 */
	public void setTimeUntilAdult(long timeUntilAdult) {
		this.timeUntilAdult -= timeUntilAdult;
	}
}
