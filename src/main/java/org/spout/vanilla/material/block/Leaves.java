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
package org.spout.vanilla.material.block;

import org.spout.api.material.Material;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.generic.Solid;

import java.util.Random;

public class Leaves extends Solid {
	public static final Leaves DEFAULT = register(new Leaves("Leaves"));
	public static final Leaves SPRUCE = register(new Leaves("Spruce Leaves", 0, DEFAULT));
	public static final Leaves BIRCH = register(new Leaves("Birch Leaves", 0, DEFAULT));
	public static final Leaves JUNGLE = register(new Leaves("Jungle Leaves", 0, DEFAULT));
	private Random rand = new Random();

	private Leaves(String name) {
		super(name, 18);
		this.setDefault();
	}

	private Leaves(String name, int data, Leaves parent) {
		super(name, 18, data, parent);
		this.setDefault();
	}

	private void setDefault() {
		this.setHardness(0.2F).setResistance(0.3F).setOpacity((byte) 0);
	}

	// TODO: Shears
	@Override
	public Material getDrop() {
		if (rand.nextInt(20) == 0) {
			return VanillaMaterials.SAPLING;
		} else if (rand.nextInt(200) == 0) {
			return VanillaMaterials.RED_APPLE;
		}

		return VanillaMaterials.AIR;
	}

	// TODO: Decay
}
