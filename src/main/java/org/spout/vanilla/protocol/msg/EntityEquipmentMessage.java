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
package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.protocol.proxy.ConnectionInfo;
import org.spout.api.protocol.proxy.TransformableMessage;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.nbt.CompoundMap;
import org.spout.vanilla.protocol.proxy.VanillaConnectionInfo;

public final class EntityEquipmentMessage extends Message implements TransformableMessage {
	public static final int HELD_ITEM = 0;
	public static final int BOOTS_SLOT = 1;
	public static final int LEGGINGS_SLOT = 2;
	public static final int CHESTPLATE_SLOT = 3;
	public static final int HELMET_SLOT = 4;
	private int entityId, slot, id, count, damage;
	private CompoundMap nbtData;

	public EntityEquipmentMessage(int id, int slot)
	{
		this(id, slot, -1, 0, 0, null);
	}
	public EntityEquipmentMessage(int entityId, int slot, int id, int count, int damage, CompoundMap nbtData) {
		this.entityId = entityId;
		this.slot = slot;
		this.id = id;
		this.count = count;
		this.damage = damage;
		this.nbtData = nbtData;
	}

	public int getEntityId() {
		return entityId;
	}
	public int getSlot() {
		return slot;
	}
	public int getId() {
		return id;
	}
	public int getCount() {
		return count;
	}
	public int getDamage() {
		return damage;
	}
	public CompoundMap getNbtData() {
		return nbtData;
	}
	@Override
	public Message transform(boolean upstream, int connects, ConnectionInfo info, ConnectionInfo auxChannelInfo) {
		if (entityId == ((VanillaConnectionInfo) info).getEntityId()) {
			entityId = ((VanillaConnectionInfo) auxChannelInfo).getEntityId();
		} else if (entityId == ((VanillaConnectionInfo) auxChannelInfo).getEntityId()) {
			entityId = ((VanillaConnectionInfo) info).getEntityId();
		}
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("entityId", entityId)
				.append("slot", slot)
				.append("id", id)
				.append("count", count)
				.append("damage", damage)
				.append("nbtData", nbtData)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EntityEquipmentMessage other = (EntityEquipmentMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.entityId, other.entityId)
				.append(this.slot, other.slot)
				.append(this.id, other.id)
				.append(this.count, other.count)
				.append(this.damage, other.damage)
				.append(this.nbtData, other.nbtData)
				.isEquals();
	}
}
