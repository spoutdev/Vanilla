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
package org.spout.vanilla.world.generator.biome;

import java.awt.Color;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.Decorator;
import org.spout.api.util.config.annotated.Setting;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.world.generator.biome.selector.BiomeSelectorElement;

public abstract class VanillaBiome extends Biome implements BiomeSelectorElement {
	private final int biomeId;
	@Setting
	private Climate climate = Climate.MODERATE;
	@Setting({"grass-color-multiplier"})
	private Color grassColorMultiplier = new Color(255, 255, 255);
	@Setting({"foliage-color-multiplier"})
	private Color foliageColorMultiplier = new Color(255, 255, 255);
	@Setting({"water-color-multiplier"})
	private Color waterColorMultiplier = new Color(255, 255, 255);

	protected VanillaBiome(int biomeId, Decorator... decorators) {
		super(decorators);
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
