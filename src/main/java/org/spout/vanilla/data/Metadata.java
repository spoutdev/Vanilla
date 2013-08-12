/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.data;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

/**
 * Represents a single Metadata field of an Entity that keeps track of changes. This is only read by and updated for Mojang clients/servers.
 *
 * @param <T> The type of value this metadata field contains.
 */
public abstract class Metadata<T> {
	/**
	 * The id which represents a byte parameter.
	 */
	public static final int TYPE_BYTE = Parameter.TYPE_BYTE;
	/**
	 * The id which represents a short parameter.
	 */
	public static final int TYPE_SHORT = Parameter.TYPE_SHORT;
	/**
	 * The id which represents an integer parameter.
	 */
	public static final int TYPE_INT = Parameter.TYPE_INT;
	/**
	 * The id which represents a float parameter.
	 */
	public static final int TYPE_FLOAT = Parameter.TYPE_FLOAT;
	/**
	 * The id which represents a string parameter.
	 */
	public static final int TYPE_STRING = Parameter.TYPE_STRING;
	/**
	 * The id which represents an item parameter.
	 */
	public static final int TYPE_ITEM = Parameter.TYPE_ITEM;
	/**
	 * The type of parameter.
	 */
	private final int type;
	/**
	 * The index.
	 */
	private final int index;
	/**
	 * The value last exposed
	 */
	private T synchedValue;

	/**
	 * Creates a new mutable parameter.
	 *
	 * @param type The type of parameter.
	 * @param index The index.
	 * @param value The value.
	 */
	public Metadata(int type, int index) {
		this.type = type;
		this.index = index;
		this.synchedValue = getValue();
	}

	/**
	 * Gets the type of the parameter.
	 *
	 * @return The type of the parameter.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the index of this parameter.
	 *
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the value of this parameter.
	 *
	 * @return The value.
	 */
	public abstract T getValue();

	/**
	 * Sets the value of this parameter.
	 *
	 * @param value to set to
	 */
	public abstract void setValue(T value);

	/**
	 * Calls {@link setValue(Object)} with the new value of the parameter.
	 *
	 * @param parameter to set to
	 */
	public void setParameter(Parameter<T> parameter) {
		setValue(parameter.getValue());
	}

	/**
	 * Updates the synchronized value with the current value obtained using {@link #getValue()}. If the value did not change, null is returned, otherwise a new Parameter with the new value is returned.
	 *
	 * @return Immutable Parameter instance with the new value if changed, or null for no change
	 */
	public Parameter<T> getUpdateParameter() {
		T current = this.getValue();
		if (this.synchedValue.equals(current)) {
			return null;
		}
		this.synchedValue = current;
		return getSpawnParameter();
	}

	/**
	 * Creates a new Parameter instance representing the current synchronized value of this Metadata. Note that further updates may be needed to refresh and use the live value instead.
	 *
	 * @return Immutable Parameter instance with the synchronized Value
	 */
	public Parameter<T> getSpawnParameter() {
		return new Parameter<T>(this.type, this.index, this.synchedValue);
	}

	@Override
	public String toString() {
		return "Metadata{type=" + type + ",index=" + index + ",synchedValue=" + synchedValue + "}";
	}

	/**
	 * Gets the Type ID that represents a given type. If no such type ID is available, -1 is returned instead.
	 *
	 * @param type to get the ID for
	 * @return type ID
	 */
	public static int getTypeId(Class<?> type) {
		if (Byte.class.isAssignableFrom(type)) {
			return TYPE_BYTE;
		} else if (Short.class.isAssignableFrom(type)) {
			return TYPE_SHORT;
		} else if (Integer.class.isAssignableFrom(type)) {
			return TYPE_INT;
		} else if (Float.class.isAssignableFrom(type)) {
			return TYPE_FLOAT;
		} else if (String.class.isAssignableFrom(type)) {
			return TYPE_STRING;
		} else if (ItemStack.class.isAssignableFrom(type)) {
			return TYPE_ITEM;
		} else {
			return -1;
		}
	}
}
