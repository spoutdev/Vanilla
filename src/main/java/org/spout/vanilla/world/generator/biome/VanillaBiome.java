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
package org.spout.vanilla.world.generator.biome;

import java.awt.Color;

import org.spout.api.generator.biome.Biome;
import org.spout.api.util.config.annotated.Setting;

import org.spout.vanilla.data.Climate;

public abstract class VanillaBiome extends Biome {
	private final int biomeId;
	@Setting
	private Climate climate = Climate.MODERATE;
	@Setting({"color-multiplier", "grass"})
	private Color grassColorMultiplier = new Color(255, 255, 255);
	@Setting({"color-multiplier", "foliage"})
	private Color foliageColorMultiplier = new Color(255, 255, 255);
	@Setting({"color-multiplier", "water"})
	private Color waterColorMultiplier = new Color(255, 255, 255);

	protected VanillaBiome(int biomeId) {
		this.biomeId = biomeId;
	}

	public int getBiomeId() {
		return biomeId;
	}

	/**
	 * Gets the Climate of this Biome
	 *
	 * @return the climate
	 */
	public Climate getClimate() {
		return this.climate;
	}

	/**
	 * Sets the Climate for this Biome
	 *
	 * @param climate to set to
	 */
	public void setClimate(Climate climate) {
		this.climate = climate;
	}

	public Color getGrassColorMultiplier() {
		return grassColorMultiplier;
	}

	public void setGrassColorMultiplier(Color grassColorMultiplier) {
		this.grassColorMultiplier = grassColorMultiplier;
	}

	public Color getFoliageColorMultiplier() {
		return foliageColorMultiplier;
	}

	public void setFoliageColorMultiplier(Color foliageColorMultiplier) {
		this.foliageColorMultiplier = foliageColorMultiplier;
	}

	public Color getWaterColorMultiplier() {
		return waterColorMultiplier;
	}

	public void setWaterColorMultiplier(Color waterColorMultiplier) {
		this.waterColorMultiplier = waterColorMultiplier;
	}
}