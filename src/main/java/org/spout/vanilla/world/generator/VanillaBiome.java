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
package org.spout.vanilla.world.generator;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.Decorator;
import org.spout.vanilla.data.Climate;

public abstract class VanillaBiome extends Biome {
	private final int biomeId;
	private Climate climate = Climate.MODERATE;

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
}
