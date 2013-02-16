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
package org.spout.vanilla.world.generator;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.spout.vanilla.world.generator.flat.FlatGenerator;
import org.spout.vanilla.world.generator.nether.NetherGenerator;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.skylands.SkylandsGenerator;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

public class VanillaGenerators {
	public static final NormalGenerator NORMAL = new NormalGenerator();
	public static final NetherGenerator NETHER = new NetherGenerator();
	public static final FlatGenerator FLAT = new FlatGenerator(64);
	public static final TheEndGenerator THE_END = new TheEndGenerator();
	public static final SkylandsGenerator SKYLANDS = new SkylandsGenerator();
	private static final Map<String, VanillaGenerator> BY_NAME = new HashMap<String, VanillaGenerator>();

	static {
		for (Field objectField : VanillaGenerators.class.getDeclaredFields()) {
			objectField.setAccessible(true);
			try {
				final Object object = objectField.get(null);
				if (object instanceof VanillaGenerator) {
					BY_NAME.put(objectField.getName().toLowerCase(), (VanillaGenerator) object);
				}
			} catch (Exception ex) {
				System.out.println("Could not properly reflect VanillaGenerators! Unexpected behaviour may occur, please report to http://issues.spout.org!");
				ex.printStackTrace();
			}
		}
	}

	public static VanillaGenerator byName(String name) {
		return BY_NAME.get(name.toLowerCase());
	}

	public static Collection<VanillaGenerator> getGenerators() {
		return BY_NAME.values();
	}
}
