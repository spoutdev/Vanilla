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
package org.spout.vanilla.protocol.handler.window;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.component.inventory.window.block.BrewingStandWindow;
import org.spout.vanilla.component.inventory.window.block.CraftingTableWindow;
import org.spout.vanilla.component.inventory.window.block.DispenserWindow;
import org.spout.vanilla.component.inventory.window.block.EnchantmentTableWindow;
import org.spout.vanilla.component.inventory.window.block.FurnaceWindow;
import org.spout.vanilla.component.inventory.window.block.chest.ChestWindow;
import org.spout.vanilla.component.inventory.window.entity.VillagerWindow;
import org.spout.vanilla.component.substance.material.chest.Chest;
import org.spout.vanilla.inventory.block.BrewingStandInventory;
import org.spout.vanilla.inventory.block.DispenserInventory;
import org.spout.vanilla.inventory.block.EnchantmentTableInventory;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.inventory.entity.VillagerInventory;
import org.spout.vanilla.protocol.msg.window.WindowOpenMessage;

public class WindowOpenHandler extends MessageHandler<WindowOpenMessage> {

	@Override
	public void handleClient(Session session, WindowOpenMessage message) {
		Player player = ((Client) Spout.getEngine()).getActivePlayer();
		switch (message.getType()) {
			case DEFAULT:
				break;
			case CHEST:
				player.add(ChestWindow.class).init(new Chest()).open();
				break;
			case CRAFTING_TABLE:
				player.add(CraftingTableWindow.class).open();
				break;
			case FURNACE:
				player.add(FurnaceWindow.class).init(new FurnaceInventory()).open();
				break;
			case DISPENSER:
				player.add(DispenserWindow.class).init(new DispenserInventory()).open();
				break;
			case ENCHANTMENT_TABLE:
				player.add(EnchantmentTableWindow.class).init(new EnchantmentTableInventory()).open();
				break;
			case BREWING_STAND:
				player.add(BrewingStandWindow.class).init(new BrewingStandInventory()).open();
				break;
			case VILLAGER:
				player.add(VillagerWindow.class).init(new VillagerInventory()).open();
				break;
			default:
				break;
		}
	}
}
