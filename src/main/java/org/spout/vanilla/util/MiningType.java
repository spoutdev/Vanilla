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
package org.spout.vanilla.util;

import java.util.HashMap;
import java.util.Map;

import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.material.item.tool.Pickaxe;
import org.spout.vanilla.material.item.tool.Spade;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Bow;
import org.spout.vanilla.material.item.weapon.Sword;

public enum MiningType {
	NONE(-1, Tool.class, Bow.class),
	PICKAXE(0, Pickaxe.class),
	AXE(1, Axe.class),
	SPADE(2, Spade.class),
	SWORD(3, Sword.class);
	public final short id;
	public final Class<?>[] cls;
	private static Map<Class<?>, MiningType> typeClassMap;//Not initialized here, see putClassType

	private MiningType(int id, Class<?>... cls) {
		this.id = (short) id;
		this.cls = cls;
		putClassType(cls, this);
	}

	private static void putClassType(Class<?> cls[], MiningType mt) {// Workaround for Java 6 limitation - no static variables accessed in enum constructor.
		if (typeClassMap == null) {
			typeClassMap = new HashMap<Class<?>, MiningType>();
		}
		for (Class<?> cl : cls) {
			typeClassMap.put(cl, mt);
		}
	}

	public static MiningType getType(Tool tool) {
		return typeClassMap.get(tool.getClass());
	}

	public boolean isInstance(Object o) {
		return this.isInstance(o.getClass());
	}

	public boolean isInstance(Class<?> cls) {
		return (typeClassMap.get(cls) == this || typeClassMap.get(cls.getSuperclass()) == this);
	}

	public final class MiningLevel {
		public static final int WOOD = 1;
		public static final int GOLD = 1;
		public static final int STONE = 2;
		public static final int IRON = 3;
		public static final int DIAMOND = 4;
	}
}
