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
package org.spout.vanilla.api.component.misc;

import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;

import org.spout.vanilla.api.data.GameMode;
import org.spout.vanilla.api.data.VanillaData;
import org.spout.vanilla.api.event.cause.BlockDamageCause;
import org.spout.vanilla.api.event.cause.DamageCause.DamageType;

/**
 * Component that handles a entity drowning in water.
 * The drowning component requires a health component and head component
 */
public abstract class DrowningComponent extends EntityComponent {

	public float getNbBubExact() {
		final float maxSecsBubbles = VanillaData.AIR_SECS.getDefaultValue();
		final float secsBubbles = getData().get(VanillaData.AIR_SECS);
		
		return secsBubbles / maxSecsBubbles * 10f;
	}

	/**
	 * Hide bubbles in the GUI
	 */
	public abstract void hideBubbles();

	/**
	 * Show bubbles in the GUI
	 */
	public abstract void showBubbles();

	/**
	 * Retrieve the amount of air the entity currently have.
	 * @return The amount of air in seconds.
	 */
	public float getAir() {
		return getData().get(VanillaData.AIR_SECS);
	}

	/**
	 * Sets the amount of air the entity currently have.
	 * @param airSecs The amount of air (in seconds) that the entity have.
	 */
	public void setAir(float airSecs) {
		getData().put(VanillaData.AIR_SECS, airSecs);
	}
}
