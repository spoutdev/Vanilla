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
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.FurnaceInventory;
import org.spout.vanilla.inventory.WindowInventory;
import org.spout.vanilla.material.item.Armor;
import org.spout.vanilla.protocol.msg.TransactionMessage;
import org.spout.vanilla.protocol.msg.WindowClickMessage;
import org.spout.vanilla.util.InventoryUtil;

public final class WindowClickMessageHandler extends MessageHandler<WindowClickMessage> {
	@Override
	public void handleServer(Session session, Player player, WindowClickMessage message) {
		// Get the clicker
		Entity entity = player.getEntity();
		if (!(entity.getController() instanceof VanillaPlayer)) {
			System.out.println("Invalid player");
			return;
		}

		// Get the inventory
		VanillaPlayer controller = (VanillaPlayer) entity.getController();
		Inventory inventory = controller.getActiveInventory();
		if (inventory == null) {
			inventory = entity.getInventory();
		}

		// Player clicked outside of window
		if (message.getSlot() == 64537) {
			controller.setItemOnCursor(null);
			// TODO: Drop item
			respond(session, message, true);
			System.out.println("Clicked outside of window");
			return;
		}

		if (!(inventory instanceof WindowInventory)) {
			System.out.println("Invalid inventory");
			return;
		}

		WindowInventory window = (WindowInventory) inventory;
		int clickedSlot = window.getSlotIndex(message.getSlot());
		ItemStack cursorStack = controller.getItemOnCursor(), slotStack = inventory.getItem(clickedSlot);
		if (message.isShift()) {
			// TODO: Fix Shift clicking
			InventoryUtil.quickMoveStack(inventory, clickedSlot);
			System.out.println("Shift click");
		}

		if (message.isRightClick()) {
			// Right click
			if (slotStack != null) {
				if (cursorStack == null) {
					System.out.println("Right click - slot not null; cursor null");
					int amount = (slotStack.getAmount() + 1) / 2;
					slotStack = slotStack.setAmount(slotStack.getAmount() - amount);
					cursorStack = new ItemStack(slotStack.getMaterial(), amount);
				} else {
					System.out.println("Right click - slot not null; cursor not null");
					slotStack = slotStack.setAmount(slotStack.getAmount() + 1);
					cursorStack = cursorStack.setAmount(cursorStack.getAmount() - 1);
				}
			} else if (slotStack == null && cursorStack != null) {
				System.out.println("Right click - slot null; cursor not null");
				slotStack = new ItemStack(cursorStack.getMaterial(), 1);
				cursorStack = cursorStack.setAmount(cursorStack.getAmount() - 1);
			}
		} else if (slotStack != null) { // Left click
			if (cursorStack == null) {
				System.out.println("Left click - slot not null; cursor null");
				cursorStack = slotStack;
				slotStack = null;
			} else {
				InventoryUtil.mergeStack(cursorStack, slotStack);
				System.out.println("Left click - slot not null; cursor not null");
			}
		} else if (slotStack == null && cursorStack != null) {
			System.out.println("Left click - slot null; cursor not null");
			slotStack = cursorStack;
			cursorStack = null;
		}

		controller.setItemOnCursor(cursorStack);
		boolean response = window.onClicked(controller, clickedSlot, slotStack);
		System.out.println("Responded: " + response);
		respond(player.getSession(), message, response);
	}

	private void respond(Session session, WindowClickMessage message, boolean success) {
		session.send(new TransactionMessage(message.getWindowId(), message.getTransaction(), success));
	}
}
