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
package org.spout.vanilla.generator;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.generator.biome.BiomeType;

public abstract class VanillaBiomeType extends BiomeType {
	private final int biomeId;

    //TODO: actually implement these in the selector instead of hard coding them in
    private float minTemp = 0f, maxTemp = 0f;
    private float minHumiditiy = 0f, maxHumidity = 0f;
    private float minElevation = 0f, maxElevation = 0f;

    protected VanillaBiomeType(int biomeId, BiomeDecorator... decorators) {
		super(decorators);
		this.biomeId = biomeId;
	}

	public int getBiomeId() {
		return biomeId;
	}

    public float getMinTemp() {
        return this.minTemp;
    }

    public float getMaxTemp() {
        return this.maxTemp;
    }

    public float getMinHumiditiy() {
        return this.minHumiditiy;
    }

    public float getMaxHumidity()  {
        return this.maxHumidity;
    }

    public float getMinElevation() {
        return this.minElevation;
    }

    public float getMaxElevation() {
        return this.maxElevation;
    }

    public VanillaBiomeType setTemp(float min, float max) {
        this.minTemp = min;
        this.maxTemp = max;
        return this;
    }

    public VanillaBiomeType setHumidity(float min, float max) {
        this.minHumiditiy = min;
        this.maxHumidity = max;
        return this;
    }

    public VanillaBiomeType setElevation(float min, float max) {
        this.minElevation = min;
        this.maxElevation = max;
        return this;
    }
}
