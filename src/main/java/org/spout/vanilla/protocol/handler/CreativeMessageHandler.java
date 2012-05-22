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
package org.spout.vanilla.protocol.handler;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.msg.CreativeMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

public class CreativeMessageHandler extends MessageHandler<CreativeMessage> {
	@Override
	public void handleServer(Session session, Player player, CreativeMessage message) {
		Entity entity = player.getEntity();
		if (!(entity.getController() instanceof VanillaPlayer)) {
			return;
		}

		VanillaPlayer controller = (VanillaPlayer) entity.getController();
		if (controller.isSurvival()) {
			player.kick("Now now, don't try that here. Won't work.");
			return;
		}

		Inventory inventory = controller.getActiveInventory();
		if (inventory == null) {
			inventory = entity.getInventory();
		}

		int slot = message.getSlot();
		slot = VanillaMessageHandlerUtils.getSpoutInventorySlot(inventory, slot);
		if (slot < 0 || slot >= entity.getInventorySize()) {
			return;
		}

		ItemStack newItem;
		if (message.getId() == -1) {
			//something to be done here?
			newItem = null;
		} else {
			Material material = VanillaMaterials.getMaterial(message.getId());
			if (material != null && message.getDamage() != 0) {
				material = material.getSubMaterial(message.getDamage());
			}
			if (material != null) {
				newItem = new ItemStack(material, message.getAmount());
			} else {
				player.kick("Unknown item ID: " + message.getId() + " and durability " + message.getDamage() + "!");
				return;
			}
		}

		entity.getInventory().setItem(slot, newItem);
		/*
		 * if (currentItem != null) { player.setItemOnCursor(currentItem); } else {
		 * player.setItemOnCursor(null);
		}
		 */
	}

	public boolean checkValidId(short id, short data) {
		Material mat = VanillaMaterials.getMaterial(id);
		return mat != null && (data == 0 || mat.getSubMaterial(data) != null);
	}
}
