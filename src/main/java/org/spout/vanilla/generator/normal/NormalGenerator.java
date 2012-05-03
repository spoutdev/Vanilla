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
package org.spout.vanilla.generator.normal;

import org.spout.api.math.Vector3;

import org.spout.vanilla.generator.VanillaBiomes;
import org.spout.vanilla.generator.VanillaGeneratorBase;
import org.spout.vanilla.generator.selector.WhittakerNoiseSelector;

public class NormalGenerator extends VanillaGeneratorBase {
	private static WhittakerNoiseSelector selector;
	private long seed = 1337;

	@Override
	public void registerBiomes() {
		selector = new WhittakerNoiseSelector(2.0);
		setSelector(selector);
		register(VanillaBiomes.OCEAN);
		register(VanillaBiomes.PLAIN);
		register(VanillaBiomes.DESERT);
		register(VanillaBiomes.MOUNTAINS);
		register(VanillaBiomes.FOREST);
		register(VanillaBiomes.TAIGA);
		register(VanillaBiomes.SWAMP);
		register(VanillaBiomes.TUNDRA);
		register(VanillaBiomes.BEACH);
		register(VanillaBiomes.SMALL_MOUNTAINS);
		register(VanillaBiomes.JUNGLE);
	}

	@Override
	public String getName() {
		return "VanillaNormal";
	}

	public Vector3 getSafeSpawn() {
		int shift = 0;
		while (selector.pickBiome(shift, 0, this.seed) == 0) {
			shift += 16;
		}
		return new Vector3((float) shift + 0.5F, 75.5F, 0.5F);
	}

	public void setSeed(long s) {
		this.seed = s;
	}

	public long getSeed() {
		return this.seed;
	}
}
