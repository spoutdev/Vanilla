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
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
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
		VanillaPlayer vplayer = (VanillaPlayer) entity.getController();
		Inventory inventory = vplayer.getActiveInventory();
		if (inventory == null) {
			inventory = entity.getInventory();
		}

		if (message.getSlot() == 64537) {
			vplayer.setItemOnCursor(null);
			//TODO drop
			respond(true);
			return;
		}

		int clickedSlot = VanillaMessageHandlerUtils.getSpoutInventorySlot(inventory, message.getSlot());
		System.out.println("Clicked: " + clickedSlot);
		String errorMsg = "";
		Object errorParams = null;
		if (clickedSlot < 0) {
			errorMsg = "Got invalid inventory slot {0} from {1}";
			errorParams = new Object[]{message.getSlot(), player.getName()};
		} else if (!vplayer.isSurvival() && message.getWindowId() == VanillaMessageHandlerUtils.getInventoryId(inventory.getClass())) {
			errorMsg = "{0} tried to do an invalid inventory action in Creative mode!";
			errorParams = new Object[]{player.getName()};
		}

		if (!errorMsg.equals("")) {
			session.getGame().getLogger().log(Level.WARNING, errorMsg, errorParams);
			respond(false);
			return;
		}

		ItemStack slotStack = inventory.getItem(clickedSlot);
		ItemStack cursorStack = vplayer.getItemOnCursor();
		boolean emptyCursor = cursorStack == null;
		boolean emptySlot = slotStack == null;
		boolean neitherIsEmpty = !emptySlot && !emptyCursor;
		boolean leftClick = !message.isRightClick();

		// Determine action
		if (emptyCursor && emptySlot) {
			// Do Nothing!
		} else if (message.isShift()) {
			// Ignore cursorStack; move slotStack to/from inventory if it exists.
			if (!emptySlot) {
				moveStackToFromQuickbar(inventory, clickedSlot, player);
				slotStack = null;
			}
		} else if (leftClick && !neitherIsEmpty || (neitherIsEmpty && !cursorStack.equalsIgnoreSize(slotStack))) {
			// Left click with one side empty OR two different types of stacks => Swap stack positions!
			ItemStack tmp = slotStack;
			slotStack = cursorStack;
			cursorStack = tmp;
		} else if (emptyCursor) {
			// Empty handed right-click on stack: Split stack
			cursorStack = splitStack(slotStack);
		} else {
			// Clicked while holding something, move items from cursorStack
			int amountToMove = cursorStack.getAmount();
			if (!leftClick) {
				// Move 1 item from cursor to slot (slotStack can only be null if right-click).
				if (slotStack == null) {
					slotStack = cursorStack.clone();
					slotStack.setAmount(0);
				}
				amountToMove = 1;
			}

			mergeStack(cursorStack, slotStack, amountToMove, true);
		}

		if (emptyCursor && !emptySlot && leftClick) {
			slotStack = null;
		}

		cursorStack = nullIfEmpty(cursorStack);
		slotStack = nullIfEmpty(slotStack);
		vplayer.setItemOnCursor(cursorStack);
		inventory.setItem(slotStack, clickedSlot);
		respond(true);
	}

	private ItemStack splitStack(ItemStack stack) {
		ItemStack newStack = stack.clone();
		newStack.setAmount(0);
		mergeStack(stack, newStack, (stack.getAmount() + 1) / 2, true);
		return newStack;
	}

	private int mergeStack(ItemStack from, ItemStack to, int count, boolean tryToFill) {
		if (from == null || to == null) {
			return 0;
		}

		int fromAmount = from.getAmount();
		int toAmount = to.getAmount();
		int toFreeSpace = to.getMaterial().getMaxStackSize() - toAmount;

		if (count <= 0 || !tryToFill && (fromAmount < count || toFreeSpace < count)) {
			return 0;
		}

		if (tryToFill) {
			count = Math.min(fromAmount, count);
			count = Math.min(toFreeSpace, count);
		}

		from.setAmount(fromAmount - count);
		to.setAmount(toAmount + count);
		return count;
	}

	private ItemStack nullIfEmpty(ItemStack s) {
		return (s != null && s.getAmount() == 0) ? null : s;
	}

	private void moveStackToFromQuickbar(Inventory inv, int pos, Player player) {
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
