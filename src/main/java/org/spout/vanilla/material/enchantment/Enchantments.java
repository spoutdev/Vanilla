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
package org.spout.vanilla.material.enchantment;

import java.util.HashMap;
import java.util.Map;

import org.spout.vanilla.material.enchantment.armor.AquaAffinity;
import org.spout.vanilla.material.enchantment.armor.BlastProtection;
import org.spout.vanilla.material.enchantment.armor.FeatherFalling;
import org.spout.vanilla.material.enchantment.armor.FireProtection;
import org.spout.vanilla.material.enchantment.armor.ProjectileProtection;
import org.spout.vanilla.material.enchantment.armor.Protection;
import org.spout.vanilla.material.enchantment.armor.Respiration;
import org.spout.vanilla.material.enchantment.bow.Flame;
import org.spout.vanilla.material.enchantment.bow.Infinity;
import org.spout.vanilla.material.enchantment.bow.Power;
import org.spout.vanilla.material.enchantment.bow.Punch;
import org.spout.vanilla.material.enchantment.sword.BaneOfArthropods;
import org.spout.vanilla.material.enchantment.sword.FireAspect;
import org.spout.vanilla.material.enchantment.sword.Knockback;
import org.spout.vanilla.material.enchantment.sword.Looting;
import org.spout.vanilla.material.enchantment.sword.Sharpness;
import org.spout.vanilla.material.enchantment.sword.Smite;
import org.spout.vanilla.material.enchantment.tool.Efficiency;
import org.spout.vanilla.material.enchantment.tool.Fortune;
import org.spout.vanilla.material.enchantment.tool.SilkTouch;
import org.spout.vanilla.material.enchantment.tool.Unbreaking;

public class Enchantments {
	private static final Map<Integer, Enchantment> idLookup = new HashMap<Integer, Enchantment>();
	private static final Map<String, Enchantment> nameLookup = new HashMap<String, Enchantment>();
	// ==Armor Enchantments==
	public static final Protection PROTECTION = register(new Protection("Protection", 0));
	public static final FireProtection FIRE_PROTECTION = register(new FireProtection("Fire Protection", 1));
	public static final FeatherFalling FEATHER_FALLING = register(new FeatherFalling("Feather Falling", 2));
	public static final BlastProtection BLAST_PROTECTION = register(new BlastProtection("Blast Protection", 3));
	public static final ProjectileProtection PROJECTILE_PROTECTION = register(new ProjectileProtection("Projectile Protection", 4));
	public static final Respiration RESPIRATION = register(new Respiration("Respiration", 5));
	public static final AquaAffinity AQUA_AFFINITY = register(new AquaAffinity("Aqua Affinity", 6));
	// ==Sword Enchantments==
	public static final Sharpness SHARPNESS = register(new Sharpness("Sharpness", 16));
	public static final Smite SMITE = register(new Smite("Smite", 17));
	public static final BaneOfArthropods BANE_OF_ARTHROPODS = register(new BaneOfArthropods("Bane of Arthropods", 18));
	public static final Knockback KNOCKBACK = register(new Knockback("Knockback", 19));
	public static final FireAspect FIRE_ASPECT = register(new FireAspect("Fire Aspect", 20));
	public static final Looting LOOTING = register(new Looting("Looting", 21));
	// ==Tool Enchantments==
	public static final Efficiency EFFICIENCY = register(new Efficiency("Efficiency", 32));
	public static final SilkTouch SILK_TOUCH = register(new SilkTouch("Silk Touch", 33));
	public static final Unbreaking UNBREAKING = register(new Unbreaking("Unbreaking", 34));
	public static final Fortune FORTUNE = register(new Fortune("Fortune", 35));
	// ==Bow Enchantments==
	public static final Power POWER = register(new Power("Power", 48));
	public static final Punch PUNCH = register(new Punch("Punch", 49));
	public static final Flame FLAME = register(new Flame("Flame", 50));
	public static final Infinity INFINITY = register(new Infinity("Infinity", 51));

	/**
	 * Registers the given enchantment
	 * @param enchantment Enchantment to register
	 * @return Registered enchantment
	 */
	public static <T extends Enchantment> T register(T enchantment) {
		if (idLookup.containsKey(enchantment.getId())) {
			throw new IllegalArgumentException("Enchantment ID '" + enchantment.getId() + "' has already been registered!");
		}

		idLookup.put(enchantment.getId(), enchantment);
		nameLookup.put(enchantment.getName().toLowerCase(), enchantment);
		return enchantment;
	}

	/**
	 * Gets an enchantment with the given id
	 * @param id id of the enchantment
	 * @return Enchantment with the given id, or null if not found
	 */
	public static Enchantment getById(int id) {
		return idLookup.get(id);
	}

	/**
	 * Gets an enchantment with the given name (case-insensitive)
	 * @param name Name of the enchantment
	 * @return Enchantment with the given name, or null if not found
	 */
	public static Enchantment getByName(String name) {
		return nameLookup.get(name.toLowerCase());
	}

	/**
	 * Gets all registered Enchantments
	 * @return Array of enchantments
	 */
	public static Enchantment[] values() {
		Enchantment[] values = new Enchantment[idLookup.size()];
		idLookup.values().toArray(values);
		return values;
	}
}
