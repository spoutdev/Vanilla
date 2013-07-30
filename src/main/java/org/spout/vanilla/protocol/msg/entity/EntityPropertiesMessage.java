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
package org.spout.vanilla.protocol.msg.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

public class EntityPropertiesMessage extends EntityMessage {

	private final Map<EntityProperties, Double> properties = new HashMap<EntityProperties, Double>();

	public EntityPropertiesMessage(int id) {
		super(id);
	}

	public void addProperty(EntityProperties property, double value) {
		properties.put(property, value);
	}

	public Map<EntityProperties,Double> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

	@Override
	public String toString() {
		ToStringBuilder stringBuilder = new ToStringBuilder(this, SpoutToStringStyle.INSTANCE).append("id", this.getEntityId());
		for (Map.Entry<EntityPropertiesMessage.EntityProperties, Double> value: properties.entrySet()) {
			stringBuilder.append("entityProperties", value.getKey().getName());
			stringBuilder.append("value", value.getValue());
		}
		return stringBuilder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EntityPropertiesMessage other = (EntityPropertiesMessage) obj;
		EqualsBuilder equalsBuilder =  new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId());
		if (this.properties.size() == other.properties.size()) {
			Iterator<Map.Entry<EntityProperties,Double>> iterator = other.properties.entrySet().iterator();
			for (Map.Entry<EntityPropertiesMessage.EntityProperties, Double> value: properties.entrySet()) {
				Map.Entry<EntityProperties, Double> otherValue = iterator.next();
				equalsBuilder.append(value.getKey(), otherValue.getKey());
				equalsBuilder.append(value.getValue(), otherValue.getValue());
			}
		} else {
			return false;
		}
		return equalsBuilder.isEquals();
	}

	public enum EntityProperties {

		GENERIC_MAXHEALTH("generic.maxHealth", 20),
		GENERIC_FOLLOWRANGE("generic.followRange", 32),
		GENERIC_KNOCKBACKRESISTANCE("generic.knockbackResistance", 0),
		GENERIC_MOVEMENTSPEED("generic.movementSpeed", 0.699999988079071),
		GENERIC_ATTACKDAMAGE("generic.attackDamage", 2.0),
		HORSE_JUMPSTRENGTH("horse.jumpStrength", 0.7),
		ZOMBIE_SPAWNREINFORCEMENTS("zombie.spawnReinforcements", 0);

		private final String name;
		private final double defaultValue;
		EntityProperties(String name, double defaultValue) {
			this.name = name;
			this.defaultValue = defaultValue;
		}

		public double getDefaultValue() {
			return defaultValue;
		}

		public String getName() {
			return name;
		}

		public static EntityProperties getByName(String name) {
			for (EntityProperties value : values()) {
				if (value.toString().equals(name)) {
					return value;
				}
			}
			return null;
		}
	}
}