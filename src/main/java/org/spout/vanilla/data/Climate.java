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
package org.spout.vanilla.data;

import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.cuboid.Block;
import org.spout.vanilla.world.generator.VanillaBiome;

/**
 * Enum of Biome climates
 */
public enum Climate {
	WARM(true, false, false, true),
	MODERATE(true, false, false, true),
	COLD(false, true, true, false);

	private final boolean melting, freezing, snow, rain;
	private Climate(boolean melting, boolean freezing, boolean snow, boolean rain) {
		this.melting = melting;
		this.freezing = freezing;
		this.snow = snow;
		this.rain = rain;
	}

	/**
	 * Gets whether snow and ice melts in this Climate
	 * 
	 * @return True if ice and snow melts, False if not
	 */
	public boolean isMelting() {
		return this.melting;
	}

	/**
	 * Gets whether water freezes in this Climate
	 * 
	 * @return True if water freezes, False if not
	 */
	public boolean isFreezing() {
		return this.freezing;
	}

	/**
	 * Gets whether there is rainfall in this Climate
	 * 
	 * @return True if it rains, False if not
	 */
	public boolean hasRainfall() {
		return this.rain;
	}

	/**
	 * Gets whether there is snowfall in this Climate
	 * 
	 * @return True if it snows, False if not
	 */
	public boolean hasSnowfall() {
		return this.snow;
	}

	/**
	 * Gets the Climate at the Block specified
	 * 
	 * @param block position
	 * @return the Climate
	 */
	public static Climate get(Block block) {
		Biome biome = block.getBiomeType();
		if (biome instanceof VanillaBiome) {
			return ((VanillaBiome) biome).getClimate();
		} else {
			return Climate.MODERATE;
		}
	}
}
