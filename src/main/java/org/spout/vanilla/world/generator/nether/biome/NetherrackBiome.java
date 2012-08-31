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
package org.spout.vanilla.world.generator.nether.biome;

import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiome;
import org.spout.vanilla.world.generator.nether.decorator.BlockPatchDecorator;
import org.spout.vanilla.world.generator.nether.decorator.FireDecorator;
import org.spout.vanilla.world.generator.nether.decorator.GlowstoneDecorator;
import org.spout.vanilla.world.generator.nether.decorator.LavaFallDecorator;
import org.spout.vanilla.world.generator.nether.decorator.NetherMushroomDecorator;

public class NetherrackBiome extends VanillaBiome {
	public NetherrackBiome(int id) {
		super(id, new BlockPatchDecorator(VanillaMaterials.SOUL_SAND), new BlockPatchDecorator(VanillaMaterials.GRAVEL),
				new LavaFallDecorator(), new FireDecorator(), new GlowstoneDecorator(), new NetherMushroomDecorator());
	}

	@Override
	public String getName() {
		return "Netherrack";
	}
}
