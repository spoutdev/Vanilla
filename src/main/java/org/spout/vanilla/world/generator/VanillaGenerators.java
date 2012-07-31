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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spout.vanilla.world.generator.flat.FlatGenerator;
import org.spout.vanilla.world.generator.nether.NetherGenerator;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

public class VanillaGenerators {
	private static final Map<String, VanillaGenerator> generators = new ConcurrentHashMap<String, VanillaGenerator>();
	public static final NormalGenerator NORMAL = addGenerator("normal", new NormalGenerator());
	public static final NetherGenerator NETHER = addGenerator("nether", new NetherGenerator());
	public static final FlatGenerator FLAT = addGenerator("flat", new FlatGenerator(64));
	public static final TheEndGenerator THE_END = addGenerator("the_end", new TheEndGenerator());

	/**
	 * Maps a Vanilla Generator to a certain name
	 * @param name of the generator
	 * @param generator to map to the name
	 * @return the generator
	 */
	public static <T extends VanillaGenerator> T addGenerator(String name, T generator) {
		generators.put(name, generator);
		return generator;
	}

	/**
	 * Gets the Vanilla Generator mapped to a certain name
	 * @param name of the Generator
	 * @return the Generator, or null if not found
	 */
	public static VanillaGenerator getGenerator(String name) {
		return generators.get(name);
	}
}
