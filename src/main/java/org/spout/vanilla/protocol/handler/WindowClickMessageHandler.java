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
import org.spout.vanilla.material.item.Armor;
import org.spout.vanilla.protocol.msg.TransactionMessage;
import org.spout.vanilla.protocol.msg.WindowClickMessage;
import org.spout.vanilla.util.InventoryUtil;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

public final class WindowClickMessageHandler extends MessageHandler<WindowClickMessage> {
	@Override
	public void handleServer(Session session, Player player, WindowClickMessage message) {
		// Get the clicker
		Entity entity = player.getEntity();
		if (!(entity.getController() instanceof VanillaPlayer)) {
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
			return;
		}

		// Check for validity of the slot
		int clickedSlot = VanillaMessageHandlerUtils.getSpoutInventorySlot(inventory, message.getSlot());
		System.out.println("Player inventory clicked!");
		System.out.println("Minecraft slot: " + message.getSlot());
		System.out.println("Spout slot: " + clickedSlot);
		if (clickedSlot < 0) {
			System.out.println("Error: Invalid slot!");
			return;
		}

		// Get the stacks
		ItemStack slotStack = inventory.getItem(clickedSlot);
		ItemStack cursorStack = controller.getItemOnCursor();

		// Handle the click
		ItemStack[] stacks = handleClick(message, inventory, clickedSlot, cursorStack, slotStack);
		cursorStack = stacks[0];
		slotStack = stacks[1];

		// Pass to inventory specific functions which will then respond to the transaction.
		if (inventory instanceof PlayerInventory) {
			handlePlayerInventory(controller, clickedSlot, (PlayerInventory) inventory, message, cursorStack, slotStack);
		}

		if (inventory instanceof FurnaceInventory) {
			handleFurnaceInventory(controller, clickedSlot, (FurnaceInventory) inventory, message, cursorStack, slotStack);
		}
	}

	private void handlePlayerInventory(VanillaPlayer controller, int clickedSlot, PlayerInventory inventory, WindowClickMessage message, ItemStack cursorStack, ItemStack slotStack) {
		Player player = controller.getPlayer();
		boolean armorSlot = clickedSlot == 36 || clickedSlot == 37 || clickedSlot == 41 || clickedSlot == 44;

		// Only allow armor in the armor slots
		if (armorSlot && cursorStack != null && !(cursorStack.getMaterial() instanceof Armor)) {
			respond(player.getSession(), message, false);
			return;
		}

		// Do not allow input in the output slot.
		if (clickedSlot == 40 && cursorStack != null) {
			respond(player.getSession(), message, false);
			return;
		}

		cursorStack = InventoryUtil.nullIfEmpty(cursorStack);
		slotStack = InventoryUtil.nullIfEmpty(slotStack);
		controller.setItemOnCursor(cursorStack);
		inventory.setItem(clickedSlot, slotStack);
		respond(player.getSession(), message, true);
	}

	private void handleFurnaceInventory(VanillaPlayer controller, int clickedSlot, FurnaceInventory inventory, WindowClickMessage message, ItemStack cursorStack, ItemStack slotStack) {
		Player player = controller.getPlayer();
		if (clickedSlot == 37 && cursorStack != null) {
			respond(player.getSession(), message, false);
			return;
		}

		cursorStack = InventoryUtil.nullIfEmpty(cursorStack);
		slotStack = InventoryUtil.nullIfEmpty(slotStack);
		controller.setItemOnCursor(cursorStack);
		inventory.setItem(clickedSlot, slotStack);
		respond(player.getSession(), message, true);
	}

	private ItemStack[] handleClick(WindowClickMessage message, Inventory inventory, int clickedSlot, ItemStack cursorStack, ItemStack slotStack) {
		// Right click
		if (message.isRightClick()) {
			if (!message.isShift()) {
				if (slotStack != null && cursorStack != null && cursorStack.equalsIgnoreSize(slotStack)) {
					InventoryUtil.mergeStack(cursorStack, slotStack, 1);
				} else if (slotStack != null && cursorStack == null) {
					int amount = (slotStack.getAmount() + 1) / 2;
					slotStack.setAmount(slotStack.getAmount() - amount);
					cursorStack = new ItemStack(slotStack.getMaterial(), amount);
				} else if (slotStack == null && cursorStack != null) {
					slotStack = new ItemStack(cursorStack.getMaterial(), 1);
					cursorStack.setAmount(cursorStack.getAmount() - 1);
				}
			}
		}

		// Left click
		if (!message.isRightClick()) {
			if (!message.isShift()) {
				if (slotStack != null && cursorStack != null && cursorStack.equalsIgnoreSize(slotStack)) {
					InventoryUtil.mergeStack(cursorStack, slotStack);
				} else if (slotStack != null && cursorStack == null) {
					cursorStack = slotStack;
					slotStack = null;
				} else if (slotStack == null && cursorStack != null) {
					slotStack = cursorStack;
					cursorStack = null;
				}
			} else if (slotStack != null) {
				InventoryUtil.quickMoveStack(inventory, clickedSlot);
			}
		}

		return new ItemStack[]{cursorStack, slotStack};
	}

	private void respond(Session session, WindowClickMessage message, boolean success) {
		session.send(new TransactionMessage(message.getWindowId(), message.getTransaction(), success));
	}
}
