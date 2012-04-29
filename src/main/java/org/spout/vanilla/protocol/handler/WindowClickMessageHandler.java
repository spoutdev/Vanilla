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

import org.spout.api.entity.Entity;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.item.generic.Armor;
import org.spout.vanilla.protocol.msg.TransactionMessage;
import org.spout.vanilla.protocol.msg.WindowClickMessage;
import org.spout.vanilla.util.VanillaMessageHandlerUtils;

public final class WindowClickMessageHandler extends MessageHandler<WindowClickMessage> {
	private Session session;
	private WindowClickMessage message;

	@Override
	public void handleServer(Session session, Player player, WindowClickMessage message) {

		this.session = session;
		this.message = message;

		Entity entity = player.getEntity();
		VanillaPlayer controller = (VanillaPlayer) entity.getController();
		Inventory inventory = controller.getActiveInventory();
		if (inventory == null) {
			inventory = entity.getInventory();
		}

		if (message.getSlot() == 64537) {
			controller.setItemOnCursor(null);
			//TODO drop item
			respond(true);
			return;
		}

		int clickedSlot = VanillaMessageHandlerUtils.getSpoutInventorySlot(inventory, message.getSlot());
		System.out.println("Minecraft slot: " + message.getSlot());
		System.out.println("Spout slot: " + clickedSlot);
		if (clickedSlot < 0) {
			System.out.println("Error: Invalid slot");
			respond(false);
			return;
		}

		ItemStack slotStack = inventory.getItem(clickedSlot);
		ItemStack cursorStack = controller.getItemOnCursor();

		boolean armorSlot = clickedSlot == 36 || clickedSlot == 37 || clickedSlot == 41 || clickedSlot == 44;
		if (inventory instanceof PlayerInventory) {
			if (armorSlot && cursorStack != null && !(cursorStack.getMaterial() instanceof Armor)) {
				respond(false);
				return;
			}

			if (clickedSlot == 40 && cursorStack != null) {
				respond(false);
				return;
			}
		}

		if (message.isShift()) {
			if (!message.isRightClick()) {
				if (slotStack != null) {
					// Move from hot-bar to inventory, or vice-versa
					quickMoveStack(inventory, clickedSlot, player);
					slotStack = null;
				}
			} else {
				// TODO: Shift click right click does what?
			}
		} else {
			if (!message.isRightClick()) {
				if (cursorStack != null) {
					if (slotStack != null) {
						if (!cursorStack.equalsIgnoreSize(slotStack)) {
							// Swap stacks
							slotStack = cursorStack;
							cursorStack = slotStack;
						} else {
							// Try to fill slot stacks
							mergeStack(cursorStack, slotStack, cursorStack.getAmount(), true);
						}
					} else {
						// Put cursor in empty slot
						slotStack = cursorStack;
						cursorStack = null;
					}
				} else if (slotStack != null) {
					cursorStack = slotStack;
					slotStack = null;
				}
			} else {
				if (cursorStack != null) {
					if (slotStack != null) {
						if (cursorStack.equalsIgnoreSize(slotStack)) {
							// Dispose one of the stack into the slot stack.
							mergeStack(cursorStack, slotStack, 1, true);
						}
					} else {
						// Dispose on of the stack into the empty slot.
						cursorStack.setAmount(cursorStack.getAmount() - 1);
						slotStack = new ItemStack(cursorStack.getMaterial(), 1);
					}
				} else  if (slotStack != null) {
					// Split the stack
					int amount = slotStack.getAmount() / 2;
					slotStack.setAmount(amount);
					cursorStack = new ItemStack(slotStack.getMaterial(), amount);
				}
			}
		}

		cursorStack = nullIfEmpty(cursorStack);
		slotStack = nullIfEmpty(slotStack);
		controller.setItemOnCursor(cursorStack);
		inventory.setItem(slotStack, clickedSlot);
		respond(true);
	}

	private void mergeStack(ItemStack from, ItemStack to, int count, boolean tryToFill) {
		if (from == null || to == null) {
			return;
		}

		int fromAmount = from.getAmount();
		int toAmount = to.getAmount();
		int toFreeSpace = to.getMaterial().getMaxStackSize() - toAmount;

		if (count <= 0 || !tryToFill && (fromAmount < count || toFreeSpace < count)) {
			return;
		}

		if (tryToFill) {
			count = Math.min(fromAmount, count);
			count = Math.min(toFreeSpace, count);
		}

		from.setAmount(fromAmount - count);
		to.setAmount(toAmount + count);
	}

	private ItemStack nullIfEmpty(ItemStack s) {
		return (s != null && s.getAmount() == 0) ? null : s;
	}

	private void quickMoveStack(Inventory inv, int pos, Player player) {
		ItemStack theStack = inv.getItem(pos);
		// Assume item is in quick-bar.
		int startSlot = 0, stopSlot = 8;
		if (pos > stopSlot) {
			startSlot = 9;
			stopSlot = inv.getSize() - 1;
		}

		Set<Integer> hiddenSlots = new HashSet<Integer>();
		for (int i = startSlot; i <= stopSlot; i++) {
			if (inv.isHiddenSlot(i)) {
				hiddenSlots.add(i);
			}

			inv.setHiddenSlot(i, true);
		}

		inv.addItem(theStack, false);
		for (int i = startSlot; i <= stopSlot; i++) {
			if (!(hiddenSlots.contains(i))) {
				inv.setHiddenSlot(i, false);
			}
		}
	}

	private void respond(boolean success) {
		session.send(new TransactionMessage(message.getWindowId(), message.getTransaction(), success));
	}
}
