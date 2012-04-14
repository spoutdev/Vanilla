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
	private Session session;
	private Player player;
	private VanillaPlayer vplayer;
	private WindowClickMessage message;
	private Inventory inventory;
	private int clickedSlot;
	private ItemStack slotStack;
	private ItemStack cursorStack;

	@Override
	public void handleServer(Session session, Player player, WindowClickMessage message) {
		if (player == null || player.getEntity() == null) {
			return;
		}
		this.session = session;
		this.player = player;
		this.message = message;
		Entity entity = player.getEntity();
		vplayer = (VanillaPlayer) entity.getController();
		inventory = vplayer.getActiveInventory();
		if (inventory == null) {
			inventory = (PlayerInventory) entity.getInventory();
		}

		if(message.getSlot() == 64537) {
			vplayer.setItemOnCursor(null);
			//TODO drop
			response(session, message, true);
			return;
		}

		clickedSlot = VanillaMessageHandlerUtils.networkInventorySlotToSpout(message.getSlot());
		if (clickedSlot < 0) {
			response(session, message, false);
			session.getGame().getLogger().log(Level.WARNING, "Got invalid inventory slot {0} from {1}", new Object[]{message.getSlot(), player.getName()});
			return;
		}

		if (CreativePlayer.is(entity.getController()) && message.getWindowId() == VanillaMessageHandlerUtils.getInventoryId(inventory.getClass())) {
			response(session, message, false);
			session.getGame().getLogger().log(Level.WARNING, "{0} tried to do an invalid inventory action in Creative mode!", new Object[]{player.getName()});
			return;
		}

		slotStack = inventory.getItem(clickedSlot);
		cursorStack = vplayer.getItemOnCursor();		
		boolean emptyCursor = cursorStack == null;
		boolean emptySlot   = slotStack   == null;
		boolean neitherIsEmpty = !emptySlot && !emptyCursor;
		boolean leftClick = !message.isRightClick();

		if (emptyCursor && emptySlot) {
			// Do Nothing!
		} else if (message.isShift()) {
			// Ignore cursorStack but move slotStack to/from inventory if it exists.
			if (!emptySlot) {
				moveShiftClickedStack();
			}
		} else if (leftClick && !neitherIsEmpty || (neitherIsEmpty && !cursorStack.equalsIgnoreSize(slotStack))) { 
			// Left click with one side empty OR two different types of stacks => Swap stack positions!
			swapStacks();
		} else if (emptyCursor) { 
			// Empty handed right-click on stack: Split stack
			splitSlotStack();
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
			moveCursorItemToSlot(amountToMove);
		}
		
		updateItemStacks();
		response(session, message, true);
		/*
		 * if (clickedItem == null) { if (message.getItem() != -1) {
		 * player.getNetworkSynchronizer().onSlotSet(inv, slot, clickedItem);
		 * response(session, message, false); return; } } else if (message.getItem()
		 * != clickedItem.getMaterial().getId() || message.getCount() !=
		 * clickedItem.getAmount() || message.getDamage() != clickedItem.getData())
		 * { player.getNetworkSynchronizer().onSlotSet(inv, slot, clickedItem);
		 * response(session, message, false); return; }
		 *
		 * if (message.isShift()) { //if (inv ==
		 * player.getInventory().getOpenWindow()) { // TODO: if player has e.g.
		 * chest open //} { if (slot < 9) { for (int i = 9; i < 36; ++i) { if
		 * (inv.getItem(i) == null) { // TODO: deal with item stacks
		 * inv.setItem(clickedItem, i); inv.setItem(null, slot); response(session,
		 * message, true); return; } } } else { for (int i = 0; i < 9; ++i) { if
		 * (inv.getItem(i) == null) { // TODO: deal with item stacks
		 * inv.setItem(clickedItem, i); inv.setItem(null, slot); response(session,
		 * message, true); return; } } } } response(session, message, false);
		 * return; }
		 *
		 * /*
		 * if (inv == player.getInventory().getCraftingInventory() && slot ==
		 * CraftingInventory.RESULT_SLOT && player.getItemOnCursor() != null) {
		 * response(session, message, false); return; }
		 *
		 * response(session, message, true); inv.setItem(slot,
		 * player.getItemOnCursor()); player.setItemOnCursor(clickedItem);
		 *
		 * if (inv == player.getInventory().getCraftingInventory() && slot ==
		 * CraftingInventory.RESULT_SLOT && clickedItem != null) {
		 * player.getInventory().getCraftingInventory().craft(); }
		 */
	}
	
	private void moveCursorItemToSlot(int maxAmount) {
		int cursorAmount = cursorStack.getAmount();
		int moveMax = Math.min(cursorAmount, maxAmount);
		moveMax = Math.min(moveMax, slotStack.getMaterial().getMaxStackSize() - slotStack.getAmount());
		int newAmount = slotStack.getAmount() + moveMax;
		if (moveMax > 0) {
			int newCursorAmount = cursorAmount - moveMax;
			if (newCursorAmount == 0)
				cursorStack = null;
			else
				cursorStack.setAmount(newCursorAmount);
			slotStack.setAmount(newAmount);			
		}
		
	}

	private void splitSlotStack() {
		int amount = slotStack.getAmount();
		cursorStack = slotStack.clone();
		cursorStack.setAmount((amount+1)/2); // round up by 1 if odd
		slotStack.setAmount(amount - cursorStack.getAmount()); 
		if (slotStack.getAmount() == 0)
			slotStack = null;
	}
	
	private void swapStacks() {
		ItemStack tmp = null;
		if (slotStack != null)
			tmp = slotStack.clone();
		slotStack = cursorStack;
		cursorStack = tmp;
		updateItemStacks();
	}
	
	private void updateItemStacks() {
		if (cursorStack != null && cursorStack.getAmount() == 0)
			cursorStack = null;
		if (slotStack != null && slotStack.getAmount() == 0)
			slotStack = null;
		vplayer.setItemOnCursor(cursorStack);
		inventory.setItem(slotStack, clickedSlot);
	}
	
	private void moveShiftClickedStack() {
		int l1, l2;
		if (clickedSlot < 9) {
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
		inventory.addItem(slotStack, false);
		inventory.setItem(slotStack, clickedSlot);
		for (int i = l1; i <= l2; i++) {
			if (!(hiddenSlots.contains(i))) {
				inventory.setHiddenSlot(i, false);
			}
		}
	}

	private void response(Session session, WindowClickMessage message, boolean success) {
		session.send(new TransactionMessage(message.getWindowId(), message.getTransaction(), success));
	}
}
