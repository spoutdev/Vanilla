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
package org.spout.vanilla.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an entity effect that is applied to an entity.
 */
public class Effect {
	private final Type type;
	private byte strength;
	private short duration;

	public Effect(Type type, byte strength, short duration) {
		this.type = type;
		this.strength = strength;
		this.duration = duration;
	}

	/**
	 * Gets the effect type applied.
	 * @return type the type of effect
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Gets the strength of the effect.
	 * @return strength of effect.
	 */
	public byte getStrength() {
		return strength;
	}

	/**
	 * Sets the strength of the effect.
	 * @param strength of effect
	 */
	public void setStrength(byte strength) {
		this.strength = strength;
	}

	/**
	 * Gets the duration of the effect.
	 * @return duration
	 */
	public short getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of the effect.
	 * @param duration
	 */
	public void setDuration(short duration) {
		this.duration = duration;
	}

	/**
	 * Decrements the effects duration by one.
	 */
	public void pulse() {
		duration--;
	}

	/**
	 * Represents a type of entity effect.
	 */
	public enum Type {
		SPEED(1),
		SLOWNESS(2),
		HASTE(3),
		MINING_FATIGUE(4),
		STRENGTH(5),
		INSTANT_HEAL(6),
		INSTANT_DAMAGE(7),
		JUMP_BOOST(8),
		NAUSEA(9),
		REGENERATION(10),
		RESISTANCE(11),
		FIRE_RESISTANCE(12),
		WATER_BREATHING(13),
		INVISIBILITY(14),
		BLINDNESS(15),
		NIGHT_VISION(16),
		HUNGER(17),
		WEAKNESS(18),
		POISON(19);

		private final byte id;
		private static final Map<Integer, Type> lookup = new HashMap<Integer, Type>();

		static {
			for (Type effect : Type.values()) {
				lookup.put((int) effect.getId(), effect);
			}
		}

		private Type(int id) {
			this.id = (byte) id;
		}

		/**
		 * Gets the type of effect by it's mapped numerical value.
		 * @param id
		 * @return effect of id.
		 */
		public static Type get(int id) {
			return lookup.get(id);
		}

		/**
		 * Gets a entity effects numerical identification number.
		 * @return id
		 */
		public byte getId() {
			return id;
		}
	}
}
