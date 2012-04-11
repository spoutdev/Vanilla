/*
 * This file is part of Vanilla (http://www.spout.org/).
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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.handler;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.CreativePlayer;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.protocol.msg.TransactionMessage;
import org.spout.vanilla.protocol.msg.WindowClickMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

public final class WindowClickMessageHandler extends MessageHandler<WindowClickMessage> {

	@Override
	public void handleServer(Session session, Player player, WindowClickMessage message) {
		if (player == null || player.getEntity() == null) {
			return;
		}
		Entity entity = player.getEntity();
		PlayerInventory inv = (PlayerInventory) entity.getInventory();
		VanillaPlayer vplayer = (VanillaPlayer) entity.getController();
		Inventory inventory = vplayer.getActiveInventory();
		if (inventory == null) {
			inventory = inv;
		}
		int slot = VanillaMessageHandlerUtils.networkInventorySlotToSpout(message.getSlot());
		if( message.getSlot() == 64537) {
			vplayer.setItemOnCursor(null);
			//TODO drop
			response(session, message, true);
			return;
		}
		if (slot < 0) {
			response(session, message, false);
				session.getGame().getLogger().log(Level.WARNING, "Got invalid inventory slot {0} from {1}", new Object[]{message.getSlot(), player.getName()});
			return;
		}
		ItemStack currentItem = inventory.getItem(slot);
		if (CreativePlayer.is(entity.getController()) && message.getWindowId() == VanillaMessageHandlerUtils.getInventoryId(inv.getClass())) {
			response(session, message, false);
			session.getGame().getLogger().log(Level.WARNING, "{0} tried to do an invalid inventory action in Creative mode!", new Object[]{player.getName()});
			return;
		}

		if (message.isShift()) {
			if (currentItem == null) {
				response(session, message, true);
				return;
			}
			int l1, l2;
			if (slot < 9) {
				l1 = 0;
				l2 = 8;
				player.sendMessage("You shifted in your quickbar");
			} else {
				l1 = 9;
				l2 = inventory.getSize() - 1;
				player.sendMessage("You shifted in your main inventory!");
			}
			Set<Integer> hiddenSlots = new HashSet<Integer>();
			for (int i = l1; i <= l2; i++) {
				if (inventory.isHiddenSlot(i)) {
					hiddenSlots.add(i);
				}
				inventory.setHiddenSlot(i, true);
			}
			inventory.addItem(currentItem, false);
			inventory.setItem(currentItem, slot);
			for (int i = l1; i <= l2; i++) {
				if (!(hiddenSlots.contains(i))) {
					inventory.setHiddenSlot(i, false);
				}
			}
			response(session, message, true);
			return;
		}

		if (vplayer.getItemOnCursor() == null) { //no item on the cursor
			if (currentItem == null) {
				response(session, message, true);
				return;
			}
			if (message.isRightClick()) {
				int amount = currentItem.getAmount();
				currentItem.setAmount(amount / 2);
				inventory.setItem(currentItem, slot);
				ItemStack newStack = currentItem.clone();
				if (amount % 2 == 0) {
					newStack.setAmount(amount / 2);
				} else {
					newStack.setAmount(amount / 2 + 1);
				}
				vplayer.setItemOnCursor(newStack);
			} else {
				vplayer.setItemOnCursor(currentItem);
				inventory.setItem(null, slot);
				response(session, message, true);
			}
			return;
		} else { //got an item on the cursor
			ItemStack cursor = vplayer.getItemOnCursor();
			if (currentItem == null && !message.isRightClick()) {
				inventory.setItem(cursor, slot);
				vplayer.setItemOnCursor(null);
			} else if (currentItem == null && message.isRightClick()) {
				currentItem = cursor.clone();
				cursor.setAmount(cursor.getAmount() - 1);
				if (cursor.getAmount() == 0) {
					cursor = null;
				}
				vplayer.setItemOnCursor(cursor);
				currentItem.setAmount(1);
				inventory.setItem(currentItem, slot);
			} else if (currentItem != null && message.isRightClick()) { //TODO check for stack size limits, also shift clicking
				if (currentItem.equalsIgnoreSize(cursor)) {
					if (currentItem.getMaterial().getMaxStackSize() == currentItem.getAmount()) {
						response(session, message, true);
						return;
					}
					currentItem.setAmount(currentItem.getAmount() + 1);
					inventory.setItem(currentItem, slot);
					cursor.setAmount(cursor.getAmount() - 1);
					if (cursor.getAmount() == 0) {
						cursor = null;
					}
					vplayer.setItemOnCursor(cursor);
					inv.setItem(currentItem, slot);
				} else {
					ItemStack temp = cursor.clone();
					vplayer.setItemOnCursor(currentItem.clone());
					inv.setItem(temp, slot);
				}
			} else if (currentItem != null && !message.isRightClick()) {
				if (currentItem.equalsIgnoreSize(cursor)) {
					currentItem.setAmount(currentItem.getAmount() + cursor.getAmount());
					vplayer.setItemOnCursor(null);
					if (currentItem.getAmount() > currentItem.getMaterial().getMaxStackSize()) {
						ItemStack is = currentItem.clone();
						is.setAmount(currentItem.getAmount() - currentItem.getMaterial().getMaxStackSize());
						currentItem.setAmount(currentItem.getMaterial().getMaxStackSize());
						vplayer.setItemOnCursor(is);
					}
					inv.setItem(currentItem, slot);
				} else {
					ItemStack temp = cursor.clone();
					vplayer.setItemOnCursor(currentItem.clone());
					inv.setItem(temp, slot);
				}
			}
			response(session, message, true);
			return;
		}
		/*
		 * if (currentItem == null) { if (message.getItem() != -1) {
		 * player.getNetworkSynchronizer().onSlotSet(inv, slot, currentItem);
		 * response(session, message, false); return; } } else if (message.getItem()
		 * != currentItem.getMaterial().getId() || message.getCount() !=
		 * currentItem.getAmount() || message.getDamage() != currentItem.getData())
		 * { player.getNetworkSynchronizer().onSlotSet(inv, slot, currentItem);
		 * response(session, message, false); return; }
		 *
		 * if (message.isShift()) { //if (inv ==
		 * player.getInventory().getOpenWindow()) { // TODO: if player has e.g.
		 * chest open //} { if (slot < 9) { for (int i = 9; i < 36; ++i) { if
		 * (inv.getItem(i) == null) { // TODO: deal with item stacks
		 * inv.setItem(currentItem, i); inv.setItem(null, slot); response(session,
		 * message, true); return; } } } else { for (int i = 0; i < 9; ++i) { if
		 * (inv.getItem(i) == null) { // TODO: deal with item stacks
		 * inv.setItem(currentItem, i); inv.setItem(null, slot); response(session,
		 * message, true); return; } } } } response(session, message, false);
		 * return; }
		 *
		 * /*
		 * if (inv == player.getInventory().getCraftingInventory() && slot ==
		 * CraftingInventory.RESULT_SLOT && player.getItemOnCursor() != null) {
		 * response(session, message, false); return; }
		 *
		 * response(session, message, true); inv.setItem(slot,
		 * player.getItemOnCursor()); player.setItemOnCursor(currentItem);
		 *
		 * if (inv == player.getInventory().getCraftingInventory() && slot ==
		 * CraftingInventory.RESULT_SLOT && currentItem != null) {
		 * player.getInventory().getCraftingInventory().craft(); }
		 */
	}

	private void response(Session session, WindowClickMessage message, boolean success) {
		session.send(new TransactionMessage(message.getWindowId(), message.getTransaction(), success));
	}
}
