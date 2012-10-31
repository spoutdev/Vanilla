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
package org.spout.vanilla;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import org.spout.api.Spout;

import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.solid.Stone;

import static org.junit.Assert.fail;

public class StaticInitialisationOrderTest {
	@Test
	public void materialStaticInitialisationTest() {
		try {
			new Stone("Test Stone", 87945);
			VanillaMaterials.initialize();
			for (Field field : VanillaMaterials.class.getFields()) {
				try {
					if (field == null || ((field.getModifiers() & (Modifier.STATIC | Modifier.PUBLIC)) != (Modifier.STATIC | Modifier.PUBLIC)) || !VanillaMaterial.class.isAssignableFrom(field.getType())) {
						continue;
					}
					VanillaMaterial material = (VanillaMaterial) field.get(null);
					if (material == null) {
						fail("invalid material: VanillaMaterials field '" + field.getName() + "' is null");
					}
				} catch (NoClassDefFoundError ex) {
					staticInitFail(ex);
				} catch (Throwable t) {
					t.printStackTrace();
					fail("invalid material: An exception occurred while loading/reading VanillaMaterials field '" + field.getName() + "'");
				}
			}
		} catch (NoClassDefFoundError t) {
			staticInitFail(t);
		}
	}

	public static void staticInitFail(Throwable t) {
		String s = "";
		while (t != null) {
			System.err.print(t);
			s += t.getMessage() + "\n";
			t.printStackTrace();
			t = t.getCause();
		}
		fail(s + ": Static initialisation of VanillaMaterials failed.");//Exception type does not matter! Static initialisation failure loses the exception data, turns into ClassLoader fail. :(
	}
}
