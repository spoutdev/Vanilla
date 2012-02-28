/*
 * This file is part of Vanilla.
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity.objects;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.EntityProtocolStore;
import org.spout.vanilla.Entity;
import org.spout.vanilla.entity.MinecraftEntity;

public class Pickup extends MinecraftEntity {
	
	private static final EntityProtocolStore entityProtocolStore = new EntityProtocolStore(); //TODO this is an annoying fix, someone with knowlege in entities get rid of this?

	@Override
	public EntityProtocol getEntityProtocol(int protocolId) {
		return entityProtocolStore.getEntityProtocol(protocolId);
	}

	public static void setEntityProtocol(int protocolId, EntityProtocol protocol) {
		entityProtocolStore.setEntityProtocol(protocolId, protocol);
	}
	
	private ItemStack is;
	private int roll;

	public Pickup(ItemStack is) {
		this.is = is;
		this.roll = 1;
	}

	@Override
	public void onAttached() {
		parent.setData(Entity.KEY, Entity.DroppedItem.id);
	}

	@Override
	public void onTick(float dt) {
	}

	public Material getMaterial() {
		return is.getMaterial();
	}

	public int getAmount() {
		return is.getAmount();
	}

	public short getDamage() {
		return is.getDamage();
	}

	public int getRoll() {
		return roll;
	}
}
