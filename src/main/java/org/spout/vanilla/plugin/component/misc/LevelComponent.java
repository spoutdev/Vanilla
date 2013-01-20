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
package org.spout.vanilla.plugin.component.misc;

import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;

import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.protocol.entity.player.ExperienceChangeEvent;

public class LevelComponent extends EntityComponent {
	/**
	 * Gets current level
	 * @return the level
	 */
	public short getLevel() {
		return getData().get(VanillaData.EXPERIENCE_LEVEL);
	}

	/**
	 * Sets current level, doesn't adjust total exp
	 * @param level the level
	 */
	public void setLevel(short level) {
		getData().put(VanillaData.EXPERIENCE_LEVEL, level);
	}

	/**
	 * Gets current experience
	 * @return the total exp
	 */
	public short getExperience() {
		return getData().get(VanillaData.EXPERIENCE_AMOUNT);
	}

	/**
	 * Sets the total experience, does not add to the current amount.
	 * @param experience the total exp
	 */
	public void setExperience(short experience) {
		ExperienceChangeEvent event = new ExperienceChangeEvent(getOwner(), getExperience(), experience);
		Spout.getEventManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		getData().put(VanillaData.EXPERIENCE_AMOUNT, event.getNewExp());
	}

	/**
	 * Gets current progress of the progress bar, this value will be between 0 and 1 (0 = 0%, 1 = 100%)
	 * @return the progress bar
	 */
	public float getProgress() {
		return getData().get(VanillaData.EXPERIENCE_BAR_PROGRESS);
	}

	/**
	 * Sets current progress of the progress bar, this value will be between 0 and 1 (0 = 0%, 1 = 100%)
	 * @param progress
	 */
	public void setProgress(float progress) {
		getData().put(VanillaData.EXPERIENCE_BAR_PROGRESS, progress);
	}
}
