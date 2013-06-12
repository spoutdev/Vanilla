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
package org.spout.vanilla.protocol.handler.entity;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ClientSession;

import org.spout.vanilla.component.entity.inventory.EntityInventory;
import org.spout.vanilla.protocol.msg.entity.EntityEquipmentMessage;

public final class EntityEquipmentHandler extends MessageHandler<EntityEquipmentMessage> {
	@Override
	public void handleClient(ClientSession session, EntityEquipmentMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();
		World world = player.getWorld();

		int entityId = message.getEntityId();

		Entity entity = world.getEntity(entityId);
		if (entity == null) {
			player.getEngine().getLogger().warning("EntityEquipmentHandler entity don't exist");
			return;
		}

		EntityInventory inventory = entity.get(EntityInventory.class);
		if (inventory == null) {
			player.getEngine().getLogger().warning("EntityEquipmentHandler entity haven't EntityInventory");
			return;
		}

		ItemStack item = message.get();

		switch (message.getSlot()) {
			case 0:
				inventory.getHeldItem().setMaterial(item.getMaterial());
				inventory.getHeldItem().setAmount(item.getAmount());
				return;
			case 1:
				inventory.getArmor().getHelmet().setMaterial(item.getMaterial());
				inventory.getArmor().getHelmet().setAmount(item.getAmount());
				return;
			case 2:
				inventory.getArmor().getChestPlate().setMaterial(item.getMaterial());
				inventory.getArmor().getChestPlate().setAmount(item.getAmount());
				return;
			case 3:
				inventory.getArmor().getLeggings().setMaterial(item.getMaterial());
				inventory.getArmor().getLeggings().setAmount(item.getAmount());
				return;
			case 4:
				inventory.getArmor().getBoots().setMaterial(item.getMaterial());
				inventory.getArmor().getBoots().setAmount(item.getAmount());
				return;
			default:
				player.getEngine().getLogger().warning("EntityEquipmentHandler slot bad value");
		}
	}
}
