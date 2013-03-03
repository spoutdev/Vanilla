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
package org.spout.vanilla.component.entity.misc;

import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.entity.player.ExperienceChangeEvent;
import org.spout.vanilla.protocol.msg.player.PlayerExperienceMessage;

/**
 * Components that handles everything about the Experience system.
 */
public class Level extends EntityComponent {
	/**
	 * Gets current level
	 * @return the level
	 */
	public short getLevel() {
		return getData().get(VanillaData.EXPERIENCE_LEVEL);
	}

	/**
	 * Gets current experience
	 * @return the total exp
	 */
	public short getExperience() {
		return getData().get(VanillaData.EXPERIENCE_AMOUNT);
	}

	/**
	 * Sets current experience
	 * @param xp the total xp
	 * @return false if the experience change event is cancelled
	 */
	public boolean setExperience(short xp) {
		ExperienceChangeEvent event = new ExperienceChangeEvent(getOwner(), getExperience(), xp);
		Spout.getEventManager().callEvent(event);
		if (event.isCancelled()) {
			return false;
		}
		getData().put(VanillaData.EXPERIENCE_AMOUNT, xp);
		getData().put(VanillaData.EXPERIENCE_LEVEL, convertXpToLevel(xp));
		updateUi();
		return true;
	}

	/**
	 * Gets current progress of the progress bar, this value will be between 0 and 1 (0 = 0%, 1 = 100%)
	 * @return the progress bar
	 */
	public float getProgress() {
		return getData().get(VanillaData.EXPERIENCE_BAR_PROGRESS);
	}

	/**
	 * Changes the experience points of a player.
	 * @param amount the amount to add. The player loses experience if amount is negative
	 */
	public void addExperience(int amount) {
		short newExperience = (short) (getExperience() + amount);
		if (newExperience < 0) {
			newExperience = 0;
		}
		if (!setExperience(newExperience)) {
			return;
		}
		float newProgress = getProgress() + ((float) amount / getXpCap(getLevel()));
		if (newProgress < 0.f || newProgress >= 1.0f) {
			short newLevel = convertXpToLevel(newExperience);
			getData().put(VanillaData.EXPERIENCE_LEVEL, newLevel);
			newProgress = (float) (newExperience - convertLevelToXp(newLevel)) / getXpCap(newLevel);
		}
		getData().put(VanillaData.EXPERIENCE_BAR_PROGRESS, newProgress);
		updateUi();
	}

	/**
	 * Reduces current level without changing progress. Modifies total xp accordingly
	 * @param reduction number of levels to be removed
	 */
	public void removeLevels(int reduction) {
		addLevel(-reduction);
	}

	/**
	 * Increase the current level without changing progress. Modifies total xp accordingly.
	 * @param addition numbers of levels to be added.
	 */
	public void addLevel(int addition) {
		short newLevel = (short) (getLevel() + addition);
		if (newLevel < 0) {
			newLevel = 0;
		}
		short newExperience = (short) (convertLevelToXp(newLevel) + getProgress() * getXpCap(newLevel));
		setExperience(newExperience);
	}

	/**
	 * Get the XP cap of a given level.
	 * @param level The experience level
	 * @return The XP cap
	 */
	public static int getXpCap(short level) {
		if (level >= 30) {
			return 62 + (level - 30) * 7;
		} else if (level >= 15) {
			return 17 + (level - 15) * 3;
		} else {
			return 17;
		}
	}

	/**
	 * Determines the experience level of a player with a given total xp
	 * @param xp The total xp
	 * @return The experience level
	 */
	public static short convertXpToLevel(short xp) {
		if (xp < 272) {
			return (short) (xp / 17);
		} else if (xp < 887) {
			return (short) ((Math.sqrt(24.f * xp - 5159) + 59) / 6);
		} else {
			return (short) ((Math.sqrt(56.f * xp - 32511) + 303) / 14);
		}
	}

	/**
	 * Determines the xp required to reach a given experience level
	 * @param level The experience level
	 * @return The total xp
	 */
	public static short convertLevelToXp(short level) {
		int l = (int) level;
		if (l > 30) {
			return (short) ((7 * l - 86) * (l - 31) / 2 + 887);
		} else if (l > 15) {
			return (short) ((3 * l - 11) * (l - 16) / 2 + 272);
		} else {
			return (short) (17 * l);
		}
	}

	/**
	 * Update the player UI bar.
	 */
	private void updateUi() {

		if (!(getOwner() instanceof Player)) {
			return;
		}
		Player p = (Player) getOwner();
		p.getSession().send(false, new PlayerExperienceMessage(getProgress(), getLevel(), getExperience()));
	}
}
