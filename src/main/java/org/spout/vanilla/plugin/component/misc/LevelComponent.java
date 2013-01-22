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
import org.spout.api.entity.Player;

import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.protocol.entity.player.ExperienceChangeEvent;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerExperienceMessage;

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
	private void setLevel(short level) {
		getData().put(VanillaData.EXPERIENCE_LEVEL, level);
		updateUi();
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
	private void setExperience(short experience) {
		ExperienceChangeEvent event = new ExperienceChangeEvent(getOwner(), getExperience(), experience);
		Spout.getEventManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		getData().put(VanillaData.EXPERIENCE_AMOUNT, event.getNewExp());
		updateUi();
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
	private void setProgress(float progress) {
		getData().put(VanillaData.EXPERIENCE_BAR_PROGRESS, progress);
		updateUi();
	}

	/**
	 * Add experience points from a player.
	 * @param amount the amount to add.
	 */
	public void addExperience(int amount) {
		setProgress(getProgress() + ((float)amount / getXpCap()));
		setExperience((short) (getExperience() + amount));
		while (getProgress() >= 1.0f) {
			setLevel((short) (getLevel() + 1));
			setProgress(getProgress() - 1.0f);
		}
	}

	/**
	 * Remove experience points from a player
	 * @param amount The amount to remove.
	 */
	public void removeExperience(int amount) {
		setProgress(getProgress() - (amount / getXpCap()));
		setExperience((short) (getExperience() - amount));
		while (getProgress() <= 0.0f) {
			setLevel((short) (getLevel() - 1));
			setProgress(getProgress() + 1.0f);
		}
	}

	/**
	 * Get the XP cap of the current level.
	 * @return The XP cap of the current level.
	 */
	public int getXpCap() {
		if (getLevel() >= 30) {
			return 62 + (this.getLevel() - 30) * 7;
		} else if (this.getLevel() >= 15) {
			return 17 + (this.getLevel() - 15) * 3;
		} else {
			return 17;
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
