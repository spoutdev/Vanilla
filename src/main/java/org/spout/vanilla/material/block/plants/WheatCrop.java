/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.block.plants;

import java.util.Random;

import org.spout.api.material.Material;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.generic.Plant;
import org.spout.vanilla.material.block.generic.Solid;

public class WheatCrop extends Solid implements Plant {
	private Random rand = new Random();

	public WheatCrop() {
		super("Wheat Crop", 59);
	}

	@Override
	public boolean hasGrowthStages() {
		return true;
	}

	@Override
	public int getNumGrowthStages() {
		return 8;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 9;
	}

	@Override
	public Material getDrop() {
		return VanillaMaterials.SEEDS;
	}

	@Override
	public int getDropCount() {
		return rand.nextInt(4);
	}

	// TODO: Grow
	// TODO: Trampling
	// TODO: Multiple drops
}
