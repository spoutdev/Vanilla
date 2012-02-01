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
 * the MIT license and the SpoutDev license version 1 along with this program.  
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license, 
 * including the MIT license.
 */
package org.spout.vanilla.protocol.handler;

import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.protocol.msg.WindowClickMessage;

public final class WindowClickMessageHandler extends MessageHandler<WindowClickMessage> {

	@Override
	public void handle(Session session, Player player, WindowClickMessage message) {
		/*if (player == null) {
			return;
		}

		SpoutInventory inv = player.getInventory();
		int slot = inv.getItemSlot(message.getSlot());

		// Modify slot if needed
		if (slot < 0) {
			inv = player.getInventory().getCraftingInventory();
			slot = inv.getItemSlot(message.getSlot());
		}
		if (slot == -1) {
			player.setItemOnCursor(null);
			response(session, message, true);
		}
		if (slot < 0) {
			response(session, message, false);
			player.getServer().getLogger().log(Level.WARNING, "Got invalid inventory slot {0} from {1}", new Object[] {message.getSlot(), player.getName()});
			return;
		}

		SpoutItemStack currentItem = inv.getItem(slot);

		if (player.getGameMode() == GameMode.CREATIVE && message.getId() == inv.getId()) {
			response(session, message, false);
			player.onSlotSet(inv, slot, currentItem);
			player.getServer().getLogger().log(Level.WARNING, "{0} tried to do an invalid inventory action in Creative mode!", new Object[] {player.getName()});
			return;
		}
		if (currentItem == null) {
			if (message.getItem() != -1) {
				player.onSlotSet(inv, slot, currentItem);
				response(session, message, false);
				return;
			}
		} else if (message.getItem() != currentItem.getTypeId() || message.getCount() != currentItem.getAmount() || message.getDamage() != currentItem.getDurability()) {
			player.onSlotSet(inv, slot, currentItem);
			response(session, message, false);
			return;
		}
		*/

		//if (message.isShift()) {
		//	if (false /* inv == player.getInventory().getOpenWindow() */) {
		//		// TODO: if player has e.g. chest open
		//	} else if (inv == player.getInventory().getCraftingInventory()) {
		//		// TODO: crafting stuff
		//	} else {
		//		if (slot < 9) {
		//			for (int i = 9; i < 36; ++i) {
		//				if (inv.getItem(i) == null) {
		//					// TODO: deal with item stacks
		//					inv.setItem(i, currentItem);
		//					inv.setItem(slot, null);
		//					response(session, message, true);
		//					return;
		//				}
		//			}
		//		} else {
		//			for (int i = 0; i < 9; ++i) {
		//				if (inv.getItem(i) == null) {
		//					// TODO: deal with item stacks
		//					inv.setItem(i, currentItem);
		//					inv.setItem(slot, null);
		//					response(session, message, true);
		//					return;
		//				}
		//			}
		//		}
		//	}
		//	response(session, message, false);
		//	return;
		//}

		/*if (inv == player.getInventory().getCraftingInventory() && slot == CraftingInventory.RESULT_SLOT && player.getItemOnCursor() != null) {
			response(session, message, false);
			return;
		}

		response(session, message, true);
		inv.setItem(slot, player.getItemOnCursor());
		player.setItemOnCursor(currentItem);

		if (inv == player.getInventory().getCraftingInventory() && slot == CraftingInventory.RESULT_SLOT && currentItem != null) {
			player.getInventory().getCraftingInventory().craft();
		}*/
	}

	//private void response(SpoutSession session, WindowClickMessage message, boolean success) {
	//	session.send(new TransactionMessage(message.getId(), message.getTransaction(), success));
	//}
}