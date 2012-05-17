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
package org.spout.vanilla;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;

import static org.junit.Assert.fail;

public class staticInitialisationOrderTest {
	@Test
	public void materialStaticInitialisationTest() {
		try {
			VanillaMaterials.initialize();
			for (Field field : VanillaMaterials.class.getFields()) {
				try {
					if (field == null || ((field.getModifiers()&(Modifier.STATIC|Modifier.PUBLIC))!=(Modifier.STATIC|Modifier.PUBLIC)) || !VanillaMaterial.class.isAssignableFrom(field.getType())) {
						continue;
					}
					VanillaMaterial material = (VanillaMaterial) field.get(null);
					if (material == null) {
						fail("Vanilla Material field '" + field.getName() + "' is not yet initialized");
						continue;
					}
					try {
						material.initialize();
					} catch (Throwable t) {
						fail("An exception occurred while loading the properties of Vanilla Material '" + field.getName() + "':");
						t.printStackTrace();
					}
				} catch (NoClassDefFoundError ex) {
					staticInitFail();
				} catch (Throwable t) {
					fail("An exception occurred while reading Vanilla Material field '" + field.getName() + "':");
					t.printStackTrace();
				}
			}
		} catch (NoClassDefFoundError t) {
			staticInitFail();
		}
	}

	public static void staticInitFail() {
		fail("Static initialisation of VanillaMaterials failed! WHAT DID YOU DO?!");//Exception type does not matter! Static initialisation failure loses the exception data, turns into ClassLoader fail. :(
	}
}